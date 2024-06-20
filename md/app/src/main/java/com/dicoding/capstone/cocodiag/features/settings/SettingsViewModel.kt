package com.dicoding.capstone.cocodiag.features.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.HistoryListResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdateUserParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UserResponse
import com.dicoding.capstone.cocodiag.data.repository.AuthRepository
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import com.dicoding.capstone.cocodiag.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val classRepository: ClassificationRepository,
    private val userPref: UserPreference
) : ViewModel() {
    fun findById(): LiveData<ResultState<UserResponse>> {
        val userId = getUserId()
        return userRepository.findById(userId)
    }

    fun updateUser(param: UpdateUserParam) = userRepository.update(param)

//    fun updatePassword(newPassword: String): LiveData<ResultState<UserResponse>> {
//        val currentUser = getUser()
//        val param = UpdateUserParam(
//            name = currentUser.name,
//            email = currentUser.email,
//            password = newPassword,
//            imageProfile = currentUser.imageProfile,
//        )
//        return userRepository.update(param)
//    }

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

    fun findHistory(): LiveData<ResultState<HistoryListResponse>> {
        val userId = getUserId()
        return classRepository.findHistory(userId)
    }

    private fun getUserId(): String {
        val userId = runBlocking {
            userPref.getUserId().first()
        }
        return userId ?: ""
    }

    fun getUser(): UserModel {
        val user = runBlocking {
            userPref.getUser().first()
        }
        return user
    }

    fun signOut(): Boolean {
        runBlocking {
            userPref.setSignOut()
        }
        val currentUser = getUser()
        if (currentUser.isSigned) return false else return true
    }
}