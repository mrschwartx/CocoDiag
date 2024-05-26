package com.dicoding.capstone.cocodiag.common

import android.content.Context
import android.widget.Toast

// Toast message
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}