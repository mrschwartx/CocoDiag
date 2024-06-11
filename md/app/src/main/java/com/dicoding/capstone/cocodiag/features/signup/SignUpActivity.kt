package com.dicoding.capstone.cocodiag.features.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.MainActivity
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.InputValidator
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.CreateUserParam
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.dicoding.capstone.cocodiag.databinding.ActivitySignUpBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.signin.SignInActivity



class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            val confirmPassword = binding.edConfirmPassword.text.toString()

            if (validateInput(name, email, password, confirmPassword)) {
                val param = CreateUserParam(
                    name = name,
                    email = email,
                    password = password
                )
                signUp(param)
            }
        }

        signUpGoogle()
        navigateToSignIn()
    }

    private fun signUp(param: CreateUserParam) {
        viewModel.signUp(param).observe(this) {result ->
            when(result) {
                is ResultState.Loading -> {
                    setDisableBtnSignUp(true)
                }

                is ResultState.Error -> {
                    setDisableBtnSignUp(false)
                    showToast(this, result.error.message)
                }

                is ResultState.Success -> {
                    setDisableBtnSignUp(false)

                    val validUser = SignInParam(param.email, param.password)
                    autoSignIn(validUser)

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun signUpGoogle() {
        val btnSignIn = binding.btnGoogle
        btnSignIn.setOnClickListener {
            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun autoSignIn(param: SignInParam) {
        viewModel.autoSignIn(param).observe(this) { result ->
            when(result) {
                is ResultState.Loading -> {
                    setDisableBtnSignUp(true)
                }

                is ResultState.Error -> {
                    setDisableBtnSignUp(false)
                    showToast(this, result.error.message)
                }

                is ResultState.Success -> {
                    setDisableBtnSignUp(false)

                    val currentUser = UserModel(
                        result.data.id,
                        result.data.name,
                        result.data.email,
                        param.password,
                        result.data.imageProfile,
                        result.data.token,
                        true
                    )
                    viewModel.savedUser(currentUser)
                }
            }
        }
    }

    private fun navigateToSignIn() {
        val textSignUp = binding.tvSignIn
        textSignUp.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (!InputValidator.isValidName(name)) {
            binding.edName.error = "Name is required"
            isValid = false
        } else {
            binding.edName.error = null
        }

        if (!InputValidator.isValidEmail(email)) {
            binding.edEmail.error = "Invalid email address"
            isValid = false
        } else {
            binding.edEmail.error = null
        }

        if (!InputValidator.isValidPassword(password)) {
            binding.edPassword.error = "Password must be at least 8 characters and include a number and a special character"
            isValid = false
        } else {
            binding.edPassword.error = null
        }

        if (!InputValidator.isMatchingPassword(password, confirmPassword)) {
            binding.edConfirmPassword.error = "Passwords do not match"
            isValid = false
        } else {
            binding.edConfirmPassword.error = null
        }

        return isValid
    }

    private fun setDisableBtnSignUp(isDisable: Boolean) {
        if (isDisable) {
            binding.btnSignUp.text = getString(R.string.sign_up_title_loading)
            binding.btnSignUp.isEnabled = false
        } else {
            binding.btnSignUp.text = getString(R.string.sign_up_title)
            binding.btnSignUp.isEnabled = true
        }
    }
}