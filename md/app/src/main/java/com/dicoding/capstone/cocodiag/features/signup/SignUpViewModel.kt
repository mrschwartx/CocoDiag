package com.dicoding.capstone.cocodiag.features.signup

import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.CreateUserParam
import com.dicoding.capstone.cocodiag.data.repository.UserRepository

class SignUpViewModel(
    private val repository: UserRepository,
    private val pref: UserPreference
): ViewModel() {

    // TODO: implement on activity
    fun signUp(param: CreateUserParam) {
        repository.signUp(param)
    }

    // TODO: implement on activity
    fun savedUser(param: UserModel) {

    }
}