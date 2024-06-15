package com.dicoding.capstone.cocodiag.features.forum

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.InputValidator
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.convertBitmapToBase64
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.common.uriToFile
import com.dicoding.capstone.cocodiag.databinding.ActivityForumAddBinding
import com.dicoding.capstone.cocodiag.databinding.ActivityForumBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.classification.CameraActivity

class ForumAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForumAddBinding

    private val viewModel by viewModels<ForumViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    private var postImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@ForumAddActivity, binding.bottomNavigation, R.id.nav_forum)

        binding.ivPostImage.visibility = View.GONE
        binding.btnImgUpload.setOnClickListener {
            startGallery()
        }

        binding.btnPost.setOnClickListener {
            val postText = binding.etPostText.text.toString()

            if (validateInput(postText)) {
                addPost(postText)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ForumAddActivity, ForumActivity::class.java))
        finish()
    }

    fun addPost(postText: String) {
        if (postImage != null) {
            postImage?.let { uri ->
                val imageFile = uriToFile(uri, this)
                viewModel.addPost(postText, imageFile).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {}
                        is ResultState.Error -> {}
                        is ResultState.Success -> {
                            val intent = Intent(this, ForumActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                }
            }
        } else {
            viewModel.addPost(postText, null).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {}
                    is ResultState.Error -> {}
                    is ResultState.Success -> {
                        val intent = Intent(this, ForumActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

            }
        }

    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK /* or ACTION_GET_CONTENT */
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            postImage = result.data?.data
            postImage?.let { uri ->
                val imageBitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                } else {
                    val src = ImageDecoder.createSource(this.contentResolver, uri)
                    ImageDecoder.decodeBitmap(src).copy(Bitmap.Config.RGBA_F16, true)
                }
                binding.ivPostImage.setImageBitmap(imageBitmap)
                binding.ivPostImage.visibility = View.VISIBLE
            }
        }
    }

    private fun validateInput(postText: String): Boolean {
        var isValid = true

        if (!InputValidator.isValidName(postText)) {
            binding.etPostText.error = "Post text cannot be empty"
            isValid = false
        }

        return isValid
    }
}