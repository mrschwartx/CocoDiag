package com.dicoding.capstone.cocodiag.features.settings.history


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.databinding.ActivityDetailHistoryBinding


class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val label = intent.getStringExtra("LABEL")
        val name = intent.getStringExtra("NAME")
        val controls = intent.getStringExtra("CONTROLS")
        val symptoms = intent.getStringExtra("SYMPTOMS")
        val imgUrl = intent.getStringExtra("IMAGE_URL")

        binding.tvLabel.text = label
        binding.tvName.text = name
        binding.descSymptoms.text = symptoms
        binding.descControls.text = controls

    }
}