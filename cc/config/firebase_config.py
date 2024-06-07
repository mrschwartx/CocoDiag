import json
import logging
import firebase_admin
from firebase_admin import credentials, firestore, auth
from config.secret_manager import access_secret_version

def initialize_firebase():
    try:
        PROJECT_ID = "cocodiag"
        SECRET_ID = "firebase-config"
        VERSION_ID = "latest"

        firebase_config_json = access_secret_version(PROJECT_ID, SECRET_ID, VERSION_ID)
        firebase_config = json.loads(firebase_config_json)

        if not firebase_admin._apps:
            cred = credentials.Certificate(firebase_config)
            firebase_admin.initialize_app(cred)

        db = firestore.client()

        logging.info("Firebase initialized successfully.")
        return db

    except json.JSONDecodeError as e:
        logging.error(f"Error decoding JSON: {e}")
        raise
    except Exception as e:
        logging.error(f"General error: {e}")
        raise

db = initialize_firebase()