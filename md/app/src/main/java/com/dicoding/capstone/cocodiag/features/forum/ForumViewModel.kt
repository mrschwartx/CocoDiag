package com.dicoding.capstone.cocodiag.features.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.local.model.PostWithUserDetails
import com.dicoding.capstone.cocodiag.data.remote.payload.UserResponse
import com.dicoding.capstone.cocodiag.data.repository.ForumRepository
import com.dicoding.capstone.cocodiag.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ForumViewModel(
    private val userRepository: UserRepository,
    private val forumRepository: ForumRepository,
    private val userPref: UserPreference
): ViewModel() {

    fun findLatestPost(): LiveData<ResultState<List<PostWithUserDetails>>> {
        return forumRepository.getLatestPost()
    }

    fun findById(): LiveData<ResultState<UserResponse>> {
        val userId = getUserId()
        return userRepository.findById(userId)
    }

    private fun getUserId(): String {
        val userId = runBlocking {
            userPref.getUserId().first()
        }
        return userId ?: ""
    }
}