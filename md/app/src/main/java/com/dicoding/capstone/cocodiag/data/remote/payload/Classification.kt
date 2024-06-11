package com.dicoding.capstone.cocodiag.data.remote.payload

import com.google.gson.annotations.SerializedName
import java.io.File

data class ClassificationResponse(
    @field:SerializedName("accuracy")
    val accuracy: String,

    @field:SerializedName("caused_by")
    val causedBy: String,

    @field:SerializedName("controls")
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