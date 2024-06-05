package com.dicoding.capstone.cocodiag.data.remote.response

import com.google.gson.annotations.SerializedName

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