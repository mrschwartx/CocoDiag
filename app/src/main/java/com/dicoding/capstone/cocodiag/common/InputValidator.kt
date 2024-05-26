package com.dicoding.capstone.cocodiag.common

import java.util.regex.Pattern

object InputValidator {
    fun isValidName(name: String?): Boolean {
        return name != null && name.trim().isNotEmpty()
    }

    fun isValidEmail(email: String?): Boolean {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[!@#\$%^&*()_+=\\-`~{}\\[\\]:;\"'<>,.?/\\\\]).{8,}$")
        return password != null && pattern.matcher(password).matches()
    }

    fun isMatchingPassword(password: String?, confirmPassword: String?): Boolean {
        return password != null && password == confirmPassword
    }
}