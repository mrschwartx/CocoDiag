package com.dicoding.capstone.cocodiag.data.remote.payload

import com.google.gson.annotations.SerializedName

data class ForumLatestPostResponse(
    @field:SerializedName("forums")
    val forums: List<ForumPostResponse>
)

data class ForumPostResponse(
    @field:SerializedName("post_id")
    val postId: String,

    @field:SerializedName("post_text")
    val postText: String,

    @field:SerializedName("post_image")
    val postImage: String?,

    @field:SerializedName("count_like")
    var countLike: Long,

    @field:SerializedName("count_comment")
    val countComment: Long,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("updated_at")
    val updatedAt: Long,

//    @field:SerializedName("comments")
//    val comments: List<CommentResponse>?,

    @field:SerializedName("user_id")
    val userId: String
)

data class LikePostRequest(
    @field:SerializedName("post_id")
    val postId: String,

    @field:SerializedName("like")
    val like: Boolean
)

data class CommentRequest(
    @field:SerializedName("post_id")
    val postId: String,

    @field:SerializedName("comment")
    val comment: String,
)

data class CommentListResponse(
    @field:SerializedName("post_id")
    val postId: String,

    @field:SerializedName("comments")
    val comments: List<CommentResponse>,
)


data class CommentResponse(
    @field:SerializedName("comment_id")
    val commentId: String,

    @field:SerializedName("comment")
    val comment: String,

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("created_at")
    val createdAt: String
)