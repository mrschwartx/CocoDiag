package com.dicoding.capstone.cocodiag.features.settings.history


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.common.getAuthenticatedGlideUrl
import com.dicoding.capstone.cocodiag.databinding.ActivityDetailHistoryBinding


class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        token = intent.getStringExtra("TOKEN") ?: ""
        val label = intent.getStringExtra("LABEL")
        val controls = intent.getStringExtra("CONTROLS")
        val symptoms = intent.getStringExtra("SYMPTOMS")
        val imgUrl = intent.getStringExtra("IMAGE_URL")?:""

        binding.tvLabel.text = "Symptoms of $label"
        binding.descSymptoms.text = symptoms
        binding.descControls.text = controls

        Glide.with(this)
            .load(getAuthenticatedGlideUrl(imgUrl,token))
            .into(binding.imgHistory)
    }
}