from flask import Blueprint, jsonify, request
import requests
from config.secret_manager import access_secret_version
from app.auth import jwt_required

news_bp = Blueprint('news_bp', __name__)

PROJECT_ID = "cocodiag"
SECRET_ID = "news-api"
VERSION_ID = "latest"

NEWS_API_KEY = access_secret_version(PROJECT_ID, SECRET_ID, VERSION_ID)
NEWS_API_URL = 'https://newsapi.org/v2/everything'

@news_bp.route('/getNews', methods=['GET'])
@jwt_required()
def get_news():
    # TODO: Fix topics params
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
