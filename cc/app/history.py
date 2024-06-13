from flask import Blueprint, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from config.firebase_config import db
import logging

history_bp = Blueprint('history_bp', __name__)

@history_bp.route('/history/<user_id>', methods=['GET'])
@jwt_required()
def get_history(user_id):
    try:
        current_user_id = get_jwt_identity()
        if current_user_id != user_id:
            return jsonify({"message": "Access denied"}), 403
        
        history_ref = db.collection('history').where('user_id', '==', user_id)
        docs = history_ref.stream()

        history_list = []
        for doc in docs:
            history_data = doc.to_dict()
            history_list.append({
                "history_id": doc.id,
                "label": history_data['result']['label'],
                "accuracy": history_data['result']['accuracy'],
                "name": history_data['result']['name'],
                "symptoms": history_data['result']['symptoms'],
                "controls": history_data['result']['controls'],
                "created_at": history_data['created_at'],
                "image_url": history_data['image_url']
            })

        if history_list:
            response = {
                "user_id": user_id,
                "history": history_list
            }
            return jsonify(response)
        else:
            return jsonify({'message': 'No history found for this user'}), 404
    except Exception as e:
        logging.error(f"Error fetching history: {str(e)}")
        return jsonify({'message': 'Cause fail'}), 500

@history_bp.route('/history/<user_id>/<history_id>', methods=['DELETE'])
@jwt_required()
def delete_history(user_id, history_id):
    try:
        current_user_id = get_jwt_identity()
        if current_user_id != user_id:
            return jsonify({"message": "Access denied"}), 403

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

@history_bp.route('/history/<user_id>', methods=['DELETE'])
@jwt_required()
def delete_all_history(user_id):
    try:
        current_user_id = get_jwt_identity()
        if current_user_id != user_id:
            return jsonify({"message": "Access denied"}), 403

        history_ref = db.collection('history').where('user_id', '==', user_id)
        docs = history_ref.stream()

        deleted_any = False
        for doc in docs:
            doc.reference.delete()
            deleted_any = True

        if deleted_any:
            return jsonify({"message": "All history deleted successfully"}), 200
        else:
            return jsonify({"message": "No history found for this user"}), 404
    except Exception as e:
        logging.error(f"Delete all history error: {e}")
        return jsonify({"message": str(e)}), 400