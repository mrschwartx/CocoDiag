package com.dicoding.capstone.cocodiag.features.forum

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.common.convertBase64ToBitmap
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.common.showToast
import com.dicoding.capstone.cocodiag.data.dummy.dummyForumStatus
import com.dicoding.capstone.cocodiag.databinding.ActivityForumBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory

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
        setData()

        val rv: RecyclerView = binding.rvForumStatus
        rv.layoutManager = LinearLayoutManager(this)

        val adapter = ForumStatusAdapter(dummyForumStatus)
        rv.adapter = adapter
    }

    private fun setData() {
        viewModel.findById().observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showProfileLoading(true)
                    binding.ivProfile.visibility = View.GONE
                }

                is ResultState.Error -> {
                    showProfileLoading(false)
                    binding.ivProfile.visibility = View.VISIBLE
                    showToast(this, result.error.message)
                }

                is ResultState.Success -> {
                    showProfileLoading(false)
                    binding.ivProfile.visibility = View.VISIBLE

                    userId = result.data.userId ?: ""
                    userImage = result.data.imageProfile ?: ""


                    if (userImage != "") {
                        Glide.with(this)
                            .load(convertBase64ToBitmap(userImage))
                            .apply(RequestOptions().centerCrop())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.ivProfile)
                    }
                }
            }
        }
    }

    private fun showProfileLoading(isLoading: Boolean) {
        binding.pbIvProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}