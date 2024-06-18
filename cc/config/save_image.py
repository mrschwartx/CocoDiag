import uuid
import logging
from firebase_admin import storage
from flask import jsonify

def allowed_file(filename):
    allowed_extensions = {'png', 'jpg', 'jpeg'}
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in allowed_extensions

def save_image(image_file, user_id, path):    
    try:
        image_filename = f"{uuid.uuid4()}-{image_file.filename}"
        firebase_bucket = storage.bucket('cocodiag.appspot.com')
        blob = firebase_bucket.blob(f"{path}/{user_id}/{image_filename}")
        blob.upload_from_file(image_file, content_type=image_file.content_type)
       
        image_url = blob.public_url
        
        return image_url
  
    except Exception as e:
        logging.error(f"Failed to upload image to Firebase Storage: {str(e)}")
        raise e