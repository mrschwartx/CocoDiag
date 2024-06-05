package com.dicoding.capstone.cocodiag.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.databinding.ActivitySplashBinding
import com.dicoding.capstone.cocodiag.features.signin.SignInActivity

class SplashActivity : AppCompatActivity() {

    private val duration: Long = 3000
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed(splashRunnable, duration)
    }

    override fun onBackPressed() {
        Handler(Looper.getMainLooper()).removeCallbacks(splashRunnable)
        super.onBackPressed()
    }

    private val splashRunnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}