package com.dicoding.capstone.cocodiag.features.settings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.common.InputValidator
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.databinding.ActivityChangePasswordBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChangePasswordBinding

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChangePasswordBinding.inflate(layoutInflater)

        val newpass=binding.edNewPassword.text.toString()
        val confpass=binding.edConfirmNewPassword.text.toString()
        setContentView(binding.root)
        binding.btnSave.setOnClickListener {
            validPassword(newpass,confpass)
            changePassword()
        }

    }

    private fun validPassword(newPass : String, confPass : String) : Boolean{
        var isValid=true
        if (!InputValidator.isValidPassword(newPass)) {
            binding.edNewPassword.error =
                "Password must be at least 8 characters and include a number and a special character"
            isValid = false
        }else if(!InputValidator.isValidPassword(confPass)){
            binding.edConfirmNewPassword.error =
                "Password must be at least 8 characters and include a number and a special character"
            isValid = false
        }else{
            isValid=true
        }
        return isValid
    }

    private fun changePassword() {
        val oldPassword = binding.edOldPassword.text.toString()
        val newPassword = binding.edConfirmNewPassword.text.toString()

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate old password
        val savedPassword = viewModel.getPasswordFromPreference()
        if (savedPassword != oldPassword || binding.edNewPassword.text.toString() == binding.edConfirmNewPassword.text.toString()) {
            Toast.makeText(this, "Old password is incorrect or your new pass not match", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updatePassword(newPassword)
            .observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        // Show loading indicator
                    }

                    is ResultState.Success<*> -> {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                    is ResultState.Error -> {
                        Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}