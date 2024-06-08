package com.dicoding.capstone.cocodiag

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstone.cocodiag.common.setBottomNavBar
import com.dicoding.capstone.cocodiag.data.local.UserPreference
import com.dicoding.capstone.cocodiag.data.local.model.ArticleModel
import com.dicoding.capstone.cocodiag.data.remote.ApiConfig
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import com.dicoding.capstone.cocodiag.databinding.ActivityMainBinding
import com.dicoding.capstone.cocodiag.features.article.ArticleAdapter
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavBar(this@MainActivity, binding.bottomNavigation, R.id.nav_home)
        binding.rvMain.layoutManager=LinearLayoutManager(this)

        val userPreference=UserPreference.getInstance(this.dataStore)
        apiService=ApiConfig.getApiService(userPreference)
        fetchNews()

    }

    private fun fetchNews() {
        lifecycleScope.launch {
            try {
                val newsResponse = apiService.getNews()
                handleNewsResponse(newsResponse)
                binding.progressBar.visibility= View.GONE
            } catch (e: Exception) {
                binding.progressBar.visibility= View.VISIBLE
                Log.d("Error Article","${e.message}")
                Toast.makeText(this@MainActivity, "Failed to fetch news: ${e.message}", Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun handleNewsResponse(articles: List<ArticleModel>) {
        val adapter=ArticleAdapter(articles)
        binding.rvMain.adapter=adapter
        adapter.notifyDataSetChanged()
    }
}