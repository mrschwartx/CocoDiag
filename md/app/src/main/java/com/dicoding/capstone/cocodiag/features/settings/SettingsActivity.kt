package com.dicoding.capstone.cocodiag.features.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.databinding.ActivitySettingsBinding
import com.dicoding.capstone.cocodiag.features.settings.history.HistoryActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@SettingsActivity, binding.bottomNavigation, R.id.nav_setting)
        binding.tvEditProfile.setOnClickListener {
            val intent = Intent(this,EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.tvChangePass.setOnClickListener {
            val intent=Intent(this,ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.tvHistory.setOnClickListener {
            val intent = Intent(this@SettingsActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.tvAbout.setOnClickListener {
            val intent=Intent(this,AboutActivity::class.java)
            startActivity(intent)
        }
    }
}