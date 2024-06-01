package com.dicoding.capstone.cocodiag.data.remote

import com.dicoding.capstone.cocodiag.data.remote.response.ClassificationResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part fileImage: MultipartBody.Part
    ): ClassificationResponse
}