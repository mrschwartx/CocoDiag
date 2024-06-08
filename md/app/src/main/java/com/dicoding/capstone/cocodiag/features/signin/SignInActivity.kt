package com.dicoding.capstone.cocodiag.features.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.MainActivity
import com.dicoding.capstone.cocodiag.common.InputValidator
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.dicoding.capstone.cocodiag.databinding.ActivitySignInBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val viewModel by viewModels<SignInViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            if (validateInput(email, password)) {
                val param = SignInParam(
                    email = email,
                    password = password
                )
                signIn(param)
            }
        }

        signUpGoogle()
        navigateToSignUp()
    }

    private fun signIn(param: SignInParam) {
        viewModel.signIn(param).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showToast(this, result.error)
                }

                is ResultState.Success -> {
                    showLoading(false)

                    // DO: after signIn saved current user to preferences
                    val currentUser = UserModel(
                        result.data.name, result.data.email, param.password, true
                    )
                    viewModel.savedUser(currentUser)

                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun signUpGoogle() {
        val btnSignIn = binding.btnGoogle
        btnSignIn.setOnClickListener {
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun navigateToSignUp() {
        val textSignUp = binding.tvSignUp
        textSignUp.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInput(
        email: String,
        password: String
    ): Boolean {
        var isValid = true

        if (!InputValidator.isValidEmail(email)) {
            binding.edEmail.error = "Invalid email address"
            isValid = false
        } else {
            binding.edEmail.error = null
        }

        if (!InputValidator.isValidPassword(password)) {
            binding.edPassword.error =
                "Password must be at least 8 characters and include a number and a special character"
            isValid = false
        } else {
            binding.edPassword.error = null
        }

        return isValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}