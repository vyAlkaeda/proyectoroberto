package com.example.myapplication

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemSymptomMultiSelectBinding

class MultiSelectSymptomAdapter(
    private var symptoms: List<SymptomItem>,
    private val onSelectionChanged: (List<String>) -> Unit
) : RecyclerView.Adapter<MultiSelectSymptomAdapter.SymptomViewHolder>() {

    private val selectedSymptoms = mutableSetOf<String>()

    inner class SymptomViewHolder(private val binding: ItemSymptomMultiSelectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(symptom: SymptomItem) {
            binding.apply {
                titleTextView.text = symptom.title
                descriptionTextView.text = symptom.description
                
                // Configurar el estado del checkbox
                checkbox.isChecked = selectedSymptoms.contains(symptom.title)

                // Configurar el click listener para el checkbox
                checkbox.setOnClickListener {
                    if (checkbox.isChecked) {
                        selectedSymptoms.add(symptom.title)
                    } else {
                        selectedSymptoms.remove(symptom.title)
                    }
                    onSelectionChanged(selectedSymptoms.toList())
                }

                // Configurar el click listener para toda la tarjeta
                root.setOnClickListener {
                    // Mostrar diálogo con la descripción del síntoma
                    AlertDialog.Builder(root.context)
                        .setTitle(symptom.title)
                        .setMessage(symptom.description)
                        .setPositiveButton("Cerrar", null)
                        .show()
                    // Mantener la selección múltiple
                    checkbox.isChecked = !checkbox.isChecked
                    if (checkbox.isChecked) {
                        selectedSymptoms.add(symptom.title)
                    } else {
                        selectedSymptoms.remove(symptom.title)
                    }
                    onSelectionChanged(selectedSymptoms.toList())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder {
        val binding = ItemSymptomMultiSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun getSelectedSymptoms(): List<String> = selectedSymptoms.toList()

    fun clearSelection() {
        selectedSymptoms.clear()
        notifyDataSetChanged()
        onSelectionChanged(emptyList())
    }
} 