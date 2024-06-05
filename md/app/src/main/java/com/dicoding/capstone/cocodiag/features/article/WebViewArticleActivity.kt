package com.dicoding.capstone.cocodiag.features.article

import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.databinding.ActivityWebViewArticleBinding

class WebViewArticleActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWebViewArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWebViewArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val url=intent.getStringExtra("url")

        if (url != null){
            binding.webView.webViewClient= WebViewClient()
            binding.webView.settings.javaScriptEnabled=true
            binding.webView.loadUrl(url)
        }else {
            Log.e("WebViewActivity", "URL is null")
        }
    }
}