package com.dicoding.capstone.cocodiag.features.forum

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.databinding.ActivityForumAddBinding
import com.dicoding.capstone.cocodiag.databinding.ActivityForumBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.classification.CameraActivity

class ForumAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForumAddBinding

    private val viewModel by viewModels<ForumViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@ForumAddActivity, binding.bottomNavigation, R.id.nav_forum)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ForumAddActivity, ForumActivity::class.java))
        finish()
    }
}