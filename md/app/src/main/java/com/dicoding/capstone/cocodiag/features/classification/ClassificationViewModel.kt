package com.dicoding.capstone.cocodiag.features.classification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.ClassificationResponse
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import com.dicoding.capstone.cocodiag.data.repository.ConnectivityRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

class ClassificationViewModel(
    private val repository: ClassificationRepository,
    private val userPref: UserPreference,
    private val connectivity: ConnectivityRepository
) : ViewModel() {

    val isOnline = connectivity.isConnected.asLiveData()

    fun predict(file: File) = repository.predict(file)

    fun saveHistory(classification: ClassificationResponse): LiveData<ResultState<Unit>> {
        val userId = getUserId()
        return repository.saveHistory(userId, classification)
    }

    fun getUser(): UserModel {
        val user = runBlocking {
            userPref.getUser().first()
        }

        return user
    }

    private fun getUserId(): String {
        val userId = runBlocking {
            userPref.getUserId().first()
        }
        return userId ?: ""
    }
}