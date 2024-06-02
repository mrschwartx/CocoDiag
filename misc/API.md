## API Contract

## Features

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
        "id": 1,                        // if use JWT return JWT
        "name": "John Doe",
        "email": "jhondoe@gmail.com",  
        "imageProfile": "image-link"    // nullable
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
        "id": 1,                        // if use JWT return JWT
        "name": "John Doe",
        "email": "jhondoe@gmail.com",  
        "imageProfile": "image-link"
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```

3. Classification Predict
    - Request FORM POST
    ```json
    {
        "imageFile": File,
        "id": 1                         // if use JWT request send with JWT
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

4. Classification History
    - Request URL `history/1`
    - Response JSON
    ```json
    # OK
    {
        "id": 1,                                // if use JWT return JWT
        "history": [
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
            },
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

        ]
    }

    # Fail
    {
        "message": "Cause fail"
    }
    ```
5. Profile Featch By ID or Email
6. Profile Update
7. Profile Change Password
8. Profile Change Image Profile
9. Forum Create A Posting
10. Forum Fetch All Posting
11. Forum Fetch A Posting
12. Forum Create A Like
13. Forum Create A Comment