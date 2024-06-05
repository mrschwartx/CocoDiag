from flask import Blueprint, jsonify, request
import requests
from google.cloud import secretmanager
from .auth import firebase_auth_required

news_bp = Blueprint('news_bp', __name__)

def access_secret_version(project_id, secret_id, version_id):
    client = secretmanager.SecretManagerServiceClient()
    name = f"projects/{project_id}/secrets/{secret_id}/versions/{version_id}"
    response = client.access_secret_version(request={"name": name})
    return response.payload.data.decode("UTF-8")

PROJECT_ID = "cocodiag"
SECRET_ID = "NEWS_API_KEY"
VERSION_ID = "latest"

NEWS_API_KEY = access_secret_version(PROJECT_ID, SECRET_ID, VERSION_ID)
NEWS_API_URL = 'https://newsapi.org/v2/everything'

@news_api_bp.route('/getNews', methods=['GET'])
@firebase_auth_required
def get_news():
    topics = [
        "coconut plantation",
        "(pohon AND kelapa)",
        "(buah AND kelapa)",
        "(manfaat AND kelapa)",
        "(menanam AND kelapa)",
        "(merawat AND kelapa)",
        "(petani AND kelapa)",
        "(perkebunan AND kelapa)",
        "(kebun AND kelapa)",
        '("benefits of coconut")',
        "(dari AND kelapa)",
        "(makanan AND kelapa)"
    ]

    query = " OR ".join(topics)
    params = {
        'q': query,
        'apiKey': NEWS_API_KEY,
        'sortBy': 'relevancy'
    }

    try:
        response = requests.get(NEWS_API_URL, params=params)
        response.raise_for_status()
        data = response.json()
        return jsonify(data['articles'])

    except requests.exceptions.RequestException as e:
        return jsonify({'error': str(e)}), 500
