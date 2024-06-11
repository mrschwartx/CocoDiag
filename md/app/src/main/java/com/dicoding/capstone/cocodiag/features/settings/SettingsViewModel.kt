package com.dicoding.capstone.cocodiag.features.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdatePasswordParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdateUserParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UserResponse
import com.dicoding.capstone.cocodiag.data.repository.AuthRepository
import com.dicoding.capstone.cocodiag.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userPref: UserPreference
): ViewModel() {
    fun findById(): LiveData<ResultState<UserResponse>> {
        val userId = getUserId()
        return userRepository.findById(userId)
    }

    fun updateUser(param: UpdateUserParam) = userRepository.update(param)



    fun updatePassword(newPassword: String): LiveData<ResultState<UserResponse>> {
        val currentUser = getUser()
        val param = UpdatePasswordParam(
            name = currentUser.name,
            email = currentUser.email,
            imageProfile = currentUser.imageProfile,
            newPassword = newPassword,
        )
        return userRepository.updatePassword(param)
    }

    fun reSignIn(param: SignInParam) = authRepository.signIn(param)

    fun savedUser(param: UserModel) {
        viewModelScope.launch {
            userPref.saveUser(param)
        }
    }

    fun getPasswordFromPreference(): String {
        val password = runBlocking {
            userPref.getPassword().first()
        }
        return password ?: ""
    }

    fun getEmailFromPreference(): String {
        val email = runBlocking {
            userPref.getEmail().first()
        }
        return email ?: ""
    }

    private fun getUserId(): String {
        val userId = runBlocking {
            userPref.getUserId().first()
        }
        return userId ?: ""
    }

    private fun getUser(): UserModel {
        val user = runBlocking {
            userPref.getUser().first()
        }
        return user
    }
}