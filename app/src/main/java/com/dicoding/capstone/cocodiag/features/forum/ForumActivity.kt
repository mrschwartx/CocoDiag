package com.dicoding.capstone.cocodiag.features.forum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.data.dummy.dummyForumStatus
import com.dicoding.capstone.cocodiag.databinding.ActivityForumBinding

class ForumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@ForumActivity, binding.bottomNavigation, R.id.nav_forum)

        val rv: RecyclerView = binding.rvForumStatus
        rv.layoutManager = LinearLayoutManager(this)

        val adapter  = ForumStatusAdapter(dummyForumStatus)
        rv.adapter = adapter
    }

}