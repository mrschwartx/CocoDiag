package com.dicoding.capstone.cocodiag.features.classification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.getListExtra
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

        showResult()
    }

    private fun showResult() {
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

        val resultLabel = binding.tvClassLabel
        val labelAndAcc: String =
            intent.getStringExtra(EXTRA_RESULT_LABEL) + " " + intent.getStringExtra(
                EXTRA_RESULT_LABEL
            )
        resultLabel.text = labelAndAcc
    }

    private fun showInfo() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(intent.getStringExtra(EXTRA_RESULT_NAME))
        val stringBuilder = StringBuilder()
        stringBuilder.append("Symptoms:\n")
        val symptoms: List<String>? = intent.getListExtra(EXTRA_RESULT_SYMPTOMS)
        symptoms?.forEach { symptom ->
            stringBuilder.append("- $symptom\n")
        }
        stringBuilder.append("Controls:\n")
        val controls: List<String>? = intent.getListExtra(EXTRA_RESULT_CONTROL)
        controls?.forEach { control ->
            stringBuilder.append(" $control\n")
        }
        dialog.setMessage(stringBuilder)
        dialog.show()
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
        binding.btnInfo.setOnClickListener {
            showInfo()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ClassificationResultActivity, CameraActivity::class.java))
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_RESULT_LABEL = "extra_result_label"
        const val EXTRA_RESULT_ACC = "extra_result_acc"
        const val EXTRA_RESULT_NAME = "extra_result_name"
        const val EXTRA_RESULT_CONTROL = "extra_result_controls"
        const val EXTRA_RESULT_SYMPTOMS = "extra_result_symptoms"
        private const val TAG = "ClassificationResultActivity"
    }
}