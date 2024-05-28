package com.dicoding.capstone.cocodiag.features.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.databinding.ActivityEditProfileBinding
import com.dicoding.capstone.cocodiag.databinding.ActivitySettingsBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_edit_profile)
        setBottomNavBar(this@EditProfileActivity, binding.bottomNavigation, R.id.nav_setting)
    }
}