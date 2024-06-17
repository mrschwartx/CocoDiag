package com.dicoding.capstone.cocodiag.features.settings.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.common.getAuthenticatedGlideUrl
import com.dicoding.capstone.cocodiag.data.remote.payload.HistoryResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val historyList: List<HistoryResponse>,
    private val token: String
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view, token)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]
        holder.bind(history)

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, DetailHistoryActivity::class.java)
            val controls = history.controls
            val controlsString = when (controls) {
                is ArrayList<*> -> {
                    val controlList = controls as? ArrayList<String>
                    controlList?.joinToString("\n") ?: ""
                }

                is String -> controls
                else -> ""
            }

            val symptoms = history.symptoms
            val symptomsString = when (symptoms) {
                is ArrayList<*> -> {
                    val symptomsList = symptoms as? ArrayList<String>
                    symptomsList?.joinToString("\n") ?: ""
                }

                is String -> symptoms
                else -> ""
            }

            intent.putExtra("LABEL", history.label)
            intent.putExtra("NAME", history.name)
            intent.putExtra("CONTROLS", controlsString)
            intent.putExtra("SYMPTOMS", symptomsString)
            intent.putExtra(
                "IM  bb                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          AGE_URL",
                history.imageUrl
            )
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount() = historyList.size

    class HistoryViewHolder(itemView: View, token: String) : RecyclerView.ViewHolder(itemView) {
        private val tvLabel: TextView = itemView.findViewById(R.id.tv_disease_name_history)
        private val createdLabel: TextView = itemView.findViewById(R.id.tv_disease_created_history)
        private val img: ImageView = itemView.findViewById(R.id.img_item_history)
        private val jwt = token

        fun bind(history: HistoryResponse) {
            tvLabel.text = history.label
            history.createdAt.let { createdAt ->
                val date = Date(createdAt * 1000L)
                val sdf = SimpleDateFormat("dd MMM yyyy ", Locale.getDefault())
                val formattedDate = sdf.format(date)
                createdLabel.text = "Classified at $formattedDate"
            }
            Glide.with(itemView.context)
                .load(getAuthenticatedGlideUrl(history.imageUrl, jwt))
                .into(img)
        }
    }
}
