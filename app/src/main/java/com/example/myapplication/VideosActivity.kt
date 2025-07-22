package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityVideosBinding

class VideosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideosBinding
    private lateinit var videosAdapter: VideosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadVideos()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Videos Educativos"
    }

    private fun setupRecyclerView() {
        videosAdapter = VideosAdapter { videoInfo ->
            openVideo(videoInfo.url)
        }
        binding.videosRecyclerView.apply {
            adapter = videosAdapter
            layoutManager = LinearLayoutManager(this@VideosActivity)
        }
    }

    private fun loadVideos() {
        val videosList = listOf(
            VideoInfo(
                title = "Bioseguridad en Granjas Porcinas",
                description = "Medidas esenciales de bioseguridad para prevenir enfermedades en cerdos",
                duration = "8:45",
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ", // URL de ejemplo
                thumbnailRes = R.drawable.ic_video
            ),
            VideoInfo(
                title = "Síntomas Respiratorios en Cerdos",
                description = "Identificación y manejo de problemas respiratorios comunes",
                duration = "12:30",
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailRes = R.drawable.ic_video
            ),
            VideoInfo(
                title = "Técnicas de Necropsia",
                description = "Guía paso a paso para realizar necropsias en porcinos",
                duration = "15:20",
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailRes = R.drawable.ic_video
            ),
            VideoInfo(
                title = "Manejo Sanitario en Lechones",
                description = "Cuidados especiales para lechones recién nacidos",
                duration = "10:15",
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailRes = R.drawable.ic_video
            ),
            VideoInfo(
                title = "Vacunación en Porcinos",
                description = "Calendario y técnicas de vacunación recomendadas",
                duration = "14:00",
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailRes = R.drawable.ic_video
            )
        )
        
        videosAdapter.updateVideos(videosList)
    }

    private fun openVideo(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // Si no se puede abrir el video, mostrar mensaje
            android.widget.Toast.makeText(this, "No se pudo abrir el video", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    data class VideoInfo(
        val title: String,
        val description: String,
        val duration: String,
        val url: String,
        val thumbnailRes: Int
    )
}