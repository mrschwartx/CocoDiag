# CocoDiag API

## Overview

A Flask-based application to serve the backend API for the CocoDiag application

## List Of Content

- [Features](#features)
- [Deployment](#deployment)
- [Endpoints](#endpoints)
  - [Base URL](#base-url)
  - [User Authentication](#user-authentication)
    - [Signup](#1-signup)
    - [Signin](#2-signin)
  - [User](#user)
    - [Update User](#1-update-user)
    - [Find User By Id](#2-find-user-by-id)
  - [Prediction](#prediction)
  - [History](#history)
    - [Get Prediction History](#1-get-prediction-history)
    - [Delete History By History Id](#2-delete-history-by-history-id)
    - [Delete All History By User Id](#3-delete-all-history-by-user-id)
  - [Forum](#forum)
    - [Create a Post](#1-create-a-post)
    - [Fetch The Last 20 Posts](#2-fetch-the-last-20-posts)
    - [Fetch Post By Id](#3-fetch-post-by-id)
    - [Fetch Post By User Id](#4-fetch-post-by-user-id)
    - [Delete Post By Id](#5-delete-post-by-id)
    - [Like or Unlike Post](#6-like-or-unlike-post)
    - [Create a Comment](#7-create-a-comment)
    - [Fetch Comment By Post Id](#8-fetch-comment-by-post-id)
    - [Delete Comment By Id](#9-delete-comment-by-id)
  - [Price](#price)
  - [News](#news)
  - [Image](#image)

## Features

- User Authentication: Signup and Signin functionality using Firebase and JWT.
- Update User: Update user data.
- Prediction: Classify input images into four classes of coconut diseases and save the results to firebase.
- History: Get classification results history data from firebase.
- Forum: Create, update, delete, like/unlike posts, and comment on posts.
- Price: Get real-time coconut price data.
- News: Get articles data related to coconut.
- Image: Get images from Firebase.

## Deployment

#### Deploy On Google Cloud Run

1. **Clone Repository**
   ```bash
   https://github.com/AffandraF/cocodiag-api.git
   cd cocodiag-api
   ```
2. **Set up Google Cloud SDK**: Make sure you have the [Google Cloud SDK](https://cloud.google.com/sdk/docs/install) installed and initialized.

   ```bash
   gcloud init
   ```

3. **Build the Docker image**:

   ```bash
   docker build -t gcr.io/[PROJECT-ID]/project-name .
   ```

   Replace `[PROJECT-ID]` with your actual Google Cloud project ID.

4. **Push the Docker image to Google Container Registry**:

   ```bash
   docker push gcr.io/[PROJECT-ID]/project-name
   ```

5. **Deploy to Cloud Run**:
   ```bash
   gcloud run deploy project-name \
       --image gcr.io/[PROJECT-ID]/project-name \
       --platform managed \
       --region [REGION] \
       --allow-unauthenticated
   ```
   Replace `[PROJECT-ID]` with your actual Google Cloud project ID and `[REGION]` with your preferred region (e.g., `asia-southeast2`).

## Endpoints

### Base URL

`https://cocodiag-backend-api-3x4g34y3hq-et.a.run.app`

### User Authentication

#### 1. Signup

- URL: /signup
- Method: 'POST'
- Request Body
  ```json
  {
    "name": "John Doe",
    "email": "jhondoe@gmail.com",
    "password": "passwd123"
  }
  ```
- Response
  ```json
  {
    "id": "user-id",
    "name": "John Doe",
    "email": "jhondoe@gmail.com",
    "imageProfile": "image-link" // default Null
  }
  ```

#### 2. Signin

- URL: /signin
- Method: 'POST'
- Request Body
  ```json
  {
    "email": "jhondoe@gmail.com",
    "password": "passwd123"
  }
  ```
- Response
  ```json
  {
    "id": "user-id",
    "name": "John Doe",
    "email": "jhondoe@gmail.com",
    "imageProfile": "image-link",
    "token": "jwt-token"
  }
  ```

### User

#### 1. Update User

- URL: /user
- Method: 'PUT'
- Header:
  - `Authorization`: Bearer `<token>`
- Request Body: form-data
  - `imageProfile`: File (image file to be uploaded)
  ```json
  {
    "name": "John Doe",
    "email": "jhondoe@gmail.com",
    "password": "passwd123",
    "imageProfile": "image-link"
  }
  ```
- Response
  ```json
  {
    "id": "user-id",
    "name": "John Doe",
    "email": "jhondoe@gmail.com",
    "imageProfile": "image-link"
  }
  ```

#### 2. Find User By Id

- URL: /user/<user_id>
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL `user/<user_id>`
- Response
  ```json
  {
    "id": "user-id",
    "name": "John Doe",
    "email": "jhondoe@gmail.com",
    "imageProfile": "image-link"
  }
  ```

### Prediction

- URL: /predict
- Method: 'POST'
- Header:
  - `Authorization`: Bearer `<token>`
- Request Body: form-data
  - `imageFile`: File (image file to be uploaded)
- Response
  ```json
  {
    "label": "label",
    "accuracy": "confidence in %",
    "name": "diseases-name",
    "symptoms": ["cause 1", "cause 2"],
    "controls": ["1. step one", "2. step two"],
    "created_at": 1759020 // unixtime
  }
  ```

### History

#### 1. Get Prediction History

- URL: /history/<user_id>
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL `history/<user_id>`
- Response
  ```json
  {
    "user_id": "user_id",
    "history": [
      {
        "history_id": "history-id",
        "label": "label",
        "accuracy": "confidence in %",
        "name": "diseases-name",
        "symptoms": ["cause 1", "cause 2"],
        "controls": ["1. step one", "2. step two"],
        "created_at": 1759020 // unixtime
      },
      {
        "history_id": "history-id",
        "label": "label",
        "accuracy": "confidence in %",
        "name": "diseases-name",
        "symptoms": ["cause 1", "cause 2"],
        "controls": ["1. step one", "2. step two"],
        "created_at": 1759020 // unixtime
      }
    ]
  }
  ```

#### 2. Delete History By History Id

- URL: /history/<user_id>/<history_id>
- Method: 'DELETE'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL `history/<user_id>/<history_id>`
- Response
  ```JSON
  {
    "message": "History deleted successfully"
  }
  ```

#### 3. Delete All History By User Id

- URL: /history/<user_id>
- Method: 'DELETE'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL `history/<user_id>`
- Response
  ```JSON
  {
    "message": "All history deleted successfully"
  }
  ```

### Forum

#### 1. Create a Post

- URL: /forum
- Method: 'POST'
- Header:
  - `Authorization`: Bearer `<token>`
- Request Body
  ```json
  {
    "user_id": "user_id",
    "post_text": "text-post",
    "post_image": "image-post"
  }
  ```
- Response
  ```json
  {
    "user_id": "user_id",
    "post_id": "post_id",
    "post_text": "text",
    "post_image": "image_url", // nullable
    "count_like": 0,
    "count_comment": 0,
    "created_at": 1759020,
    "updated_at": 1759020
  }
  ```

#### 2. Fetch The Last 20 Posts

- URL: /forum
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL `forum?limit=20`
- Response
  ```json
      # OK
      {
          "forums": [
              {
                  "user_id": "user_id",
                  "post_id": "post_id",
                  "post_text": "text",
                  "post_image": "image",      // nullable
                  "count_like": 0,
                  "count_comment": 0,
                  "created_at": 1759020,
                  "updated_at": 1759020,
              },
              {
                  "user_id": "user_id",
                  "post_id": "post_id",
                  "post_text": "text",
                  "post_image": "image_url",      // nullable
                  "count_like": 0,
                  "count_comment": 0,
                  "created_at": 1759020,
                  "updated_at": 1759020,
              },
          ]
      }
  ```

#### 3. Fetch Post By Id

- URL: /forum/<post_id>
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL `forum/<post_id>`
- Response
  ```json
  {
    "user_id": "user_id",
    "post_id": "post_id",
    "post_text": "text",
    "post_image": "image_url", // nullable
    "count_like": 0,
    "count_comment": 0,
    "created_at": 1759020,
    "updated_at": 1759020
  }
  ```

#### 4. Fetch Post By User Id

- URL: /forum/user/<user_id>
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL `forum/user/<user_id>`
- Response
  ```json
  {
    "forums": [
      {
        "user_id": "user_id",
        "post_id": "post_id",
        "post_text": "text",
        "post_image": "image", // nullable
        "count_like": 0,
        "count_comment": 0,
        "created_at": 1759020,
        "updated_at": 1759020
      },
      {
        "user_id": "user_id",
        "post_id": "post_id",
        "post_text": "text",
        "post_image": "image_url", // nullable
        "count_like": 0,
        "count_comment": 0,
        "created_at": 1759020,
        "updated_at": 1759020
      }
    ]
  }
  ```

#### 5. Delete Post By Id

- URL: /forum/<post_id>
- Method: 'DELETE'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL `forum/<post_id>`
- Response
  ```JSON
  {
      "message": "post has been deleted"
  }
  ```

#### 6. Like or Unlike Post

- URL: /forum/like
- Method: 'POST'
- Header:
  - `Authorization`: Bearer `<token>`
- Request Body
  ```JSON
  {
    "post_id": "post_id",
    "like": true,           // false for unlike
  }
  ```
- Response
  ```JSON
  {
      "message": "post has been deleted"
  }
  ```

#### 7. Create a Comment

- URL: /forum/comment
- Method: 'POST'
- Header:
  - `Authorization`: Bearer `<token>`
- Request Body
  ```json
  {
    "user_id": "user_id",
    "post_id": "post_id",
    "comment": "text"
  }
  ```
- Response
  ```json
  {
    "post_id": "post_id",
    "comments": [
      {
        "user_id": "user_id",
        "comment_id": 1,
        "comment": "text",
        "created_at": 1785960
      },
      {
        "user_id": "user_id",
        "comment_id": 1,
        "comment": "text",
        "created_at": 1785960
      }
    ]
  }
  ```

#### 8. Fetch Comment By Post Id

- URL: /forum/<post_id>/comments
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL: `/forum/<post_id>/comments`
- Response
  ```json
  {
    "post_id": "post_id",
    "comments": [
      {
        "user_id": "user_id",
        "comment_id": 1,
        "comment": "text",
        "created_at": 1785960
      },
      {
        "user_id": "user_id",
        "comment_id": 1,
        "comment": "text",
        "created_at": 1785960
      }
    ]
  }
  ```

#### 9. Delete Comment By Id

- URL: /comment/<comment_id>
- Method: 'DELETE'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL: `/comment/<comment_id>`
- Response
  ```json
  {
    "message": "comment has been deleted"
  }
  ```

### Price

- URL: /getPrice
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL: `/getPrice`
- Response
  ```json
  {
    "date": "date",
    "price": "coconut_price",
    "region": "DKI Jakarta"
  }
  ```

### News

- URL: /getNews
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL: `/getNews`
- Response
  ```json
  {
    "article": [
      {
        "author": "author_name",
        "content": "text",
        "date": "date",
        "img_url": "image_url",
        "title": "text",
        "url": "url"
      },
      {
        "author": "author_name",
        "content": "text",
        "date": "date",
        "img_url": "image_url",
        "title": "text",
        "url": "url"
      }
    ]
  }
  ```

### Image

- URL: /image/<img_url>
- Method: 'GET'
- Header:
  - `Authorization`: Bearer `<token>`
- Request URL: `/image/<img_url>`
- Response: File `image`

## Author

Team ID: C241-PS469

| ID           | Name                  |
| ------------ | --------------------- |
| C295D4KY1243 | Affandra Fahrezi      |
| C335D4KY0015 | Muhamad Ivan Fadillah |
