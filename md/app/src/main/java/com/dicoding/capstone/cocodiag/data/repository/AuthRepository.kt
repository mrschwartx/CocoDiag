package com.dicoding.capstone.cocodiag.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.data.remote.payload.CreateUserParam
import com.dicoding.capstone.cocodiag.data.remote.payload.ErrorResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.google.gson.Gson
import retrofit2.HttpException

class AuthRepository private constructor(
    private val service: ApiService
) {
    fun signUp(param: CreateUserParam) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.createUser(param)
            Log.d("user-repo", "$response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse))
        }
    }

    fun signIn(param: SignInParam) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.auth(param)
            Log.d("user-repo-signin", "$response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse))
        }
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}