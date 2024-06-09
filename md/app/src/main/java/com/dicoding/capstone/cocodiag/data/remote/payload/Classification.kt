package com.dicoding.capstone.cocodiag.data.remote.payload

import com.google.gson.annotations.SerializedName
import java.io.File

data class ClassificationParam(
    val imageFile: File,
    val userId: String,
)

data class ClassificationResponse(
    @field:SerializedName("accuracy")
    val accuracy: String,

    @field:SerializedName("control")
    val control: List<String>,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("label")
    val label: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("symptoms")
    val symptoms: List<String>
)