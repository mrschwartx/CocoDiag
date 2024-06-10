package com.dicoding.capstone.cocodiag.data.remote.payload

import com.google.gson.annotations.SerializedName

// TODO: Adjust with server
data class UpdateUserParam(
    @field:SerializedName("id")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("imageProfile")
    val imageProfile: String,
)

data class UserResponse(
    @field:SerializedName("id")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("imageProfile")
    val imageProfile: String,
)