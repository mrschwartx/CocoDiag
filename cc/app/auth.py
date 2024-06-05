from flask import Blueprint, request, jsonify
import pyrebase
from firebase_config import firebase_config

auth_bp = Blueprint('auth_bp', __name__)

firebase = pyrebase.initialize_app(firebase_config)
auth = firebase.auth()
db = firebase.database()

@auth_bp.route('/signup', methods=['POST'])
def signup():
    data = request.get_json()
    name = data.get('name')
    email = data.get('email')
    password = data.get('password')

    try:
        user = auth.create_user_with_email_and_password(email, password)
        user_id = user['localId']
        db.child("users").child(user_id).set({"name": name, "email": email})
        return jsonify({
            "id": user_id,
            "name": name,
            "email": email,
            "imageProfile": None
        }), 200
    except Exception as e:
        return jsonify({"message": str(e)}), 400

@auth_bp.route('/signin', methods=['POST'])
def signin():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')

    try:
        user = auth.sign_in_with_email_and_password(email, password)
        user_id = user['localId']
        user_info = db.child("users").child(user_id).get().val()
        return jsonify({
            "id": user_id,
            "name": user_info['name'],
            "email": user_info['email'],
            "imageProfile": None
        }), 200
    except Exception as e:
        return jsonify({"message": str(e)}), 400