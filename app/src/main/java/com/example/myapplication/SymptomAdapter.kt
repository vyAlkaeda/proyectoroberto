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
    val activityClass: Class<*>
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
                typeTextView.text = symptom.type
                
                // Configurar el color de fondo del tipo según sea LESIÓN o SÍNTOMA
                val backgroundColor = when (symptom.type) {
                    "LESIÓN" -> R.color.lesion_tag
                    "SÍNTOMA" -> R.color.sintoma_tag
                    else -> R.color.primary
                }
                typeTextView.setBackgroundColor(ContextCompat.getColor(root.context, backgroundColor))

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
                                    onItemClick(symptom.activityClass)
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