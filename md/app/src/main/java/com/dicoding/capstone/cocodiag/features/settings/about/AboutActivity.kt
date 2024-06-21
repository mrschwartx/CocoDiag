package com.dicoding.capstone.cocodiag.features.settings.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.capstone.cocodiag.common.defaultMember
import com.dicoding.capstone.cocodiag.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycleview.layoutManager=GridLayoutManager(this,2)

        val memberlist= emptyList<Member>().defaultMember

        val adapter=MemberAdapter(memberlist)
        binding.recycleview.adapter=adapter
    }
}