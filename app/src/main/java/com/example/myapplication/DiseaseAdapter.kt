package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiseaseAdapter(
    private val diseases: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return DiseaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        val disease = diseases[position]
        holder.textView.text = disease
        holder.itemView.setOnClickListener { onItemClick(disease) }
    }

    override fun getItemCount(): Int = diseases.size

    class DiseaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }
} 