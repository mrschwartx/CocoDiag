package com.dicoding.capstone.cocodiag.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.data.remote.payload.AddForumParam
import com.dicoding.capstone.cocodiag.data.remote.payload.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

class ForumRepository private constructor(
    private val service: ApiService
){
    fun addPost(param: AddForumParam) = liveData {
        emit(ResultState.Loading)
        try {
            val response = service.addPost(param)
            Log.d("forumrepo-addpost", "$response")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("forumrepo-addpost", errorBody.toString())
            emit(ResultState.Error(errorResponse))
        }
    }

    companion object {
        @Volatile
        private var instance: ForumRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: ForumRepository(apiService)
            }.also { instance = it }
    }
}