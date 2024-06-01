package com.dicoding.capstone.cocodiag.common

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.dicoding.capstone.cocodiag.MainActivity
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.features.article.Article
import com.dicoding.capstone.cocodiag.features.classification.CameraActivity
import com.dicoding.capstone.cocodiag.features.forum.ForumActivity
import com.dicoding.capstone.cocodiag.features.settings.SettingsActivity
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


fun getSampleArticles(): List<Article> {
    return listOf(
        Article("Artikel 1", "https://lindungihutan.com/blog/pohon-kelapa/"),
        Article("Artikel 2", "https://ditjenbun.pertanian.go.id/mengenal-lebih-dekat-beberapa-varietas-tanaman-kelapa-genjah-di-indonesia/"),
        Article("Artikel 3", "https://id.wikipedia.org/wiki/Kelapa"),
        Article("Artikel 1", "https://lindungihutan.com/blog/pohon-kelapa/"),
        Article("Artikel 2", "https://ditjenbun.pertanian.go.id/mengenal-lebih-dekat-beberapa-varietas-tanaman-kelapa-genjah-di-indonesia/"),
        Article("Artikel 3", "https://id.wikipedia.org/wiki/Kelapa")
    )
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