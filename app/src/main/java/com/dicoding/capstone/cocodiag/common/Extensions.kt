package com.dicoding.capstone.cocodiag.common

import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.MainActivity
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.features.classification.CameraActivity
import com.dicoding.capstone.cocodiag.features.forum.ForumActivity
import com.dicoding.capstone.cocodiag.features.settings.SettingsActivity
import com.google.android.material.navigation.NavigationBarView

fun AppCompatActivity.setBottomNavBar(
    context: Context,
    bottomNavView: NavigationBarView,
    @IdRes itemId: Int,
) {
    bottomNavView.apply {
        selectedItemId = itemId
        setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(context, MainActivity::class.java))
                    true
                }

                R.id.nav_camera -> {
                    startActivity(Intent(context, CameraActivity::class.java))
                    true
                }

                R.id.nav_forum -> {
                    startActivity(Intent(context, ForumActivity::class.java))
                    true
                }

                R.id.nav_setting -> {
                    startActivity(Intent(context, SettingsActivity::class.java))
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}