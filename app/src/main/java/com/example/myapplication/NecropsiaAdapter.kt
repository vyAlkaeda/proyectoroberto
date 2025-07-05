package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemSymptomBinding

class NecropsiaAdapter(
    private var necropsia: List<NecropsiaItem>,
    private val onItemClick: (Class<*>?) -> Unit
) : RecyclerView.Adapter<NecropsiaAdapter.NecropsiaViewHolder>() {

    class NecropsiaViewHolder(private val binding: ItemSymptomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NecropsiaItem, onItemClick: (Class<*>?) -> Unit) {
            binding.titleTextView.text = item.title
            binding.descriptionTextView.text = item.description
            binding.root.setOnClickListener { onItemClick(item.detailActivity) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NecropsiaViewHolder {
        val binding = ItemSymptomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NecropsiaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NecropsiaViewHolder, position: Int) {
        holder.bind(necropsia[position], onItemClick)
    }

    override fun getItemCount() = necropsia.size

    fun updateNecropsia(newNecropsia: List<NecropsiaItem>) {
        necropsia = newNecropsia
        notifyDataSetChanged()
    }
}

data class NecropsiaItem(
    val title: String,
    val description: String,
    val type: String,
    val detailActivity: Class<*>?
) 