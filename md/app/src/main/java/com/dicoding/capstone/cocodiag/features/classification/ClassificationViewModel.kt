package com.dicoding.capstone.cocodiag.features.classification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.remote.payload.ClassificationResponse
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

class ClassificationViewModel(
    private val repository: ClassificationRepository,
    private val userPref: UserPreference
) : ViewModel() {
    fun predict(file: File) = repository.predict(file)

    fun saveHistory(classification: ClassificationResponse): LiveData<ResultState<Unit>> {
        val userId = getUserId()
        return repository.saveHistory(userId, classification)
    }

    private fun getUserId(): String {
        val userId = runBlocking {
            userPref.getUserId().first()
        }
        return userId ?: ""
    }
}