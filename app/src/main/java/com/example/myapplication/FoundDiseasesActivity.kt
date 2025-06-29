package com.example.myapplication

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityFoundDiseasesBinding
import androidx.recyclerview.widget.LinearLayoutManager

class FoundDiseasesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoundDiseasesBinding
    private lateinit var foundDiseasesAdapter: FoundDiseasesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoundDiseasesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Enfermedades encontradas"
        // Mostrar enfermedades y coincidencias
        foundDiseasesAdapter = FoundDiseasesAdapter(emptyList())
        binding.foundDiseasesRecyclerView.apply {
            adapter = foundDiseasesAdapter
            layoutManager = LinearLayoutManager(this@FoundDiseasesActivity)
        }
        val enfermedadesConContador = intent.getSerializableExtra("enfermedades_con_contador") as? HashMap<String, Int>
        val diseaseList = enfermedadesConContador?.map { it.key to it.value } ?: emptyList()
        foundDiseasesAdapter.updateDiseases(diseaseList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 