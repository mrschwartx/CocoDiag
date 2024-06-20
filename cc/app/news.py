from flask import Blueprint, jsonify
from google.cloud import storage as gcs_storage
from app.auth import jwt_required
import json

news_bp = Blueprint('news_bp', __name__)

@news_bp.route('/getNews', methods=['GET'])
@jwt_required()
def get_news():
    client = gcs_storage.Client()
    bucket_name = "cocodiag-storage"
    file_path = "articles/article_data.json"

    try:
        bucket = client.bucket(bucket_name)
        blob = bucket.blob(file_path)
        data = blob.download_as_text()
        articles = json.loads(data)
        return jsonify(articles)

    except Exception as e:
        return jsonify({'error': str(e)}), 500