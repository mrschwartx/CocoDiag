## API Contract

## Features

### AUTHENTICATION

1. Sign Up
    - Request JSON
    ```json
    {
        "name": "John Doe",
        "email": "jhondoe@gmail.com",
        "password": "passwd123"
    }
    ```
    - Response JSON
    ```json
    # OK
    {
        "id": "user-id",
        "name": "John Doe",
        "email": "jhondoe@gmail.com",  
        "imageProfile": "image-link"
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

2. Sign In
    - Request JSON
    ```json
    {
        "email": "jhondoe@gmail.com",
        "password": "passwd123"
    }
    ```
    - Response JSON
    ```json
    # OK
    {
        "id": "user-id",
        "name": "John Doe",
        "email": "jhondoe@gmail.com",  
        "imageProfile": "image-link",
        "token": "jwt-token"
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```
---

### USER

1. Update User `Header Token`
    - Request JSON
    ```json
    {
        "name": "John Doe",
        "email": "jhondoe@gmail.com",
        "password": "passwd123",
        "imageProfile": "image-link"
    }
    ```
    - Response JSON
    ```json
    # OK
    {
        "id": "user-id",
        "name": "John Doe",
        "email": "jhondoe@gmail.com",  
        "imageProfile": "image-link"
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

2. FindByID `Header Token`
    - Request URL `user/1`
    - Response JSON
    ```json
    # OK
    {
        "id": "user-id",
        "name": "John Doe",
        "email": "jhondoe@gmail.com",  
        "imageProfile": "image-link"
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

---

### CLASSIFICATION

1. Classification Predict
    - Request FORM POST `Header Token`
    ```json
    {
        "imageFile": File,
        "user_id": "user-id"
    }
    ```
    - Response JSON
    ```json
    # OK
    {
        "label": "label",
        "accuracy": "confidence in %",
        "name": "dieases-name",
        "symptoms": [
            "cause 1",
            "cause 2"
        ],
        "controls": [
            "1. step one",
            "2. step two"
        ],
        "created_at": 1759020 // unixtime
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

2. Classification History
    - Request URL `history/1`
    - Response JSON
    ```json
    # OK
    {
        "user_id": "user_id",
        "history": [
            {
                "history_id": "history-id",
                "label": "label",
                "accuracy": "confidence in %",
                "name": "dieases-name",
                "symptoms": [
                    "cause 1",
                    "cause 2"
                ],
                "controls": [
                    "1. step one",
                    "2. step two"
                ],
                "created_at": 1759020 // unixtime
            },
            {
                "history_id": "history-id",
                "label": "label",
                "accuracy": "confidence in %",
                "name": "dieases-name",
                "symptoms": [
                    "cause 1",
                    "cause 2"
                ],
                "controls": [
                    "1. step one",
                    "2. step two"
                ],
                "created_at": 1759020 // unixtime
            }

        ]
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

---

### FORUM - POST

1. Create A Post
    - Request `Header Token`
    ```json
    {
        "user_id": 1,
        "post_text": "text-post", 
        "post_image": "image-post" 
    }
    ```
    - Response JSON
    ```json
    # OK
    {
        "user_id": "user-id",
        "user_image": "user-image",
        "user_name": "user_name",
        "user_email": "user_email",
        "post_id": "post-id",
        "post_text": "text",
        "post_image": "image",      // nullable
        "count_like": 10,
        "count_comment": 11,
        "created_at": 1759020,
        "updated_at": 1759020,
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```


2. Fetch All The Latest Limit 20
    - Request URL `forum&limit=20`
    - Response JSON
    ```json
    # OK
    {
        "forums": [
            {
                "user_id": "user-id",
                "user_image": "user-image",
                "user_name": "user_name",
                "user_email": "user_email",
                "post_id": "post-id",
                "post_text": "text",
                "post_image": "image",      // nullable
                "count_like": 10,
                "count_comment": 11,
                "created_at": 1759020,
                "updated_at": 1759020,
            },
            {
                "user_id": "user-id",
                "user_image": "user-image",
                "user_name": "user_name",
                "user_email": "user_email",
                "post_id": "post-id",
                "post_text": "text",
                "post_image": "image",      // nullable
                "count_like": 10,
                "count_comment": 11,
                "created_at": 1759020,
                "updated_at": 1759020,
            },
        ]
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

3. Fetch By ID Post
    - Request URL `forum/1`
    - Response JSON
    ```json
    # OK
    {
        "user_id": "user-id",
        "user_image": "user-image",
        "user_name": "user_name",
        "user_email": "user_email",
        "post_id": "post_id",
        "post_text": "text",
        "post_image": "image",      // nullable
        "count_like": 10,
        "count_comment": 11,
        "created_at": 1759020,
        "updated_at": 1759020,
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

4. Like or Unlike
    - Request `Header Token`
    ```json
    {
        "post_id": "post_id",
        "like": true,           // false for unlike 
    }
    - Response JSON
    ```json
    # OK
    {
        "user_id": "user-id",
        "user_image": "user-image",
        "user_name": "user_name",
        "user_email": "user_email",
        "post_id": "post-id",
        "post_text": "text",
        "post_image": "image",      // nullable
        "count_like": 10,
        "count_comment": 11,
        "created_at": 1759020,
        "updated_at": 1759020,
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

5. Delete By Post ID
    - Request URL `forum/1`
    - Response JSON
    ```json
    # OK
    {
        "message": "post has been deleted"
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

---

### FORUM - COMMENT

1. Create A Comment
    - Request `Header Token`
    ```json
    {
        "user_id": "user-id",
        "post_id": "post-id", 
        "comment": "text-comment" 
    }
    ```
    - Response JSON
    ```json
    # OK
    {
        "post_id": "post-id",
        "comments": [
            {
                "user_id": "user-id",
                "comment_id": 1,
                "comment": "text-comment",
                "created_at": 1785960,
            },
            {
                "user_id": "user-id",
                "comment_id": 1,
                "comment": "text-comment",
                "created_at": 1785960,
            }
        ]
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```


2. Delete By Comment ID
    - Request URL `comment/1`
    - Response JSON
    ```json
    # OK
    {
        "message": "comment has been deleted"
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

---

### ARTICLE