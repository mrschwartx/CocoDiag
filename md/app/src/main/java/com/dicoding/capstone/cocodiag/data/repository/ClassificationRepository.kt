package com.dicoding.capstone.cocodiag.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.data.remote.payload.ClassificationResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.ErrorResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.PredictionErrorResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class ClassificationRepository private constructor(
    private val service: ApiService
) {
    fun predict(imageFile: File) = liveData {
        emit(ResultState.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "imageFile",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = service.predict(multipartBody)
            Log.d("classrepo-predict", "$successResponse")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val predictionErrorResponse =
                Gson().fromJson(errorBody, PredictionErrorResponse::class.java)
            Log.d("classrepo-predict", "$predictionErrorResponse")
            emit(ResultState.Error(ErrorResponse(predictionErrorResponse.message)))
        }
    }

    fun findHistory(userId: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.getHistory(userId)
            Log.d("classification-findHistory", "$response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("classification-findHistory", errorBody.toString())
            emit(ResultState.Error(errorResponse))
        }
    }

    fun saveHistory(userId: String, classification: ClassificationResponse) = liveData {
        emit(ResultState.Loading)
        try {
            service.saveHistory(userId, classification)
            emit(ResultState.Success(Unit))
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