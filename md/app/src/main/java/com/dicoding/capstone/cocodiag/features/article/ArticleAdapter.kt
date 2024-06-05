package com.dicoding.capstone.cocodiag.features.article


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.cocodiag.R

class ArticleAdapter (private val mList: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = mList[position]

        holder.textView.text=article.title

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, WebViewArticleActivity::class.java)
            intent.putExtra("url", article.link)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.title_article)
    }
}