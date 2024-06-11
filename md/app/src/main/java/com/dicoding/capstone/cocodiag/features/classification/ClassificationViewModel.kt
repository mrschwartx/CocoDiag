package com.dicoding.capstone.cocodiag.features.classification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

class ClassificationViewModel(
    private val repository: ClassificationRepository,
    private val userPref: UserPreference
) : ViewModel() {
    fun predict(file: File) = repository.predict(file)

    private fun getUserId(): String {
        val userId = runBlocking {
            userPref.getUserId().first()
        }
        return userId ?: ""
    }
}