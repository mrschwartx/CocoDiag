package com.dicoding.capstone.cocodiag.features.splash


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashViewModel(
    private val pref: UserPreference
) : ViewModel() {

    private val _currentIndex = MutableLiveData<Int>()
    val currentIndex: LiveData<Int>
        get() = _currentIndex

    init {
        _currentIndex.value = 0
    }

    fun next() {
        _currentIndex.value = (_currentIndex.value ?: 0) + 1
    }

    fun isSignIn(): Boolean {
        val isSignIn = runBlocking {
            pref.getState().first()
        }

        return isSignIn ?: false
    }

}