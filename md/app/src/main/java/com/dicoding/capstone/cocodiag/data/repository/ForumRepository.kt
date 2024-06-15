package com.dicoding.capstone.cocodiag.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.model.PostWithUserDetails
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.data.remote.payload.ErrorResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class ForumRepository private constructor(
    private val service: ApiService
){
    fun getLatestPost() = liveData {
        emit(ResultState.Loading)
        try {
            val latestPostResponse = service.findLatestPost()
            Log.d("forumrepo-getlatest-post", "$latestPostResponse")
            val postWithUserDetails = latestPostResponse.forums.map { res ->
                val userResponse = service.findUserById(res.userId)
                PostWithUserDetails(
                    post = res,
                    user = userResponse
                )
            }
            emit(ResultState.Success(postWithUserDetails))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("forumrepo-getlatest", errorBody.toString())
            emit(ResultState.Error(errorResponse))
        }
    }

    fun addPost(postText: String, postImage: File?) = liveData {
        emit(ResultState.Loading)
        try {
            if (postImage != null) {
                val requestImageFile = postImage.asRequestBody("image/jpeg".toMediaType())
                val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "post_image",
                    postImage.name,
                    requestImageFile
                )
                val requestBody = postText.toRequestBody("text/plain".toMediaType())
                val response = service.addPost(requestBody, multipartBody)
                Log.d("forumrepo-addpost", "$response")
                emit(ResultState.Success(response))
            } else {
                val requestBody = postText.toRequestBody("text/plain".toMediaType())
                val response = service.addPost(requestBody, null)
                Log.d("forumrepo-addpost", "$response")
                emit(ResultState.Success(response))
            }
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