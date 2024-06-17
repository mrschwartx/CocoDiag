package com.dicoding.capstone.cocodiag.features.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.getAuthenticatedGlideUrl
import com.dicoding.capstone.cocodiag.data.local.model.CommentWithUserDetails
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForumCommentAdapter(
    private val commentList: List<CommentWithUserDetails>,
    private val token: String,
    private val userId: String,
    private val onDeleteClickListener: (String) -> Unit
) : RecyclerView.Adapter<ForumCommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForumCommentAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = commentList[position]

        holder.tvUserName.text = data.user.name
        holder.tvUserEmail.text = data.user.email

        if (data.user.imageProfile != null) {
            Glide.with(holder.itemView.context)
                .load(getAuthenticatedGlideUrl(data.user.imageProfile, token))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.ivUserProfile)
        }

        holder.tvCommentText.text = data.comment.comment

        data.comment.createdAt.let { createdAt ->
            val currentMillis = System.currentTimeMillis()
            val diffMillis = currentMillis - (createdAt * 1000L)

            val seconds = diffMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30

            val timeAgo = when {
                months > 0 -> {
                    val date = Date(createdAt * 1000L)
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    sdf.format(date)
                }

                days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
                hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
                minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
                else -> "$seconds second${if (seconds > 1) "s" else ""} ago"
            }

            holder.tvCreatedAt.text = timeAgo
        }

        if (userId == data.user.userId) {
            holder.tvDelete.visibility = View.VISIBLE
            holder.tvDelete.setOnClickListener {
                onDeleteClickListener(data.comment.commentId)
            }
        } else {
            holder.tvDelete.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tv_comment_user_name)
        val tvUserEmail: TextView = itemView.findViewById(R.id.tv_comment_user_email)
        val ivUserProfile: CircleImageView = itemView.findViewById(R.id.iv_comment_user_profile)
        val tvCommentText: TextView = itemView.findViewById(R.id.tv_comment_post_text)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tv_comment_post_created_at)
        val tvDelete: TextView = itemView.findViewById(R.id.tv_delete)
    }
}