package com.dicoding.capstone.cocodiag.features.forum

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.getAuthenticatedGlideUrl
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.data.remote.payload.LikePostRequest
import com.dicoding.capstone.cocodiag.databinding.ActivityForumBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.settings.EditProfileActivity

class ForumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForumBinding

    private val viewModel by viewModels<ForumViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var userId: String
    private lateinit var userImage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@ForumActivity, binding.bottomNavigation, R.id.nav_forum)
        setCurrentUser()
        setLatestPost()
        showLoading(false)

        binding.ctCurrentProfile.setOnClickListener {
            val moveIntent = Intent(this, EditProfileActivity::class.java)
            startActivity(moveIntent)
            finish()
        }

        binding.fabAddPost.setOnClickListener {
            val moveIntent = Intent(this@ForumActivity, ForumAddActivity::class.java)
            startActivity(moveIntent)
            finish()
        }
    }

    private fun setCurrentUser() {
        viewModel.findUserById(viewModel.getUser().id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showProfileLoading(true)
                    binding.ivCurrentProfile.visibility = View.GONE
                }

                is ResultState.Error -> {
                    showProfileLoading(false)
                    binding.ivCurrentProfile.visibility = View.VISIBLE
                    showToast(this, result.error.message)
                }

                is ResultState.Success -> {
                    showProfileLoading(false)
                    binding.ivCurrentProfile.visibility = View.VISIBLE

                    userId = result.data.userId
                    userImage = result.data.imageProfile ?: ""

                    if (userImage != "") {
                        val token = viewModel.getUser().token!!
                        Glide.with(this)
                            .load(getAuthenticatedGlideUrl(userImage, token))
                            .into(binding.ivCurrentProfile)
                    }
                }
            }
        }
    }

    private fun setLatestPost() {
        viewModel.findLatestPost().observe(this) { result ->
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
                    val rv: RecyclerView = binding.rvPosts
                    val adapter = ForumPostAdapter(result.data, token) { data ->
                        viewModel.setLike(LikePostRequest(data.post.postId, true)).observe(this) {
                            when (it) {
                                is ResultState.Loading -> {
                                }

                                is ResultState.Error -> {
                                }

                                is ResultState.Success -> {
                                    setLatestPost()
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

    private fun showProfileLoading(isLoading: Boolean) {
        binding.pbCurrentProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}