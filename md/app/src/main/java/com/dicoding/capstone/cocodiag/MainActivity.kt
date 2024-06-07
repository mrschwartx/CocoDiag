package com.dicoding.capstone.cocodiag

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstone.cocodiag.common.getSampleArticles
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.databinding.ActivityMainBinding
import com.dicoding.capstone.cocodiag.features.article.Article
import com.dicoding.capstone.cocodiag.features.article.ArticleAdapter

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@MainActivity, binding.bottomNavigation, R.id.nav_home)
        binding.rvMain.layoutManager=LinearLayoutManager(this)

        val dataRv= getSampleArticles()
        val adapter=ArticleAdapter(dataRv)
        binding.rvMain.adapter=adapter


    }
}