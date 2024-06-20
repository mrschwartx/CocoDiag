package com.dicoding.capstone.cocodiag.features.settings

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.InputValidator
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdateUserParam
import com.dicoding.capstone.cocodiag.databinding.ActivityChangePasswordBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var oldPasswordOri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()

        binding.btnSave.setOnClickListener {
            val oldPassword = binding.edOldPassword.text.toString()
            val newPassword = binding.edNewPassword.text.toString()
            val confirmNewPassword = binding.edConfirmNewPassword.text.toString()

            if (validateCurrentPassword(oldPassword)) {
                if (validateInput(newPassword, confirmNewPassword)) {
                    changePassword(newPassword)
                }
            }
        }
    }

    private fun setData() {
        oldPasswordOri = viewModel.getPasswordFromPreference()
    }

    private fun changePassword(param: String) {
        val currentUser = viewModel.getUser()
        val body = UpdateUserParam(
            name = currentUser.name,
            email = currentUser.email,
            password = param,
            imageProfile = null
        )
        viewModel.updateUser(body).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    setDisableBtnSave(true)
                }

                is ResultState.Error -> {
                    setDisableBtnSave(false)
                    showToast(this, result.error.message)
                }

                is ResultState.Success -> {
                    setDisableBtnSave(false)
                    reSignIn(SignInParam(viewModel.getEmailFromPreference(), param))
                    showToast(this, "Password updated successfully")
                }
            }
        }
    }

    private fun reSignIn(param: SignInParam) {
        viewModel.reSignIn(param).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    setDisableBtnSave(true)
                }

                is ResultState.Error -> {
                    setDisableBtnSave(false)
                    showToast(this, result.error.message)
                }

                is ResultState.Success -> {
                    setDisableBtnSave(false)

                    val currentUser = UserModel(
                        result.data.id,
                        result.data.name,
                        result.data.email,
                        param.password,
                        result.data.imageProfile,
                        result.data.token,
                        true
                    )
                    viewModel.savedUser(currentUser)
                }
            }
        }
    }


    private fun validateCurrentPassword(
        oldPassword: String,
    ): Boolean {
        var isValid = true

        if (!InputValidator.isMatchingPassword(oldPassword, oldPasswordOri)) {
            binding.edOldPassword.error = "Your current password is wrong"
            isValid = false
        } else {
            binding.edOldPassword.error = null
        }

        if (!InputValidator.isValidPassword(oldPassword)) {
            binding.edNewPassword.error =
                "Password must be at least 8 characters and include a number and a special character"
            isValid = false
        } else {
            binding.edNewPassword.error = null
        }

        return isValid
    }

    private fun validateInput(
        newPassword: String,
        confirmNewPassword: String,
    ): Boolean {
        var isValid = true

        if (!InputValidator.isValidPassword(newPassword)) {
            binding.edNewPassword.error =
                "Password must be at least 8 characters and include a number and a special character"
            isValid = false
        } else {
            binding.edNewPassword.error = null
        }

        if (!InputValidator.isMatchingPassword(newPassword, confirmNewPassword)) {
            binding.edConfirmNewPassword.error = "New Passwords do not match"
            isValid = false
        } else {
            binding.edConfirmNewPassword.error = null
        }

        return isValid
    }

    private fun setDisableBtnSave(isDisable: Boolean) {
        if (isDisable) {
            binding.btnSave.text = getString(R.string.save_loading)
            binding.btnSave.isEnabled = false
        } else {
            binding.btnSave.text = getString(R.string.save)
            binding.btnSave.isEnabled = true
        }
    }

    private fun downloadImageToFile(context: Context, imageUrl: String, fileName: String): File? {
        val client = OkHttpClient()
        val token = viewModel.getUser().token

        val request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(imageUrl)
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Failed to download file: $response")

            val file = File(context.cacheDir, fileName)
            val sink = file.sink()
            val bufferedSink = sink.buffer()
            bufferedSink.writeAll(response.body!!.source())
            bufferedSink.close()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}