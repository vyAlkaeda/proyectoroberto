package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemVideoBinding

class VideosAdapter(
    private val onVideoClick: (VideosActivity.VideoInfo) -> Unit
) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    private var videos: List<VideosActivity.VideoInfo> = emptyList()

    fun updateVideos(newVideos: List<VideosActivity.VideoInfo>) {
        videos = newVideos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    inner class VideoViewHolder(
        private val binding: ItemVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideosActivity.VideoInfo) {
            binding.videoTitleText.text = video.title
            binding.videoDescriptionText.text = video.description
            binding.videoDurationText.text = video.duration
            binding.videoThumbnail.setImageResource(video.thumbnailRes)
            
            binding.root.setOnClickListener {
                onVideoClick(video)
            }
        }
    }
}