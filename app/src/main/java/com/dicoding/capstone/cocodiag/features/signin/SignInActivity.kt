package com.dicoding.capstone.cocodiag.features.signin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.MainActivity
import com.dicoding.capstone.cocodiag.databinding.ActivitySignInBinding
import com.dicoding.capstone.cocodiag.features.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUp()
        signUpGoogle()
        navigateToSignUp()
    }

    private fun signUp() {
        val btnSignIn = binding.btnSignIn
        btnSignIn.setOnClickListener {
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
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
}