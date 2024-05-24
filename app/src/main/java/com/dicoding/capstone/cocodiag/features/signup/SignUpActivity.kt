package com.dicoding.capstone.cocodiag.features.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.MainActivity
import com.dicoding.capstone.cocodiag.databinding.ActivitySignUpBinding
import com.dicoding.capstone.cocodiag.features.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signIn()
        signInGoogle()
        navigateToSignIn()
    }

    private fun signIn() {
        val btnSignIn = binding.btnSignUp
        btnSignIn.setOnClickListener {
            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signInGoogle() {
        val btnSignIn = binding.btnGoogle
        btnSignIn.setOnClickListener {
            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
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
}