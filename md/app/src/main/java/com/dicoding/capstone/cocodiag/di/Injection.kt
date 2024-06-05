package com.dicoding.capstone.cocodiag.di

import com.dicoding.capstone.cocodiag.data.remote.ApiConfig
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository

object Injection {
    fun provideRepository(): ClassificationRepository {
        val apiService = ApiConfig.getApiService()
        return ClassificationRepository.getInstance(apiService)
    }
}