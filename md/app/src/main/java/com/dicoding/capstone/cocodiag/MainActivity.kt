package com.dicoding.capstone.cocodiag

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.dicoding.capstone.cocodiag.features.price.PriceAdapter
import com.dicoding.capstone.cocodiag.features.price.PriceItem
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        initializeDependencies()
        fetchNews()
        fetchPrice()

    }

    private fun setupUI() {
        setBottomNavBar(this@MainActivity, binding.bottomNavigation, R.id.nav_home)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvPrice.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    private fun initializeDependencies() {
        val userPreference = UserPreference.getInstance(this.dataStore)
        apiService = ApiConfig.getApiService(userPreference)
    }

    private fun fetchNews() {
        lifecycleScope.launch {
            try {
                val newsResponse = apiService.getNews()
                handleNewsResponse(newsResponse)
                binding.progressBar.visibility = View.GONE
            } catch (e: Exception) {
                binding.progressBar.visibility = View.VISIBLE
                Log.e("Error Article", e.message ?: "Unknown error")
            }
        }
    }

    private fun handleNewsResponse(articles: List<ArticleModel>) {
        val adapter = ArticleAdapter(articles)
        binding.rvMain.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun fetchPrice(){
        lifecycleScope.launch {
            try {
                val priceResponse=apiService.getPrice()
                handlePriceResponse(priceResponse)
            }catch (e: Exception){
                Log.e("Error Price", e.message ?: "Unknown error")
            }

        }
    }

    private fun handlePriceResponse(prices: PriceItem) {
        val adapter = PriceAdapter(prices)
        binding.rvPrice.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
