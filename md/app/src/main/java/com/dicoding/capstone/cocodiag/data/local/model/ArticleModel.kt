package com.dicoding.capstone.cocodiag.data.local.model

import com.google.gson.annotations.SerializedName


data class ArticleModel(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("date") val date: String,
    @SerializedName("img_url") val imageUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
)

data class Source(
    val id: String?,
    val name: String
)
