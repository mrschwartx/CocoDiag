from flask import Blueprint, request, jsonify
from google.cloud import storage
import tensorflow as tf
import numpy as np
from PIL import Image
import io
import logging
import time
from flask_jwt_extended import jwt_required

prediction_bp = Blueprint('prediction_bp', __name__)

def download_model(bucket_name, bucket_path, local_path):
    try:
        client = storage.Client()
        bucket = client.get_bucket(bucket_name)
        blob = bucket.blob(bucket_path)
        blob.download_to_filename(local_path)
    except Exception as e:
        logging.error(f"Error downloading model: {str(e)}")
        raise e

bucket_name = 'cocodiag-storage'
model_path = 'model/coconut_model.keras'
local_model_path = '/tmp/coconut_model.keras'

try:
    download_model(bucket_name, model_path, local_model_path)
    model = tf.keras.models.load_model(local_model_path)
except Exception as e:
    logging.error(f"Error loading model: {str(e)}")
    model = None

class_names = ['Bud Root Dropping', 'Bud Rot', 'Gray Leaf spot', 'Leaf Rot', 'Stem Bleeding']

# TODO: Load class info from cloud storage
class_info = {
    'Bud Root Dropping': {
        'name': 'Bud Root Dropping',
        'symptoms': ['gejala 1', 'gejala 2'],
        'control': ['1. Tips number 1', '2. Tips number 2']
    },
    'Bud Rot': {
        'name': 'Bud Rot',
        'symptoms': ['gejala 1', 'gejala 2'],
        'control': ['1. Tips number 1', '2. Tips number 2']
    },
    'Gray Leaf spot': {
        'name': 'Gray Leaf spot',
        'symptoms': ['gejala 1', 'gejala 2'],
        'control': ['1. Tips number 1', '2. Tips number 2']
    },
    'Leaf Rot': {
        'name': 'Leaf Rot',
        'symptoms': ['gejala 1', 'gejala 2'],
        'control': ['1. Tips number 1', '2. Tips number 2']
    },
    'Stem Bleeding': {
        'name': 'Stem Bleeding',
        'symptoms': ['gejala 1', 'gejala 2'],
        'control': ['1. Tips number 1', '2. Tips number 2']
    }
}

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
        processed_image = prepare_image(image, target_size=(150, 150)) 
        predictions = model.predict(processed_image)
        predicted_class_index = np.argmax(predictions, axis=1)[0]
        accuracy = np.max(predictions)

        if predicted_class_index < len(class_names):
            predicted_class = class_names[predicted_class_index]

        # TODO: Add handling for outside classes
        disease_info = class_info.get(predicted_class)

        response = {
            'label': predicted_class,
            'accuracy': f"{accuracy:.2%}", 
            'name': disease_info['name'],
            'symptoms': disease_info['symptoms'],
            'control': disease_info['control'],
            'created_at': int(time.time())
        }

        return jsonify(response)
    except Exception as e:
        return jsonify({'error': str(e)}), 500