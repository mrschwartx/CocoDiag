package com.dicoding.capstone.cocodiag.data.remote.payload

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.File


data class UserResponse(
    @field:SerializedName("id")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("imageProfile")
    val imageProfile: String?,
)

data class PasswordUserResponse(
    @field:SerializedName("id")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("imageProfile")
    val imageProfile: String?,

    @field:SerializedName("password")
    val password: String?,

)


data class UpdateUserParam(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("imageProfile")
    val imageProfile: File?,
)

data class UpdatePasswordParam(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("imageProfile")
    val imageProfile: String?,

    @field:SerializedName("password")
    val newPassword: String
)

