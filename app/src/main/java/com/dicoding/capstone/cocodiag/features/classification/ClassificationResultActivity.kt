package com.dicoding.capstone.cocodiag.features.classification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.databinding.ActivityClassificationResultBinding
import com.dicoding.capstone.cocodiag.features.forum.ForumActivity

class ClassificationResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassificationResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassificationResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(
            this@ClassificationResultActivity,
            binding.bottomNavigation,
            R.id.nav_camera
        )
        setButtonMoved()

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

    private fun setButtonMoved() {
        binding.btnShare.setOnClickListener {
            val moveIntent = Intent(this@ClassificationResultActivity, ForumActivity::class.java)
            startActivity(moveIntent)
            finish()
        }
        binding.btnRetake.setOnClickListener {
            val moveIntent = Intent(this@ClassificationResultActivity, CameraActivity::class.java)
            startActivity(moveIntent)
            finish()
        }
        binding.btnNext.setOnClickListener {
            val moveIntent =
                Intent(this@ClassificationResultActivity, CareInstructionActivity::class.java)
            startActivity(moveIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ClassificationResultActivity, CameraActivity::class.java))
        finish()
    }

    companion object {
        const val EXTRA_IMAGE = "extra"
        private const val TAG = "ClassificationResultActivity"
    }
}