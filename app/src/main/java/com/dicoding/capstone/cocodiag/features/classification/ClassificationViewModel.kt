package com.dicoding.capstone.cocodiag.features.classification

import androidx.lifecycle.ViewModel
import com.dicoding.capstone.cocodiag.data.repository.ClassificationRepository
import java.io.File

class ClassificationViewModel(private val repository: ClassificationRepository) : ViewModel() {
    fun predict(file: File) = repository.predict(file)
}