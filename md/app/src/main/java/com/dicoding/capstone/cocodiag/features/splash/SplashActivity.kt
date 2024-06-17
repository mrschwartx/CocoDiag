package com.dicoding.capstone.cocodiag.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.capstone.cocodiag.databinding.ActivitySplashBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.main.MainActivity
import com.dicoding.capstone.cocodiag.features.signin.SignInActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewPager: ViewPager2

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    private val lastPageDelay: Long = 3000
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (viewModel.isSignIn()) {
            navigateToMainActivity()
        } else {
            setupSplashScreen()
        }
    }

    private fun setupSplashScreen() {
        viewPager = binding.viewPager
        val adapter = SplashPagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == adapter.itemCount - 1) {
                    isLastPage = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (isLastPage) {
                            navigateToNextActivity()
                        }
                    }, lastPageDelay)
                } else {
                    isLastPage = false
                }
            }
        })
    }

    private fun navigateToNextActivity() {
        if (!isFinishing) {
            if (viewModel.isSignIn()) {
                navigateToMainActivity()
            } else {
                val intent = Intent(this@SplashActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}




