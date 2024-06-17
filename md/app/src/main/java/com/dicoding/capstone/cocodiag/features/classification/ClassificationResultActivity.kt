package com.dicoding.capstone.cocodiag.features.classification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.getAuthenticatedGlideUrl
import com.dicoding.capstone.cocodiag.common.getListExtra
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.data.remote.payload.ClassificationResponse
import com.dicoding.capstone.cocodiag.databinding.ActivityClassificationResultBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.forum.ForumActivity

class ClassificationResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassificationResultBinding

    private val viewModel by viewModels<ClassificationViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

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
        binding.btnSaveHistory.setOnClickListener {
            saveClassificationResult()
        }
    }

    private fun showResult() {
        val ivImageResult = binding.ivClassImage
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE)
        if (imageUriString != null) {
            val token = viewModel.getUser().token!!
            Glide.with(this)
                .load(getAuthenticatedGlideUrl(imageUriString, token))
                .into(ivImageResult)
        } else {
            showToast(this, "No Image")
        }

        val resultLabel = binding.tvClassLabel
        val labelAndAcc: String =
            intent.getStringExtra(EXTRA_RESULT_LABEL) + " " + intent.getStringExtra(
                EXTRA_RESULT_ACC
            )
        resultLabel.text = labelAndAcc
    }

    private fun showInfo() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_classification_info, null)
        val tvInfo = dialogView.findViewById<TextView>(R.id.tv_info)

        val stringBuilder = StringBuilder()

        val symptoms: List<String>? = intent.getListExtra(EXTRA_RESULT_SYMPTOMS)
        if (!symptoms.isNullOrEmpty()) {
            stringBuilder.append("Symptoms:\n")
            symptoms.forEach { symptom ->
                stringBuilder.append("- $symptom\n")
            }
            stringBuilder.append("\n")
        }

        val controls: List<String>? = intent.getListExtra(EXTRA_RESULT_CONTROL)
        if (!controls.isNullOrEmpty()) {
            stringBuilder.append("Controls:\n")
            controls.forEach { control ->
                stringBuilder.append("- $control\n")
            }
        }

        tvInfo.text = stringBuilder.toString()

        val dialog = AlertDialog.Builder(this)
            .setTitle(intent.getStringExtra(EXTRA_RESULT_NAME))
            .setView(dialogView)
            .create()

        dialog.show()
    }

    private fun setButtonMoved() {
        binding.btnSaveHistory.setOnClickListener {
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

    private fun saveClassificationResult() {
        val classificationResult = getClassificationResultFromIntent()

        viewModel.saveHistory(classificationResult).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Show loading
                    showLoading(true)
                }

                is ResultState.Success<*> -> {
                    // Handle success
                    Toast.makeText(this, "History saved successfully", Toast.LENGTH_SHORT).show()
                }

                is ResultState.Error -> {
                    // Handle error
                    Toast.makeText(
                        this,
                        "Error saving history: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getClassificationResultFromIntent(): ClassificationResponse {
        // Extract classification result data from intent
        // This is just an example, modify it according to your data
        return ClassificationResponse(
            accuracy = intent.getStringExtra("extra_result_acc") ?: "",
            causedBy = intent.getStringExtra("extra_result_caused_by") ?: "",
            control = intent.getStringArrayListExtra("extra_result_controls") ?: emptyList(),
            createdAt = intent.getLongExtra("created_at", 0L),
            label = intent.getStringExtra("extra_result_label") ?: "",
            name = intent.getStringExtra("extra_result_name") ?: "",
            symptoms = intent.getStringArrayListExtra("extra_result_symptoms") ?: emptyList()
        )
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_RESULT_LABEL = "extra_result_label"
        const val EXTRA_RESULT_ACC = "extra_result_acc"
        const val EXTRA_RESULT_NAME = "extra_result_name"
        const val EXTRA_RESULT_CONTROL = "extra_result_controls"
        const val EXTRA_RESULT_SYMPTOMS = "extra_result_symptoms"
        const val EXTRA_RESULT_CAUSED_BY = "extra_result_caused_by"
        private const val TAG = "ClassificationResultActivity"
    }
}