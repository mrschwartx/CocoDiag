package com.dicoding.capstone.cocodiag.features.splash

import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashViewModel(
    private val pref: UserPreference
): ViewModel() {

    fun isSignIn(): Boolean {
        val isSignIn = runBlocking {
            pref.getState().first()
        }

        return isSignIn ?: false
    }

}