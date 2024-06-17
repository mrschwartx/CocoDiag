package com.dicoding.capstone.cocodiag.features.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.dicoding.capstone.cocodiag.data.repository.AuthRepository
import com.dicoding.capstone.cocodiag.data.repository.ConnectivityRepository
import kotlinx.coroutines.launch

class SignInViewModel(
    private val repository: AuthRepository,
    private val pref: UserPreference,
    private val connectivity: ConnectivityRepository
) : ViewModel() {

    val isOnline = connectivity.isConnected.asLiveData()

    fun signIn(param: SignInParam) = repository.signIn(param)

    fun savedUser(param: UserModel) {
        viewModelScope.launch {
            pref.saveUser(param)
        }
    }
}