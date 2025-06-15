package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemBannerBinding

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    private val images = listOf(
        R.drawable.banner_apps  // Usando banner_apps.png de la carpeta drawable
        // Aquí puedes agregar más imágenes cuando las tengas
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val imageRes = images[position % images.size]
        holder.bind(imageRes)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE // Para scroll infinito

    inner class BannerViewHolder(private val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageRes: Int) {
            binding.bannerImage.setImageResource(imageRes)
        }
    }
} 