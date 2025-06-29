package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemSymptomBinding

data class SymptomItem(
    val title: String,
    val description: String,
    val type: String,
    val activityClass: Class<*>? = null
)

class SymptomAdapter(
    private var symptoms: List<SymptomItem>,
    private val onItemClick: (Class<*>) -> Unit
) : RecyclerView.Adapter<SymptomAdapter.SymptomViewHolder>() {

    inner class SymptomViewHolder(private val binding: ItemSymptomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(symptom: SymptomItem) {
            binding.apply {
                titleTextView.text = symptom.title
                descriptionTextView.text = symptom.description
                
                // Añadir efecto de elevación al tocar la tarjeta
                root.setOnClickListener { 
                    root.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction {
                            root.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction {
                                    symptom.activityClass?.let { onItemClick(it) }
                                }
                        }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder {
        val binding = ItemSymptomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SymptomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SymptomViewHolder, position: Int) {
        holder.bind(symptoms[position])
    }

    override fun getItemCount() = symptoms.size

    fun updateSymptoms(newSymptoms: List<SymptomItem>) {
        symptoms = newSymptoms
        notifyDataSetChanged()
    }
} 