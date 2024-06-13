from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from config.firebase_config import db
import logging
from datetime import datetime

forum_bp = Blueprint('forum_bp', __name__)

@forum_bp.route('/forum', methods=['POST'])
@jwt_required()
def create_post():
    data = request.get_json()
    user_id = get_jwt_identity()
    post_text = data.get('post_text')
    post_image = str(data.get('post_image'))

    try:
        doc_ref = db.collection('forum').document()
        doc_ref.set({
            "user_id": user_id,
            "post_text": post_text,
            "post_image": post_image,
            "count_like": 0,
            "count_comment": 0,
            "created_at": int(datetime.now().timestamp()),
            "updated_at": int(datetime.now().timestamp())
        })

        post_data = doc_ref.get().to_dict()
        post_data["post_id"] = doc_ref.id

        return jsonify(post_data), 201
    except Exception as e:
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
    try:
        post_ref = db.collection('forum').document(post_id)
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

        comment_ref = post_ref.collection('comments').document()
        comment_ref.set({
            "user_id": user_id,
            "comment": comment_text,
            "created_at": int(datetime.now().timestamp())
        })

        post_data = post_doc.to_dict()
        comments = post_ref.collection('comments').stream()
        comments_list = [{"user_id": comment.get('user_id'), "comment_id": comment.id, "comment": comment.get('comment'), "created_at": comment.get('created_at')} for comment in comments]

        return jsonify({"post_id": post_id, "comments": comments_list}), 201
    except Exception as e:
        logging.error(f"Create comment error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/comment/<comment_id>', methods=['DELETE'])
@jwt_required()
def delete_comment(comment_id):
    try:
        comment_ref = db.collection_group('comments').document(comment_id)
        comment_ref.delete()
        return jsonify({"message": "comment has been deleted"}), 200
    except Exception as e:
        logging.error(f"Delete comment error: {e}")
        return jsonify({"message": str(e)}), 400
