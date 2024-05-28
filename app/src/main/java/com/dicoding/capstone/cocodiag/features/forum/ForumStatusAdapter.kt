package com.dicoding.capstone.cocodiag.features.forum

import com.dicoding.capstone.cocodiag.data.dummy.ForumStatus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dicoding.capstone.cocodiag.R
import java.text.SimpleDateFormat
import java.util.*

class ForumStatusAdapter(
    private val statusList: List<ForumStatus>
) : RecyclerView.Adapter<ForumStatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val status = statusList[position]

        holder.nameText.text = status.name
        holder.emailText.text = status.email
        Glide.with(holder.itemView.context)
            .load(status.profileImage)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(holder.profileImage)

        if (status.imageContent != null) {
            Glide.with(holder.itemView.context)
                .load(status.imageContent)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.statusImage)
        }
        holder.statusText.text = status.textContent
        holder.likeText.text = "${status.likeCount} likes"
        holder.commentText.text = "${status.commentCount} comments"

        status.createdAt?.let { createdAt ->
            val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            val formattedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(createdAt)
            holder.createdAtText.text = formattedDate
        }
    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.tv_status_name)
        val emailText: TextView = itemView.findViewById(R.id.tv_status_email)
        val profileImage: ImageView = itemView.findViewById(R.id.iv_status_profile)
        val statusText: TextView = itemView.findViewById(R.id.tv_status_text)
        val statusImage: ImageView = itemView.findViewById(R.id.iv_status_image)
        val likeText: TextView = itemView.findViewById(R.id.tv_like)
        val commentText: TextView = itemView.findViewById(R.id.tv_comment)
        val createdAtText: TextView = itemView.findViewById(R.id.tv_created_at)
    }
}