package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemDiseaseBinding

class FoundDiseasesAdapter(
    private var diseases: List<Pair<String, Int>>
) : RecyclerView.Adapter<FoundDiseasesAdapter.FoundDiseaseViewHolder>() {

    inner class FoundDiseaseViewHolder(private val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: Pair<String, Int>) {
            binding.diseaseNameTextView.text = disease.first
            binding.diseaseShortDescTextView.text = "Coincidencias: ${disease.second}"
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DiseaseDetailActivity::class.java)
                intent.putExtra("ENFERMEDAD_NOMBRE", disease.first)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundDiseaseViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoundDiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoundDiseaseViewHolder, position: Int) {
        holder.bind(diseases[position])
    }

    override fun getItemCount() = diseases.size

    fun updateDiseases(newDiseases: List<Pair<String, Int>>) {
        diseases = newDiseases
        notifyDataSetChanged()
    }
} 