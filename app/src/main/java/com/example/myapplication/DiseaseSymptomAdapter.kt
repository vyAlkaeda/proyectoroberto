package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemDiseaseSymptomBinding

class DiseaseSymptomAdapter(
    private val tipoDiagnostico: String
) : RecyclerView.Adapter<DiseaseSymptomAdapter.SymptomViewHolder>() {

    private var symptoms: List<String> = emptyList()

    fun updateSymptoms(newSymptoms: List<String>) {
        symptoms = newSymptoms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder {
        val binding = ItemDiseaseSymptomBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SymptomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SymptomViewHolder, position: Int) {
        holder.bind(symptoms[position])
    }

    override fun getItemCount(): Int = symptoms.size

    inner class SymptomViewHolder(
        private val binding: ItemDiseaseSymptomBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(symptom: String) {
            binding.symptomNameText.text = symptom
            
            // Aplicar estilo según el tipo de diagnóstico
            if (tipoDiagnostico == "NECROPSIA") {
                binding.symptomIcon.setColorFilter(binding.root.context.getColor(R.color.necro_primary))
                binding.symptomNameText.setTextColor(binding.root.context.getColor(R.color.necro_primary_dark))
            } else {
                binding.symptomIcon.setColorFilter(binding.root.context.getColor(R.color.primary))
                binding.symptomNameText.setTextColor(binding.root.context.getColor(R.color.primary_dark))
            }
        }
    }
}