package com.dicoding.capstone.cocodiag.data.remote.payload

import com.dicoding.capstone.cocodiag.data.local.model.ArticleModel
import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @field:SerializedName("articles")
    val articles: List<ArticleModel>
)