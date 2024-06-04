package com.dicoding.capstone.cocodiag.features.settings.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}