package com.dicoding.capstone.cocodiag.features.classification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.remote.payload.ClassificationParam
import com.dicoding.capstone.cocodiag.data.remote.payload.ClassificationResponse
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

class ClassificationViewModel(
    private val repository: ClassificationRepository,
    private val userPref: UserPreference
) : ViewModel() {
    fun predict(file: File): LiveData<ResultState<ClassificationResponse>> {
        val param = ClassificationParam(
            imageFile = file,
            userId = getUserId()
        )
        return repository.predict(param)
    }

    private fun getUserId(): String {
        val userId = runBlocking {
            userPref.getUserId().first()
        }
        return userId ?: ""
    }
}