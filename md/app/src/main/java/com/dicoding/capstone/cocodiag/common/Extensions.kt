package com.dicoding.capstone.cocodiag.common

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.features.classification.CameraActivity
import com.dicoding.capstone.cocodiag.features.forum.ForumActivity
import com.dicoding.capstone.cocodiag.features.main.MainActivity
import com.dicoding.capstone.cocodiag.features.settings.SettingsActivity
import com.dicoding.capstone.cocodiag.features.settings.about.Member
import com.google.android.material.navigation.NavigationBarView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

// AppCompatActivity
fun AppCompatActivity.setBottomNavBar(
    context: Context,
    bottomNavView: NavigationBarView,
    @IdRes itemId: Int,
) {
    bottomNavView.apply {
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this@setBottomNavBar
        )

        selectedItemId = itemId
        setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(context, MainActivity::class.java), options.toBundle())
                    true
                }

                R.id.nav_camera -> {
                    startActivity(Intent(context, CameraActivity::class.java), options.toBundle())
                    true
                }

                R.id.nav_forum -> {
                    startActivity(Intent(context, ForumActivity::class.java), options.toBundle())
                    true
                }

                R.id.nav_setting -> {
                    startActivity(Intent(context, SettingsActivity::class.java), options.toBundle())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}

val List<Member>.defaultMember:List<Member>
    get()= listOf(
        Member("Putri Nur Fadillah","Politeknik Negeri Ketapang","ML",R.drawable.putry),
        Member("Reyana Hilda Arti","Universitas Tanjungpura","ML",R.drawable.reyana),
        Member("Silva Octaviacantika","Universitas Yarsi","ML",R.drawable.silpa),
        Member("Muhamad Ivan Fadillah","Universitas Yarsi","CC",R.drawable.ipan2),
        Member("Affandra Fahrezi","Universitas Pembangunan Nasional Veteran Jakarta","CC",R.drawable.affandra),
        Member("Muhammad Sutan Baihaqi","Universitas Yarsi","MD",R.drawable.sutan),
        Member("Agus Syahril Mubarok","Universitas Islam Al-Ihya Kuningan","MD",R.drawable.aguss)
    )

// Extension function to add list to bundle
fun <T> Intent.putExtraList(key: String, list: List<T>) {
    val arrayList = ArrayList<T>(list)
    val bundle = Bundle()
    bundle.putSerializable(key, arrayList)
    putExtra(key, bundle)
}

// Extension function to retrieve list from bundle
fun <T> Intent.getListExtra(key: String): List<T>? {
    val bundle = getBundleExtra(key) ?: return null
    @Suppress("UNCHECKED_CAST")
    return bundle.getSerializable(key) as? List<T>
}

// File
fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 3000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

// Bitmap

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}