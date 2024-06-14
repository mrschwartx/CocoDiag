package com.dicoding.capstone.cocodiag.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.remote.ApiConfig
import com.dicoding.capstone.cocodiag.data.repository.AuthRepository
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import com.dicoding.capstone.cocodiag.data.repository.ConnectivityRepository
import com.dicoding.capstone.cocodiag.data.repository.ForumRepository
import com.dicoding.capstone.cocodiag.data.repository.UserRepository

object Injection {
    fun provideAuthRepository(): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService)
    }

    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
        val userPreference = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService(userPreference)
        return UserRepository.getInstance(apiService)
    }

    fun provideClassificationRepository(dataStore: DataStore<Preferences>): ClassificationRepository {
        val userPreference = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService(userPreference)
        return ClassificationRepository.getInstance(apiService)
    }

    fun provideForumRepository(dataStore: DataStore<Preferences>): ForumRepository {
        val userPreference = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService(userPreference)
        return ForumRepository.getInstance(apiService)
    }

    fun provideUserPreference(dataStore: DataStore<Preferences>): UserPreference {
        return UserPreference.getInstance(dataStore)
    }

    fun provideConnectivityRepository(context: Context): ConnectivityRepository {
        return ConnectivityRepository(context)
    }
}
