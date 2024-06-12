package com.dicoding.capstone.cocodiag.data.remote.payload

import com.google.gson.annotations.SerializedName

data class AddForumParam(
    @field:SerializedName("post")
    val post: String,

    @field:SerializedName("post_image")
    val postImage: String?,
)

data class ForumPostResponse(
    @field:SerializedName("post_id")
    val postId: String,

    @field:SerializedName("post_image")
    val post: String,

    @field:SerializedName("post_image")
    val postImage: String?,

    @field:SerializedName("like_count")
    val likeCount: Long,

    @field:SerializedName("comments")
    val comments: List<CommentResponse>?,

    @field:SerializedName("user_id")
    val userId: String
)

data class CommentResponse(
    @field:SerializedName("comment_id")
    val commentId: String,

    @field:SerializedName("comment")
    val comment: String,

    @field:SerializedName("user_id")
    val userId: String
)