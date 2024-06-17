package com.dicoding.capstone.cocodiag.features.forum

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.dicoding.capstone.cocodiag.data.remote.payload.LikePostRequest
import com.dicoding.capstone.cocodiag.databinding.ActivityForumUserBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory

class ForumUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForumUserBinding

    private val viewModel by viewModels<ForumViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@ForumUserActivity, binding.bottomNavigation, R.id.nav_forum)
        setProfile()
        setPosts()
    }

    private fun setProfile() {
        val userId = intent.getStringExtra(EXTRA_USER_POST_ID)
        if (userId != null) {
            viewModel.findUserById(userId).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                    }
                    is ResultState.Success -> {
                        showLoading(false)
                        binding.tvForumUserName.text = result.data.name
                        binding.tvForumUserEmail.text = result.data.email
                        val token = viewModel.getUser().token
                        if (result.data.imageProfile != null && token != null) {
                            Glide.with(this)
                                .load(getAuthenticatedGlideUrl(result.data.imageProfile, token))
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                                .into(binding.ivForumUserProfile)
                        }
                    }
                }
            }
        }
    }

    private fun setPosts() {
        val userId = intent.getStringExtra(EXTRA_USER_POST_ID)
        if (userId != null) {
            viewModel.findPostByUser(userId).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val token = viewModel.getUser().token!!
                        val rv: RecyclerView = binding.rvUserPosts
                        val adapter = ForumPostAdapter(result.data, token) { data ->
                            viewModel.setLike(LikePostRequest(data.post.postId, true)).observe(this) {
                                when (it) {
                                    is ResultState.Loading -> {
                                    }

                                    is ResultState.Error -> {
                                    }

                                    is ResultState.Success -> {
                                        setPosts()
                                    }
                                }
                            }
                        }

                        rv.layoutManager = LinearLayoutManager(this)
                        rv.adapter = adapter
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ForumUserActivity, ForumActivity::class.java))
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER_POST_ID = "extra_user_post_id"
        private const val TAG = "ForumUserActivity"
    }
}