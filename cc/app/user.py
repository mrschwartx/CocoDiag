from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from config.firebase_config import auth, db
import logging
from config.save_image import allowed_file, save_image

user_bp = Blueprint('user_bp', __name__)

@user_bp.route('/user', methods=['PUT'])
@jwt_required()
def update_user():
    try:
        user_id = get_jwt_identity()
        
        data = request.form
        name = data.get('name')
        email = data.get('email')
        password = data.get('password')
        
        image_profile_file = request.files.get('imageProfile')

        image_url = None
        if image_profile_file:
            if not allowed_file(image_profile_file.filename):
                return jsonify({'error': 'Invalid file extension'}), 400
        
            image_url = save_image(image_profile_file, user_id, 'users')

        auth.update_user(
            user_id,
            email=email,
            password=password,
            display_name=name
        )

        user_info_ref = db.collection('users').document(user_id)
        update_data = {
            "name": name,
            "email": email,
        }
        
        if image_url:
            update_data["imageProfile"] = image_url
        
        user_info_ref.update(update_data)

        return jsonify({
            "id": user_id,
            "name": name,
            "email": email,
            "imageProfile": image_url
        }), 200
    except Exception as e:
        logging.error(f"Update user error: {e}")
        return jsonify({"message": str(e)}), 400

@user_bp.route('/user/<user_id>', methods=['GET'])
@jwt_required()
def find_user_by_id(user_id):
    try:
        user_info_ref = db.collection('users').document(user_id)
        user_info_doc = user_info_ref.get()

        if not user_info_doc.exists:
            raise Exception("User not found in Firestore")

        user_info = user_info_doc.to_dict()

        return jsonify({
            "id": user_id,
            "name": user_info.get('name'),
            "email": user_info.get('email'),
            "imageProfile": user_info.get('imageProfile')
        }), 200
    except Exception as e:
        logging.error(f"Find user error: {e}")
        return jsonify({"message": str(e)}), 400