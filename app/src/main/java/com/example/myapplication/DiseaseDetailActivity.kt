package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.DiseaseData
import com.example.myapplication.databinding.ActivityDiseaseDetailBinding

class DiseaseDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDiseaseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val diseaseName = intent.getStringExtra("disease_name") ?: ""
        binding.titleText.text = diseaseName.uppercase()
        binding.descriptionText.text = "" // Vacío para que lo llenes después

        // Buscar síntomas asociados
        val disease = DiseaseData.diseases.find { it.name.equals(diseaseName, ignoreCase = true) }
        val symptoms = disease?.symptoms ?: emptyList()
        binding.symptomsText.text = if (symptoms.isNotEmpty()) {
            symptoms.joinToString(separator = "\n") { "• $it" }
        } else {
            "Sin síntomas registrados."
        }

        // Puedes ocultar el botón de recomendaciones si no lo necesitas
        binding.recommendationsButton.visibility = android.view.View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 