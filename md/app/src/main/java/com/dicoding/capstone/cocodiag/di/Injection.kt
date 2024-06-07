package com.dicoding.capstone.cocodiag.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.remote.ApiConfig
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import com.dicoding.capstone.cocodiag.data.repository.UserRepository

object Injection {
    fun provideClassificationRepository(): ClassificationRepository {
        val apiService = ApiConfig.getApiService()
        return ClassificationRepository.getInstance(apiService)
    }

    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }

    fun provideUserPreference(dataStore: DataStore<Preferences>): UserPreference {
        return UserPreference.getInstance(dataStore)
    }
}