package com.dicoding.capstone.cocodiag.features

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import com.dicoding.capstone.cocodiag.data.repository.UserRepository
import com.dicoding.capstone.cocodiag.dataStore
import com.dicoding.capstone.cocodiag.di.Injection
import com.dicoding.capstone.cocodiag.features.classification.ClassificationViewModel
import com.dicoding.capstone.cocodiag.features.signin.SignInViewModel
import com.dicoding.capstone.cocodiag.features.signup.SignUpViewModel

class ViewModelFactory(
    private val userRepo: UserRepository,
    private val userPref: UserPreference,
    private val classRepo: ClassificationRepository
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
                ClassificationViewModel(classRepo) as T
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
                    Injection.provideUserRepository(),
                    Injection.provideUserPreference(context.dataStore),
                    Injection.provideClassificationRepository(),
                )
            }.also { instance = it }
    }
}