package com.dicoding.capstone.cocodiag.data.local.model

import com.dicoding.capstone.cocodiag.data.remote.payload.ForumPostResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.UserResponse

data class PostWithUserDetails(
    val post: ForumPostResponse,
    val user: UserResponse
)