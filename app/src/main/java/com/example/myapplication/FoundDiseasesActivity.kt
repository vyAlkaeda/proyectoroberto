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
        
        // Configurar color del texto del toolbar como blanco
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        // Configurar color del icono de navegación como blanco
        binding.toolbar.navigationIcon?.setTint(resources.getColor(R.color.white))
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
            val textoInfo = when (tipoDiagnostico) {
                "NECROPSIA" -> "Mostrando solo enfermedades con 2 o más lesiones seleccionadas en común."
                else -> "Mostrando solo enfermedades con 2 o más síntomas seleccionados en común."
            }
            binding.tvInfo.text = textoInfo
        } else {
            foundDiseasesAdapter.updateDiseases(emptyList())
            val textoInfo = when (tipoDiagnostico) {
                "NECROPSIA" -> "No se encontraron enfermedades con al menos 2 lesiones seleccionadas en común."
                else -> "No se encontraron enfermedades con al menos 2 síntomas seleccionados en común."
            }
            binding.tvInfo.text = textoInfo
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 