package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivitySymptomDetailBaseBinding

abstract class BaseSymptomDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySymptomDetailBaseBinding

    abstract val title: String
    abstract val description: String
    abstract val type: String

    // NUEVO: Lista opcional de enfermedades
    open val relatedDiseases: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySymptomDetailBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupContent()
        setupDiseaseList()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = title
    }

    private fun setupContent() {
        binding.descriptionText.text = description
        binding.typeText.text = type
    }

    private fun setupDiseaseList() {
        if (relatedDiseases.isNotEmpty()) {
            binding.relatedDiseasesTitle.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = DiseaseAdapter(relatedDiseases) { selectedDisease ->
                val intent = Intent(this, DiseaseDetailActivity::class.java)
                intent.putExtra("disease_name", selectedDisease)
                startActivity(intent)
            }
        } else {
            binding.relatedDiseasesTitle.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
