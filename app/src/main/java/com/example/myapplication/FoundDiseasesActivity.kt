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
        // Obtener datos del intent
        val enfermedadesConContador = intent.getSerializableExtra("ENFERMEDADES_CON_CONTADOR") as? HashMap<String, Int>
        val enfermedades = intent.getStringArrayListExtra("ENFERMEDADES") ?: arrayListOf()
        val sintomasSeleccionados = intent.getStringArrayListExtra("SINTOMAS_SELECCIONADOS") ?: arrayListOf()
        val tipoDiagnostico = intent.getStringExtra("TIPO_DIAGNOSTICO") ?: "SINTOMAS"

        // Configurar título según el tipo de diagnóstico
        val titulo = when (tipoDiagnostico) {
            "NECROPSIA" -> "Lesiones encontradas"
            else -> "Síntomas encontrados"
        }
        supportActionBar?.title = titulo

        // Mostrar enfermedades con contador
        if (enfermedadesConContador != null && enfermedadesConContador.isNotEmpty()) {
            val diseaseList = enfermedadesConContador.entries.sortedByDescending { it.value }.map { it.key to it.value }
            foundDiseasesAdapter.updateDiseases(diseaseList)
            binding.tvInfo.text = "Mostrando solo enfermedades con 2 o más síntomas seleccionados en común."
        } else {
            foundDiseasesAdapter.updateDiseases(emptyList())
            binding.tvInfo.text = "No se encontraron enfermedades con al menos 2 síntomas seleccionados en común."
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 