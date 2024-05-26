package com.dicoding.capstone.cocodiag.features.classification

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.databinding.ActivityClassificationResultBinding

class ClassificationResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassificationResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassificationResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showImage()
    }

    private fun showImage() {
        val ivImageResult = binding.ivClassImage
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            Glide.with(this)
                .load(imageUri)
                .into(ivImageResult)
        } else {
            showToast(this, "No Image")
        }
    }

    companion object {
        const val EXTRA_IMAGE = "extra"
        private const val TAG = "ClassificationResultActivity"
    }
}