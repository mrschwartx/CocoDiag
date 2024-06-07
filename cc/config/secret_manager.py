import logging
from google.cloud import secretmanager

def access_secret_version(project_id, secret_id, version_id):
    try:
        client = secretmanager.SecretManagerServiceClient()
        name = f"projects/{project_id}/secrets/{secret_id}/versions/{version_id}"
        response = client.access_secret_version(request={"name": name})
        secret = response.payload.data.decode('utf-8')
        
        return secret
    except AttributeError as ae:
        logging.error(f"Attribute error: {ae}")
        raise
    except Exception as e:
        logging.error(f"Error accessing secret version: {e}")
        raise