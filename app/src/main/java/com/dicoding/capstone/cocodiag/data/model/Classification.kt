package com.dicoding.capstone.cocodiag.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Classification(
    val accuracy: String,

    val control: List<String>,

    val createdAt: Long,

    val label: String,

    val name: String,

    val symptoms: List<String>
) : Parcelable