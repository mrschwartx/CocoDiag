package com.dicoding.capstone.cocodiag.features.settings

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.databinding.ActivityEditProfileBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()

    }


    private fun setData() {
        viewModel.findById().observe(this) { result ->
            when(result) {
                is ResultState.Loading -> {
                }

                is ResultState.Error -> {
                    showToast(this, result.error.message)
                }

                is ResultState.Success -> {
                    binding.edName.setText(result.data.name)
                    binding.edEmail.setText(result.data.email)
                }
            }
        }
    }
}