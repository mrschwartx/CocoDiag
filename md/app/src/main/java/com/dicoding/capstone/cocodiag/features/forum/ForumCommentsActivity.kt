package com.dicoding.capstone.cocodiag.features.forum

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.getAuthenticatedGlideUrl
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.data.remote.payload.CommentRequest
import com.dicoding.capstone.cocodiag.data.remote.payload.ForumPostResponse
import com.dicoding.capstone.cocodiag.data.remote.payload.UserResponse
import com.dicoding.capstone.cocodiag.databinding.ActivityForumCommentsBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForumCommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForumCommentsBinding

    private val viewModel by viewModels<ForumViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@ForumCommentsActivity, binding.bottomNavigation, R.id.nav_forum)
        setForumPost()

        binding.btnPostComment.setOnClickListener {
            val commentText = binding.editTextInput.text.toString()
            if (commentText != "") {
                addComment(commentText)
            }
        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        startActivity(Intent(this@ForumCommentsActivity, ForumActivity::class.java))
//        finish()
//    }

    private fun setForumPost() {
        val postId = intent.getStringExtra(EXTRA_POST_ID)
        if (postId != null) {
            viewModel.findPostById(postId).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val post = result.data
                        setPost(post)
                        viewModel.findUserById(post.userId).observe(this) { resUser ->
                            when (resUser) {
                                is ResultState.Loading -> {}
                                is ResultState.Error -> {}
                                is ResultState.Success -> {
                                    setUser(resUser.data)
                                }
                            }
                        }
                        viewModel.findCommentByPostId(post.postId).observe(this) { resComment ->
                            when (resComment) {
                                is ResultState.Loading -> {}
                                is ResultState.Error -> {}
                                is ResultState.Success -> {
                                    val token = viewModel.getUser().token!!
                                    val rv: RecyclerView = binding.rvComments
                                    val adapter = ForumCommentAdapter(resComment.data, token)
                                    rv.layoutManager = LinearLayoutManager(this)
                                    rv.adapter = adapter
                                }
                            }
                        }
                    }
                }
            }
        } else {
            val moveIntent = Intent(this, ForumActivity::class.java)
            startActivity(moveIntent)
            finish()
        }
    }

    private fun setUser(param: UserResponse) {
        binding.tvForumUserName.text = param.name
        binding.tvForumUserEmail.text = param.email
        val token = viewModel.getUser().token
        if (param.imageProfile != null && token != null) {
            Glide.with(this)
                .load(getAuthenticatedGlideUrl(param.imageProfile, token))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(binding.ivForumUserProfile)
        }
    }

    private fun setPost(param: ForumPostResponse) {
        binding.tvForumPostText.text = param.postText
        val token = viewModel.getUser().token
        if (param.postImage != null && token != null) {
            Glide.with(this)
                .load(getAuthenticatedGlideUrl(param.postImage, token))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(binding.ivForumPostImage)
            binding.ivForumPostImage.visibility = View.VISIBLE
        } else {
            binding.ivForumPostImage.visibility = View.GONE
        }
        binding.tvForumPostLike.text =
            if (param.countLike < 1) "like" else "${param.countLike} likes"
        binding.tvForumPostComment.text =
            if (param.countComment < 1) "comment" else "${param.countComment} comments"
        param.createdAt.let { createdAt ->
            val currentMillis = System.currentTimeMillis()
            val diffMillis = currentMillis - (createdAt * 1000L)

            val seconds = diffMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30

            val timeAgo = when {
                months > 0 -> {
                    val date = Date(createdAt * 1000L)
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    sdf.format(date)
                }

                days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
                hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
                minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
                else -> "$seconds second${if (seconds > 1) "s" else ""} ago"
            }
            binding.tvForumPostCreatedAt.text = timeAgo
        }
    }

    private fun addComment(commentText: String) {
        val postId = intent.getStringExtra(EXTRA_POST_ID)
        if (postId != null) {
            viewModel.createComment(CommentRequest(postId, commentText)).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {}
                    is ResultState.Error -> {}
                    is ResultState.Success -> {
                        setForumPost()
                        hideKeyboard()
                        binding.editTextInput.text?.clear()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = currentFocus
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
        }
    }

    companion object {
        const val EXTRA_POST_ID = "extra_post_id"
        private const val TAG = "ForumCommentsActivity"
    }
}