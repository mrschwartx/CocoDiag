from flask import Flask
from flask_jwt_extended import JWTManager
from config.secret_manager import access_secret_version
from datetime import timedelta

def create_app():
    app = Flask(__name__)

    SECRET_PROJECT_ID = "cocodiag"
    SECRET_ID = "jwt-secret-key"
    VERSION_ID = "latest"
    SECRET_KEY = access_secret_version(SECRET_PROJECT_ID, SECRET_ID, VERSION_ID)

    app.config['JWT_SECRET_KEY'] = SECRET_KEY
    app.config['JWT_ACCESS_TOKEN_EXPIRES'] = timedelta(weeks=1)
    jwt = JWTManager(app)

    from app.auth import auth_bp
    from app.prediction import prediction_bp
    from app.price import price_bp
    from app.news import news_bp
    from app.user import user_bp
    from app.history import history_bp
    from app.forum import forum_bp
    from app.image import image_bp

    app.register_blueprint(auth_bp)
    app.register_blueprint(prediction_bp)
    app.register_blueprint(price_bp)
    app.register_blueprint(news_bp)
    app.register_blueprint(user_bp)
    app.register_blueprint(history_bp)
    app.register_blueprint(forum_bp)
    app.register_blueprint(image_bp)

    return app

app = create_app()

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8080)