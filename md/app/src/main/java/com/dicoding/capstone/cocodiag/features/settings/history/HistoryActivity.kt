package com.dicoding.capstone.cocodiag.features.settings.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}