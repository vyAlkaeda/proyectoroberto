package com.example.myapplication

import android.content.Intent
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
                
                // Configurar click listener para navegar al detalle
                root.setOnClickListener {
                    val intent = Intent(itemView.context, DiseaseDetailActivity::class.java)
                    intent.putExtra("ENFERMEDAD_NOMBRE", disease)
                    itemView.context.startActivity(intent)
                }
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