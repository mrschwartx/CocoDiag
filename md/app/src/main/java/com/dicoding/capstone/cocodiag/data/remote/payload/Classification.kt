package com.dicoding.capstone.cocodiag.data.remote.payload

import com.google.gson.annotations.SerializedName


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

data class HistoryListResponse(
    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("history")
    val history: List<HistoryResponse>
)

data class HistoryResponse(
    @field:SerializedName("history_id")
    val historyId: String,

    @field:SerializedName("label")
    val label: String,

    @field:SerializedName("accuracy")
    val accuracy: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("symptoms")
    val symptoms: Any,

    @field:SerializedName("controls")
    val controls: Any,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("image_url")
    val imageUrl: String
)