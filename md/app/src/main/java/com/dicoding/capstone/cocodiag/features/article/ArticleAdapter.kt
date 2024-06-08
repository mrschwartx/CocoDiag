package com.dicoding.capstone.cocodiag.features.article


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.data.local.model.ArticleModel
import com.dicoding.capstone.cocodiag.databinding.ItemArticleBinding

class ArticleAdapter(private val articles: List<ArticleModel>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleModel) {
            binding.titleArticle.text = article.title
            binding.descArticle.text = article.description

            Glide.with(binding.root.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.logo_no_text)
                .into(binding.imgArticle)

            binding.root.setOnClickListener {
                val intent = Intent(it.context, WebViewArticleActivity::class.java)
                intent.putExtra("url", article.url)
                it.context.startActivity(intent)
            }
        }
    }
}
