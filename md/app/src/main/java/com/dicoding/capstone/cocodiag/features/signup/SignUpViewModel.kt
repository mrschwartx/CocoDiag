package com.dicoding.capstone.cocodiag.features.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.CreateUserParam
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.dicoding.capstone.cocodiag.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: AuthRepository,
    private val pref: UserPreference
): ViewModel() {

    fun signUp(param: CreateUserParam) = repository.signUp(param)

    fun signIn(param: SignInParam) = repository.signIn(param)

    fun savedUser(param: UserModel) {
        viewModelScope.launch {
            pref.saveUser(param)
        }
    }
}