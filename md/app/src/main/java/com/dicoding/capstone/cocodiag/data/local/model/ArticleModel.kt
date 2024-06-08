package com.dicoding.capstone.cocodiag.data.local.model

import com.google.gson.annotations.SerializedName


data class ArticleModel(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("description") val description: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("source") val source: Source,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String
)

data class Source(
    val id: String?,
    val name: String
)
