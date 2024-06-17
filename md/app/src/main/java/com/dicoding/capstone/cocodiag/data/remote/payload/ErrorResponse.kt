package com.dicoding.capstone.cocodiag.data.remote.payload

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @field:SerializedName("message")
    val message: String
)

data class PredictionErrorResponse(
    @field:SerializedName("error")
    val message: String
)