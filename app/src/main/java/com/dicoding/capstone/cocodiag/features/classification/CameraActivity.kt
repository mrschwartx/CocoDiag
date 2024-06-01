package com.dicoding.capstone.cocodiag.features.classification

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.common.uriToFile
import com.dicoding.capstone.cocodiag.data.remote.response.ClassificationResponse
import com.dicoding.capstone.cocodiag.databinding.ActivityCameraBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageBitmap: Bitmap
    private lateinit var cameraExecutor: ExecutorService

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var imageUrl: String? = null
    private var savedUri: Uri? = null

    private val viewModel by viewModels<ClassificationViewModel> {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@CameraActivity, binding.bottomNavigation, R.id.nav_camera)

        // Use Image
        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        // Use Camera
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        } else {
            startCamera()
        }
        binding.btnCapture.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            takePhoto()
            showToast(this, "Camera captured")
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            val imageSize = Size(224, 224)
            imageCapture = ImageCapture.Builder().setTargetResolution(imageSize).build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e(TAG, "CameraX bind failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoSavedDir = getOutputPhotoDirectory()
        val timeStamp = getTimestamp()
        val photoFile = File(photoSavedDir, "$timeStamp.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Camera capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    val message = "Photo saved as: $savedUri"
                    showToast(baseContext, message)
                    Log.d(TAG, "$savedUri")

                    val intent = Intent()
                    intent.putExtra(EXTRA_IMAGE_URI, savedUri.toString())
                    setResult(Activity.RESULT_OK, intent)

                    imageUrl = savedUri.toString()
                    val imageUri = Uri.parse(imageUrl)
                    imageBitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(
                            this@CameraActivity.contentResolver,
                            imageUri
                        )
                    } else {
                        val src =
                            ImageDecoder.createSource(this@CameraActivity.contentResolver, imageUri)
                        ImageDecoder.decodeBitmap(src).copy(Bitmap.Config.RGBA_F16, true)
                    }

                    predict()
                }
            })
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_PICK /* or ACTION_GET_CONTENT */
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            savedUri = result.data?.data as Uri
            imageUrl = savedUri.toString()
            val imageUri = Uri.parse(imageUrl)

            imageBitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(this@CameraActivity.contentResolver, imageUri)
            } else {
                val src = ImageDecoder.createSource(this@CameraActivity.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(src).copy(Bitmap.Config.RGBA_F16, true)
            }

            predict()
        }
    }

    private fun predict() {
        savedUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            viewModel.predict(imageFile).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            moveToResult(result.data)
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showToast(this, result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: showToast(this, "no image selected")
    }

    private fun moveToResult(result: ClassificationResponse) {
        Log.d("RESULT", "$result")
        val moveIntent = Intent(this@CameraActivity, ClassificationResultActivity::class.java)
        moveIntent.putExtra(ClassificationResultActivity.EXTRA_IMAGE, imageUrl)
        moveIntent.putExtra(ClassificationResultActivity.EXTRA_RESULT_LABEL, result.label)
        moveIntent.putExtra(ClassificationResultActivity.EXTRA_RESULT_ACC, result.accuracy)
        startActivity(moveIntent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture?.targetRotation = rotation
            }
        }
    }

    private fun getOutputPhotoDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun getTimestamp(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_img"
        private const val TAG = "CameraActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}