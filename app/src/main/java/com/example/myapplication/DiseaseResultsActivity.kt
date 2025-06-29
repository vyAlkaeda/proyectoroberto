package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityDiseaseResultsBinding

class DiseaseResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiseaseResultsBinding
    private lateinit var diseaseAdapter: DiseaseAdapter

    companion object {
        const val EXTRA_SELECTED_SYMPTOMS = "selected_symptoms"
        const val EXTRA_DISEASES = "diseases"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Enfermedades Posibles"
    }

    private fun setupRecyclerView() {
        diseaseAdapter = DiseaseAdapter(emptyList())
        binding.diseasesRecyclerView.apply {
            adapter = diseaseAdapter
            layoutManager = LinearLayoutManager(this@DiseaseResultsActivity)
        }
    }

    private fun loadData() {
        val selectedSymptoms = intent.getStringArrayListExtra(EXTRA_SELECTED_SYMPTOMS) ?: arrayListOf()
        val diseases = intent.getStringArrayListExtra(EXTRA_DISEASES) ?: arrayListOf()

        // Mostrar síntomas seleccionados
        binding.selectedSymptomsText.text = "Síntomas seleccionados: ${selectedSymptoms.joinToString(", ")}"

        // Mostrar enfermedades encontradas
        if (diseases.isEmpty()) {
            binding.noResultsText.visibility = android.view.View.VISIBLE
            binding.diseasesRecyclerView.visibility = android.view.View.GONE
        } else {
            binding.noResultsText.visibility = android.view.View.GONE
            binding.diseasesRecyclerView.visibility = android.view.View.VISIBLE
            diseaseAdapter.updateDiseases(diseases)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 