package com.dicoding.capstone.cocodiag.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.data.remote.payload.ErrorResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdatePasswordParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdateUserParam
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class UserRepository private constructor(
    private val service: ApiService
) {
    fun findById(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.findUserById(id)
            Log.d("user-repo", "$response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("userrepo", errorBody.toString())
            emit(ResultState.Error(errorResponse))
        }
    }

    fun update(param: UpdateUserParam) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.updateUser(param)
            Log.d("user-repo", "$response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("userrepo", errorBody.toString())
            emit(ResultState.Error(errorResponse))
        }
    }

    fun updatePassword(newPassword: String, myPref : UserPreference) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.updatePassword(UpdatePasswordParam(myPref.getUser().first().name,
                myPref.getUser().first().email,myPref.getUser().first().imageProfile,newPassword))
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            emit(ErrorResponse("Error"))
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