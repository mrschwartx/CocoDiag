package com.dicoding.capstone.cocodiag.features.classification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.cocodiag.R
import com.dicoding.capstone.cocodiag.data.dummy.CareInstruction

class CareInstructionAdapter(
    private val context: Context,
    private val careInstructions: List<CareInstruction>
) : RecyclerView.Adapter<CareInstructionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_care_instruction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val instruction = careInstructions[position]

        // Bind data to views
        holder.textViewDiseaseName.text = instruction.diseaseName
        holder.textViewIdentification.text = instruction.identification
        holder.textViewTreatment.text = instruction.treatment
    }

    override fun getItemCount(): Int {
        return careInstructions.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDiseaseName: TextView = itemView.findViewById(R.id.tv_disease_name)
        val textViewIdentification: TextView = itemView.findViewById(R.id.tv_identification)
        val textViewTreatment: TextView = itemView.findViewById(R.id.tv_treatment)
    }
}