package com.dicoding.capstone.cocodiag.features.forum

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.getAuthenticatedGlideUrl
import com.dicoding.capstone.cocodiag.data.local.model.PostWithUserDetails
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForumPostAdapter(
    private val postList: List<PostWithUserDetails>,
    private val token: String,
    private val onLikeClickListener: (PostWithUserDetails) -> Unit
) : RecyclerView.Adapter<ForumPostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forum_post, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = postList[position]

        holder.tvUserName.text = data.user.name
        holder.tvUserEmail.text = data.user.email

        if (data.user.imageProfile != null) {
            Glide.with(holder.itemView.context)
                .load(getAuthenticatedGlideUrl(data.user.imageProfile, token))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.ivUserProfile)
        }

        if (data.post.postImage != null) {
            Glide.with(holder.itemView.context)
                .load(getAuthenticatedGlideUrl(data.post.postImage, token))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.ivPostImage)
            holder.ivPostImage.visibility = View.VISIBLE
        } else {
            holder.ivPostImage.visibility = View.GONE
        }

        holder.tvPostText.text = data.post.postText
        holder.tvCountLike.text = "${data.post.countLike} likes"
        holder.tvCountComment.text = "${data.post.countComment} comments"

        data.post.createdAt.let { createdAt ->
            val date = Date(createdAt * 1000L)
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
            val formattedDate = sdf.format(date)
            holder.tvCreatedAt.text = formattedDate
        }

        holder.likeLayout.setOnClickListener {
            animateLikeLayout(holder.likeLayout)
            onLikeClickListener(data)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tv_forum_user_name)
        val tvUserEmail: TextView = itemView.findViewById(R.id.tv_forum_user_email)
        val ivUserProfile: CircleImageView = itemView.findViewById(R.id.iv_forum_user_profile)
        val tvPostText: TextView = itemView.findViewById(R.id.tv_forum_post_text)
        val ivPostImage: ImageView = itemView.findViewById(R.id.iv_forum_post_image)
        val tvCountLike: TextView = itemView.findViewById(R.id.tv_forum_post_like)
        val tvCountComment: TextView = itemView.findViewById(R.id.tv_forum_post_comment)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tv_forum_post_created_at)

        val likeLayout: LinearLayout = itemView.findViewById(R.id.likeLayout)
    }

    private fun animateLikeLayout(layout: LinearLayout) {
        val originalColor = layout.background
        layout.setBackgroundColor(Color.LTGRAY)
        Handler(Looper.getMainLooper()).postDelayed({
            layout.background = originalColor
        }, 200)
    }
}