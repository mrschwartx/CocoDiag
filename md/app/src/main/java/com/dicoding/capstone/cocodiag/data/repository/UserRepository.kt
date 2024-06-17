package com.dicoding.capstone.cocodiag.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.data.remote.payload.ErrorResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdatePasswordParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdateUserParam
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val service: ApiService
) {
    fun findById(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.findUserById(id)
            Log.d("userrepo-findbyid", "$response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("userrepo-findbyid", errorBody.toString())
            emit(ResultState.Error(errorResponse))
        }
    }

    fun update(param: UpdateUserParam) = liveData {
        emit(ResultState.Loading)
        try {
            if (param.imageProfile != null) {
                val response = service.updateUser(
                    param.name.toRequestBody("text/plain".toMediaType()),
                    param.email.toRequestBody("text/plain".toMediaType()),
                    MultipartBody.Part.createFormData(
                        "imageProfile",
                        param.imageProfile.name,
                        param.imageProfile.asRequestBody("image/jpeg".toMediaType())
                    )
                )
                Log.d("userrepo-update", "$response")

                emit(ResultState.Success(response))
            } else {
                val response = service.updateUser(
                    param.name.toRequestBody("text/plain".toMediaType()),
                    param.email.toRequestBody("text/plain".toMediaType()),
                    null
                )
                Log.d("userrepo-update", "$response")
                emit(ResultState.Success(response))
            }

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("userrepo-update", errorBody.toString())
            emit(ResultState.Error(errorResponse))
        }
    }

    fun updatePassword(param: UpdatePasswordParam) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.updatePassword(param)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("userrepo-updatePassword", errorBody.toString())
            emit(ResultState.Error(errorResponse))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}