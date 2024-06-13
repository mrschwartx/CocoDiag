from flask import Blueprint, request, jsonify, send_file
from firebase_admin import storage
from PIL import Image
import io
import logging
from flask_jwt_extended import jwt_required
from urllib.parse import urlparse

image_bp = Blueprint('image_bp', __name__)

@image_bp.route('/image', methods=['GET'])
@jwt_required()
def get_image():
    img_url = request.args.get('img_url')
    if not img_url:
        return jsonify({'error': 'Image URL not provided'}), 400

    try:
        bucket_name = 'cocodiag.appspot.com'
                
        parsed_url = urlparse(img_url)
        blob_path = '/'.join(parsed_url.path.split('/')[2:])

        firebase_bucket = storage.bucket(bucket_name)
        blob = firebase_bucket.blob(blob_path)

        image_data = blob.download_as_bytes()
        image = Image.open(io.BytesIO(image_data))

        img_byte_arr = io.BytesIO()
        image.save(img_byte_arr, format=image.format)
        img_byte_arr.seek(0)

        return send_file(img_byte_arr, mimetype=f'image/{image.format.lower()}')
    except Exception as e:
        logging.error(f"Error fetching image: {str(e)}")
        return jsonify({'error': str(e)}), 500