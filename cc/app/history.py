from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from config.firebase_config import db
import logging
import time

history_bp = Blueprint('history_bp', __name__)

@history_bp.route('/history', methods=['POST'])
@jwt_required()
def create_history():
    data = request.get_json()
    user_id = get_jwt_identity()
    image = data.get('image')
    result = data.get('result')

    if not image or not result:
        return jsonify({"message": "Image and result are required"}), 400

    try:
        doc_ref = db.collection('history').document()
        doc_ref.set({
            "user_id": user_id,
            "image": image,
            "created_at": int(time.time()),
            "result": result
        })

        return jsonify({"id": doc_ref.id}), 201
    except Exception as e:
        logging.error(f"Create history error: {e}")
        return jsonify({"message": str(e)}), 400

@history_bp.route('/history/<history_id>', methods=['GET'])
@jwt_required()
def get_history(history_id):
    user_id = get_jwt_identity()
    try:
        history_ref = db.collection('history').document(history_id)
        history_doc = history_ref.get()

        if not history_doc.exists:
            return jsonify({"message": "History not found"}), 404

        history_data = history_doc.to_dict()

        if history_data["user_id"] != user_id:
            return jsonify({"message": "Access denied"}), 403

        return jsonify(history_data), 200
    except Exception as e:
        logging.error(f"Get history error: {e}")
        return jsonify({"message": str(e)}), 400

@history_bp.route('/history/<history_id>', methods=['PUT'])
@jwt_required()
def update_history(history_id):
    data = request.get_json()
    user_id = get_jwt_identity()
    image = data.get('image')
    result = data.get('result')

    if not image or not result:
        return jsonify({"message": "Image and result are required"}), 400

    try:
        history_ref = db.collection('history').document(history_id)
        history_doc = history_ref.get()

        if not history_doc.exists:
            return jsonify({"message": "History not found"}), 404

        history_data = history_doc.to_dict()

        if history_data["user_id"] != user_id:
            return jsonify({"message": "Access denied"}), 403

        history_ref.update({
            "image": image,
            "result": result
        })

        return jsonify({"message": "History updated successfully"}), 200
    except Exception as e:
        logging.error(f"Update history error: {e}")
        return jsonify({"message": str(e)}), 400

@history_bp.route('/history/<history_id>', methods=['DELETE'])
@jwt_required()
def delete_history(history_id):
    user_id = get_jwt_identity()
    try:
        history_ref = db.collection('history').document(history_id)
        history_doc = history_ref.get()

        if not history_doc.exists:
            return jsonify({"message": "History not found"}), 404

        history_data = history_doc.to_dict()

        if history_data["user_id"] != user_id:
            return jsonify({"message": "Access denied"}), 403

        history_ref.delete()
        return jsonify({"message": "History deleted successfully"}), 200
    except Exception as e:
        logging.error(f"Delete history error: {e}")
        return jsonify({"message": str(e)}), 400
