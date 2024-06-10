package com.dicoding.capstone.cocodiag.data.remote.payload

import com.google.gson.annotations.SerializedName


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

data class UpdateUserParam(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("imageProfile")
    val imageProfile: String,
)

