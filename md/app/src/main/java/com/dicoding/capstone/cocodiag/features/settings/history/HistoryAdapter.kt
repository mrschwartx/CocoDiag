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
import com.dicoding.capstone.cocodiag.data.remote.payload.HistoryResponse

class HistoryAdapter(private val historyList: List<HistoryResponse>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]
        holder.bind(history)

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, DetailHistoryActivity::class.java)
            val controls=history.controls
            val controlsString = when (controls) {
                is ArrayList<*> -> {
                    val controlList = controls as? ArrayList<String>
                    controlList?.joinToString("\n") ?: ""
                }
                is String -> controls
                else -> ""
            }

            val symptoms=history.symptoms
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
            intent.putExtra("SYMPTOMS",symptomsString)
            intent.putExtra("IM  bb                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          AGE_URL", history.imageUrl)
            holder.itemView.context.startActivity(intent)
        }
    }



    override fun getItemCount() = historyList.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLabel: TextView = itemView.findViewById(R.id.tv_disease_name_history)
        private val img : ImageView =itemView.findViewById(R.id.img_item_history)


        fun bind(history: HistoryResponse) {
            tvLabel.text = history.label
            Glide.with(itemView.context)
                .load(history.imageUrl)
                .into(img)
        }
    }
}
