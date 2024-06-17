package com.dicoding.capstone.cocodiag.features.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.getAuthenticatedGlideUrl
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.databinding.ActivitySettingsBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.settings.history.HistoryActivity
import com.dicoding.capstone.cocodiag.features.signin.SignInActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@SettingsActivity, binding.bottomNavigation, R.id.nav_setting)
        binding.tvEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.tvChangePass.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.tvHistory.setOnClickListener {
            val intent = Intent(this@SettingsActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.tvAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.tvSignout.setOnClickListener {
            if (viewModel.signOut()) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }

        val currentUser = viewModel.getUser()
        binding.tvProfileName.text = currentUser.name
        if (currentUser.imageProfile != null && currentUser.token != null) {
            Glide.with(this)
                .load(getAuthenticatedGlideUrl(currentUser.imageProfile, currentUser.token))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(binding.ivProfileImage)
        }

    }
}