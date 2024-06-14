package com.dicoding.capstone.cocodiag.features.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.data.local.model.PostWithUserDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForumPostAdapter(
    private val postList: List<PostWithUserDetails>
) : RecyclerView.Adapter<ForumPostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forum_post, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = postList[position]

        holder.nameText.text = data.user.name
        holder.emailText.text = data.user.email
//        Glide.with(holder.itemView.context)
//            .load(status.profileImage)
//            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
//            .into(holder.profileImage)

//        if (status.imageContent != null) {
//            Glide.with(holder.itemView.context)
//                .load(status.imageContent)
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
//                .into(holder.statusImage)
//        }
        holder.statusText.text = data.post.postText
        holder.likeText.text = "${data.post.countLike} likes"
        holder.commentText.text = "${data.post.countComment} comments"

        data.post.createdAt.let { createdAt ->
            val date = Date(createdAt * 1000L)
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
            val formattedDate = sdf.format(date)
            holder.createdAtText.text = formattedDate
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.tv_forum_post_name)
        val emailText: TextView = itemView.findViewById(R.id.tv_forum_post_email)
        val profileImage: ImageView = itemView.findViewById(R.id.iv_forum_post_profile)
        val statusText: TextView = itemView.findViewById(R.id.tv_forum_post_content)
        val statusImage: ImageView = itemView.findViewById(R.id.iv_forum_post_content)
        val likeText: TextView = itemView.findViewById(R.id.tv_forum_post_like)
        val commentText: TextView = itemView.findViewById(R.id.tv_forum_post_comment)
        val createdAtText: TextView = itemView.findViewById(R.id.tv_forum_post_created_at)
    }
}