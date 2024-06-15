package com.dicoding.capstone.cocodiag.data.local.model

import android.graphics.Bitmap
import com.dicoding.capstone.cocodiag.data.remote.payload.UserResponse

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val imageProfile: String?,
    val token: String?,
    val isSigned: Boolean
)