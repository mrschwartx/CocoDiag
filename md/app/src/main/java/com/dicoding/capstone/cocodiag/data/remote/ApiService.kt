package com.dicoding.capstone.cocodiag.data.remote

import com.dicoding.capstone.cocodiag.data.local.model.ArticleModel
import com.dicoding.capstone.cocodiag.data.remote.payload.ClassificationResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.CommentListResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.CommentRequest
import com.dicoding.capstone.cocodiag.data.remote.payload.CreateUserParam
import com.dicoding.capstone.cocodiag.data.remote.payload.ForumLatestPostResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.ForumMessageResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.ForumPostResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.HistoryListResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.LikePostRequest
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInParam
import com.dicoding.capstone.cocodiag.data.remote.payload.SignInResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.SignUpResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.UpdatePasswordParam
import com.dicoding.capstone.cocodiag.data.remote.payload.UserResponse
import com.dicoding.capstone.cocodiag.features.main.price.PriceItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("signup")
    suspend fun createUser(@Body param: CreateUserParam): SignUpResponse

    @POST("signin")
    suspend fun auth(@Body param: SignInParam): SignInResponse

    @GET("/user/{user_id}")
    suspend fun findUserById(@Path("user_id") userId: String): UserResponse

    @Multipart
    @PUT("/user")
    suspend fun updateUser(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part imageProfile: MultipartBody.Part?
    ): UserResponse

    @PUT("/user")
    suspend fun updatePassword(@Body param: UpdatePasswordParam): UserResponse

    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part imageFile: MultipartBody.Part
    ): ClassificationResponse

    @GET("history/{user_id}")
    suspend fun getHistory(@Path("user_id") userId: String): HistoryListResponse

    @POST("history/{user_id}")
    suspend fun saveHistory(
        @Path("user_id") userId: String,
        @Body history: ClassificationResponse
    ): Response<Unit>

    @GET("getNews")
    suspend fun getNews(): List<ArticleModel>

    @GET("getPrice")
    suspend fun getPrice(): PriceItem

    @GET("forum")
    suspend fun findLatestPost(): ForumLatestPostResponse

    @GET("forum/user/{user_id}")
    suspend fun findPostByUser(@Path("user_id") userId: String): ForumLatestPostResponse

    @Multipart
    @POST("forum")
    suspend fun addPost(
        @Part("post_text") postText: RequestBody,
        @Part postImage: MultipartBody.Part?
    ): ForumPostResponse

    @GET("forum/{post_id}")
    suspend fun findPostById(@Path("post_id") postId: String): ForumPostResponse

    @POST("/forum/like")
    suspend fun likeUnlikePost(@Body param: LikePostRequest): ForumPostResponse

    @POST("/forum/comment")
    suspend fun createComment(@Body param: CommentRequest): CommentListResponse

    @GET("/forum/{post_id}/comments")
    suspend fun getCommentByPostId(@Path("post_id") postId: String): CommentListResponse

    @DELETE("/forum/{post_id}")
    suspend fun deletePostById(@Path("post_id") postId: String): ForumMessageResponse

    @DELETE("/comment/{comment_id}")
    suspend fun deleteCommentById(@Path("comment_id") commentId: String): ForumMessageResponse
}