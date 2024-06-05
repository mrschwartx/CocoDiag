from flask import Flask

def create_app():
    app = Flask(__name__)
    
    from .prediction import prediction_bp
    from .price import price_bp
    from .auth import auth_bp
    from .news import news_bp

    app.register_blueprint(prediction_bp)
    app.register_blueprint(price_bp)
    app.register_blueprint(auth_bp)
    app.register_blueprint(news_bp)
    
    return app
