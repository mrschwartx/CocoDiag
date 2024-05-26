package com.dicoding.capstone.cocodiag.features.classification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.databinding.ActivityClassificationResultBinding

class ClassificationResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassificationResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassificationResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra(EXTRA_IMAGE)
        Glide.with(this)
            .load(imageUri)
            .into(binding.resultImageView)
    }

    companion object {
        const val EXTRA_IMAGE = "extra"
        private const val TAG = "ClassificationResultActivity"
    }
}