# -*- coding: utf-8 -*-
"""Handling Error Outside Class.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/1D9YM-rBHIvmvOcXBxIuMlVv5WBTV5guO
"""

import tensorflow as tf
import numpy as np
from PIL import Image
import json
import logging
import time
import os

print(tf.__version__)

# Define local paths
local_model_path = '/content/model.keras'
local_class_info_path = '/content/class_info.json'

# Threshold untuk akurasi
threshold = 0.75

try:
    # Load model from local path
    model = tf.keras.models.load_model(local_model_path)

    # Load class info from local JSON file
    with open(local_class_info_path, 'r') as f:
        class_info = json.load(f)
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

def predict(image_path):
    if not os.path.exists(image_path):
        raise FileNotFoundError(f"Image file {image_path} not found")

    try:
        image = Image.open(image_path)
        processed_image = prepare_image(image, target_size=(224, 224))
        predictions = model.predict(processed_image)
        predicted_class_index = np.argmax(predictions, axis=1)[0]
        accuracy = np.max(predictions)

        if accuracy < threshold:
            predicted_class = 'Unknown'
            accuracy = None
            disease_info = {}
        else:
            if predicted_class_index < len(class_names):
                predicted_class = class_names[predicted_class_index]
                disease_info = class_info.get(predicted_class, {})
            else:
                raise ValueError('Predicted class index out of range')

        response = {
            'label': predicted_class,
            'prediction': predictions.tolist(),
            'predicted_class_index': predicted_class_index.tolist(),
            'predicted_class': predicted_class,
            'accuracy': f"{accuracy:.2%}" if accuracy else None,
            'name': disease_info.get('name', 'Unknown'),
            'symptoms': disease_info.get('symptoms', []),
            'control': disease_info.get('control', []),
            'created_at': int(time.time())
        }
        return response
    except Exception as e:
        logging.error(f"Error during prediction: {str(e)}")
        raise e

image_path = '/content/image.png'
try:
    result = predict(image_path)
    print(json.dumps(result, indent=4))
except Exception as e:
    print(f"Error: {str(e)}")