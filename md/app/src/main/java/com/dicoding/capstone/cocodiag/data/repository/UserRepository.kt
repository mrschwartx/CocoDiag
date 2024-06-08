package com.dicoding.capstone.cocodiag.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.data.remote.payload.CreateUserParam
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam

class UserRepository private constructor(
    private val service: ApiService
) {
    fun signUp(param: CreateUserParam) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.createUser(param)
            Log.d("user-repo", "$response")
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "an error occured"))
        }
    }

    fun signIn(param: SignInParam) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.auth(param)
            Log.d("user-repo-signin", "$response")
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "an error occured"))
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