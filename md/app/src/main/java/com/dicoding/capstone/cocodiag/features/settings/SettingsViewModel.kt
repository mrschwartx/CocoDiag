package com.dicoding.capstone.cocodiag.features.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdateUserParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UserResponse
import com.dicoding.capstone.cocodiag.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val userPref: UserPreference
): ViewModel() {
    fun findById(): LiveData<ResultState<UserResponse>>{
        val userId = getUserId()
        return userRepository.findById(userId)
    }

    fun updatePassword(newPassword: String) = userRepository.updatePassword(newPassword,userPref)

    fun getPasswordFromPreference(): String {
        val password = runBlocking {
            userPref.getPassword().first()
        }
        return password ?: ""
    }
    fun updateUser(param: UpdateUserParam) = userRepository.update(param)



    private fun getUserId(): String {
        val userId = runBlocking {
            userPref.getUserId().first()
        }
        return userId ?: ""
    }


}