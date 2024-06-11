from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from config.firebase_config import db
import logging


forum_bp = Blueprint('forum_bp', __name__)

@forum_bp.route('/forum', methods=['POST'])
@jwt_required()
def create_post():
    data = request.get_json()
    user_id = get_jwt_identity()
    post = data.get('post')
    like_count = data.get('likeCount', 0)
    comment = data.get('comment', [])

    try:
        doc_ref = db.collection('forum').document()
        doc_ref.set({
            "user_id": user_id,
            "post": post,
            "likeCount": like_count,
            "comment": comment
        })

        return jsonify({"id": doc_ref.id}), 201
    except Exception as e:
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/forum/<post_id>', methods=['GET'])
def get_post(post_id):
    try:
        post_ref = db.collection('forum').document(post_id)
        post_doc = post_ref.get()

        if not post_doc.exists:
            raise Exception("Post not found")

        post_data = post_doc.to_dict()

        return jsonify(post_data), 200
    except Exception as e:
        logging.error(f"Get post error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/forum/<post_id>', methods=['PUT'])
@jwt_required()
def update_post(post_id):
    data = request.get_json()
    post = data.get('post')
    like_count = data.get('likeCount')
    comment = data.get('comment')

    try:
        post_ref = db.collection('forum').document(post_id)
        post_ref.update({
            "post": post,
            "likeCount": like_count,
            "comment": comment
        })

        return jsonify({"message": "Post updated successfully"}), 200
    except Exception as e:
        logging.error(f"Update post error: {e}")
        return jsonify({"message": str(e)}), 400

@forum_bp.route('/forum/<post_id>', methods=['DELETE'])
@jwt_required()
def delete_post(post_id):
    try:
        post_ref = db.collection('forum').document(post_id)
        post_ref.delete()
        return jsonify({"message": "Post deleted successfully"}), 200
    except Exception as e:
        logging.error(f"Delete post error: {e}")
        return jsonify({"message": str(e)}), 400