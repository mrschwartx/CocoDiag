from flask import Flask
from flask_jwt_extended import JWTManager
from config.secret_manager import access_secret_version

def create_app():
    app = Flask(__name__)

    SECRET_PROJECT_ID = "cocodiag"
    SECRET_ID = "jwt-secret-key"
    VERSION_ID = "latest"
    SECRET_KEY = access_secret_version(SECRET_PROJECT_ID, SECRET_ID, VERSION_ID)

    app.config['JWT_SECRET_KEY'] = SECRET_KEY
    jwt = JWTManager(app)

    from app.auth import auth_bp
    from app.prediction import prediction_bp
    from app.price import price_bp
    from app.news import news_bp

    app.register_blueprint(auth_bp)
    app.register_blueprint(prediction_bp)
    app.register_blueprint(price_bp)
    app.register_blueprint(news_bp)

    return app

app = create_app()

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8080)