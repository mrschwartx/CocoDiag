package com.dicoding.capstone.cocodiag.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.data.remote.payload.ClassificationParam
import com.dicoding.capstone.cocodiag.data.remote.payload.ErrorResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class ClassificationRepository private constructor(
    private val service: ApiService
) {
    fun predict(param: ClassificationParam) = liveData {
        emit(ResultState.Loading)
        val requestImageFile = param.imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "imageFile",
            param.imageFile.name,
            requestImageFile
        )
        val requestBody = param.userId.toRequestBody("text/plain".toMediaType())
        try {
            val successResponse = service.predict(multipartBody, requestBody)
            Log.d("repository response", "$successResponse")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse))
        }
    }


    companion object {
        @Volatile
        private var instance: ClassificationRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: ClassificationRepository(apiService)
            }.also { instance = it }
    }
}