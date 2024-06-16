from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from config.firebase_config import db
from firebase_admin import storage
import logging
from datetime import datetime
import uuid
from urllib.parse import urlparse

forum_bp = Blueprint('forum_bp', __name__)

@forum_bp.route('/forum', methods=['POST'])
@jwt_required()
def create_post():
    data = request.form
    user_id = get_jwt_identity()
    post_text = data.get('post_text')
    post_image_file = request.files.get('post_image')

    try:
        user_ref = db.collection('users').document(user_id)
        user_doc = user_ref.get()
        if not user_doc.exists:
            raise Exception("User not found")


        if post_image_file:
            image_filename = f"{uuid.uuid4()}-{post_image_file.filename}"
            firebase_bucket = storage.bucket('cocodiag.appspot.com')
            blob = firebase_bucket.blob(f"forums/{user_id}/{image_filename}")
            post_image_file.seek(0)
            blob.upload_from_file(post_image_file, content_type= post_image_file.content_type)
            image_url = blob.public_url
        else:
            image_url = None

        doc_ref = db.collection('forum').document()
        doc_ref.set({
            "user_id": user_id,
            "post_text": post_text,
            "post_image": image_url,
            "count_like": 0,
            "count_comment": 0,
            "created_at": int(datetime.now().timestamp()),
            "updated_at": int(datetime.now().timestamp())
        })

        post_data = doc_ref.get().to_dict()
        post_data["post_id"] = doc_ref.id

        return jsonify(post_data), 201
    except Exception as e:
        logging.error(f"Create post error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/forum', methods=['GET'])
def get_latest_posts():
    limit = request.args.get('limit', 20)
    try:
        posts_ref = db.collection('forum').order_by('created_at', direction='DESCENDING').limit(int(limit))
        posts = posts_ref.stream()

        forums = []
        for post in posts:
            post_data = post.to_dict()
            post_data["post_id"] = post.id
            forums.append(post_data)

        return jsonify({"forums": forums}), 200
    except Exception as e:
        logging.error(f"Get latest posts error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/forum/<post_id>', methods=['GET'])
def get_post(post_id):
    try:
        post_ref = db.collection('forum').document(post_id)
        post_doc = post_ref.get()

        if not post_doc.exists:
            raise Exception("Post not found")

        post_data = post_doc.to_dict()
        post_data["post_id"] = post_doc.id

        return jsonify(post_data), 200
    except Exception as e:
        logging.error(f"Get post error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/forum/<post_id>', methods=['DELETE'])
@jwt_required()
def delete_post(post_id):
    user_id = get_jwt_identity()
    try:
        post_ref = db.collection('forum').document(post_id)
        post_doc = post_ref.get()

        if not post_doc.exists:
            raise Exception("Post not found")

        post_data = post_doc.to_dict()
        if post_data["user_id"] != user_id:
            return jsonify({"message": "You are not authorized to delete this post"}), 403
        
        image_url = post_data.get("post_image")

        if image_url:            
            try:
                bucket_name = 'cocodiag.appspot.com'        
                parsed_url = urlparse(image_url)
                blob_path = '/'.join(parsed_url.path.split('/')[2:])

                firebase_bucket = storage.bucket(bucket_name)
                blob = firebase_bucket.blob(blob_path)
                blob.delete()
                logging.info(f"Image {image_url} deleted successfully")
            except Exception as e:
                logging.error(f"Failed to delete image {image_url}: {e}")
                return jsonify({"message": "Failed to delete associated image"}), 500

        post_ref.delete()
        return jsonify({"message": "post has been deleted"}), 200
    except Exception as e:
        logging.error(f"Delete post error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/forum/like', methods=['POST'])
@jwt_required()
def like_unlike_post():
    data = request.get_json()
    post_id = data.get('post_id')
    like = data.get('like')

    try:
        post_ref = db.collection('forum').document(post_id)
        post_doc = post_ref.get()

        if not post_doc.exists:
            raise Exception("Post not found")

        post_data = post_doc.to_dict()
        if like:
            post_data["count_like"] += 1
        else:
            post_data["count_like"] -= 1

        post_ref.update({"count_like": post_data["count_like"]})
        post_data["post_id"] = post_doc.id

        return jsonify(post_data), 200
    except Exception as e:
        logging.error(f"Like/Unlike post error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/forum/comment', methods=['POST'])
@jwt_required()
def create_comment():
    data = request.get_json()
    user_id = get_jwt_identity()
    post_id = data.get('post_id')
    comment_text = data.get('comment')

    try:
        post_ref = db.collection('forum').document(post_id)
        post_doc = post_ref.get()

        if not post_doc.exists:
            raise Exception("Post not found")

        post_data = post_doc.to_dict()
        if comment_text:
            post_data["count_comment"] += 1
        else:
            raise Exception("Comment can't be empty")
        
        post_ref.update({"count_comment": post_data["count_comment"]})
        
        comment_ref = post_ref.collection('comments').document()
        comment_ref.set({
            "user_id": user_id,
            "comment": comment_text,
            "created_at": int(datetime.now().timestamp())
        })

        comments = post_ref.collection('comments').stream()
        comments_list = [{"user_id": comment.get('user_id'), 
                          "comment_id": comment.id, 
                          "comment": comment.get('comment'), 
                          "created_at": comment.get('created_at')} 
                          for comment in comments]

        return jsonify({"post_id": post_id, "comments": comments_list}), 201
    except Exception as e:
        logging.error(f"Create comment error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/comment/<comment_id>', methods=['DELETE'])
@jwt_required()
def delete_comment(comment_id):
    user_id = get_jwt_identity()
    try:
        posts_ref = db.collection('forum').stream()
        comment_ref = None
        for post in posts_ref:
            comment_doc_ref = db.collection('forum').document(post.id).collection('comments').document(comment_id)
            comment_doc = comment_doc_ref.get()
            if comment_doc.exists:
                comment_ref = comment_doc_ref
                break

        if not comment_ref:
            raise Exception("Comment not found")

        comment_data = comment_doc.to_dict()
        if comment_data["user_id"] != user_id:
            return jsonify({"message": "You are not authorized to delete this comment"}), 403

        comment_ref.delete()
        return jsonify({"message": "comment has been deleted"}), 200
    except Exception as e:
        logging.error(f"Delete comment error: {e}")
        return jsonify({"message": str(e)}), 400