from flask import Blueprint, request, jsonify
from firebase_admin import storage
import tensorflow as tf
import numpy as np
from PIL import Image
import io
import logging
import time
from flask_jwt_extended import jwt_required, get_jwt_identity
import json
from config.firebase_config import db

prediction_bp = Blueprint('prediction_bp', __name__)

def download_and_load(bucket_name, bucket_path, local_path=None, is_json=False):
    try:
        bucket = storage.bucket(bucket_name)
        blob = bucket.blob(bucket_path)
        
        if is_json:
            content = blob.download_as_text()
            return json.loads(content)
        else:
            blob.download_to_filename(local_path)
            return None
    except Exception as e:
        logging.error(f"Error downloading from bucket: {str(e)}")
        raise e

bucket_name = 'cocodiag-storage'
model_path = 'model/coconut_model.keras'
class_info_path = 'classes/class_info.json'
local_model_path = '/tmp/coconut_model.keras'

try:
    download_and_load(bucket_name, model_path, local_model_path)
    model = tf.keras.models.load_model(local_model_path)
    class_info = download_and_load(bucket_name, class_info_path, is_json=True)
except Exception as e:
    logging.error(f"Error loading resources: {str(e)}")
    model = None
    class_info = {}

class_names = list(class_info.keys())

def prepare_image(image, target_size):
    if image.mode != "RGB":
        image = image.convert("RGB")
    image = image.resize(target_size)
    image = np.array(image)
    image = image / 255.0
    image = np.expand_dims(image, axis=0)    
    return image

@prediction_bp.route('/predict', methods=['POST'])
@jwt_required()
def predict():    
    if 'imageFile' not in request.files:
        return jsonify({'error': 'Image file not provided'}), 400
    file = request.files['imageFile']
    if file.filename == '':
        return jsonify({'error': 'Empty file'}), 400

    try:
        image = Image.open(io.BytesIO(file.read()))
        processed_image = prepare_image(image, target_size=(224, 224)) 
        predictions = model.predict(processed_image)
        predicted_class_index = np.argmax(predictions, axis=1)[0]
        accuracy = np.max(predictions)

        if predicted_class_index < len(class_names):
            predicted_class = class_names[predicted_class_index]

        disease_info = class_info.get(predicted_class)

        response = {
            'label': predicted_class,
            'accuracy': f"{accuracy:.2%}", 
            'name': disease_info['name'],
            'caused_by': disease_info['caused_by'],
            'symptoms': disease_info['symptoms'],
            'controls': disease_info['controls'],
            'created_at': int(time.time())
        }

        user_id = get_jwt_identity()
        
        bucket = storage.bucket()
        blob = bucket.blob(f'uploads/{user_id}/{file.filename}')
        blob.upload_from_string(file.read(), content_type=file.content_type)
        image_url = blob.public_url

        doc_ref = db.collection('history').document()
        doc_ref.set({
            "user_id": user_id,
            "image": file.filename,
            "created_at": response['created_at'],
            "result": response,
            "image_url": image_url
        })
        
        return jsonify(response)
    except Exception as e:
        return jsonify({'error': str(e)}), 500