package com.dicoding.capstone.cocodiag.features

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.repository.AuthRepository
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import com.dicoding.capstone.cocodiag.dataStore
import com.dicoding.capstone.cocodiag.di.Injection
import com.dicoding.capstone.cocodiag.features.classification.ClassificationViewModel
import com.dicoding.capstone.cocodiag.features.signin.SignInViewModel
import com.dicoding.capstone.cocodiag.features.signup.SignUpViewModel

class ViewModelFactory(
    private val userRepo: AuthRepository,
    private val classRepo: ClassificationRepository,
    private val userPref: UserPreference
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(userRepo, userPref) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(userRepo, userPref) as T
            }
            modelClass.isAssignableFrom(ClassificationViewModel::class.java) -> {
                ClassificationViewModel(classRepo, userPref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideAuthRepository(),
                    Injection.provideClassificationRepository(context.dataStore),
                    Injection.provideUserPreference(context.dataStore),
                )
            }.also { instance = it }
    }
}