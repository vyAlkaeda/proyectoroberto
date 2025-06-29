package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemDiseaseBinding

class DiseaseAdapter(
    private var diseases: List<String>
) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    inner class DiseaseViewHolder(private val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: String) {
            binding.apply {
                diseaseNameTextView.text = disease
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(diseases[position])
    }

    override fun getItemCount() = diseases.size

    fun updateDiseases(newDiseases: List<String>) {
        diseases = newDiseases
        notifyDataSetChanged()
    }
} 