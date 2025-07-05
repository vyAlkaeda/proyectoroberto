package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.symptomToSystemByAge
import com.example.myapplication.data.symptomDescriptions
import com.example.myapplication.databinding.ActivityNecropsiaBinding

class NecropsiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNecropsiaBinding
    private lateinit var necropsiaAdapter: NecropsiaAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private val rangosEdad = listOf(
        "Todas las edades",
        "Lechones lactantes (0-21 días)",
        "Lechones destetados (22-56 días)",
        "Crecimiento/Engorda (57 días a mercado)",
        "Adultos/Reproductores"
    )

    private var rangoEdadSeleccionado = "Todas las edades"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNecropsiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la edad seleccionada desde el Intent, si existe
        val edadIntent = intent.getStringExtra("EDAD_SELECCIONADA")
        if (edadIntent != null && rangosEdad.contains(edadIntent)) {
            rangoEdadSeleccionado = edadIntent
        }

        setupToolbar()
        setupAgeSpinner()
        setupRecyclerViews()
        loadNecropsiaForCategory("Digestivo") // Cargar digestivo por defecto
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Diagnóstico por Necropsia"
    }

    private fun setupAgeSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rangosEdad)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.ageSpinner.adapter = adapter

        // Seleccionar el valor inicial si viene del Intent
        val selectedIndex = rangosEdad.indexOf(rangoEdadSeleccionado)
        if (selectedIndex >= 0) {
            binding.ageSpinner.setSelection(selectedIndex)
        }

        binding.ageSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                rangoEdadSeleccionado = rangosEdad[position]
                updateCategoriesForAge()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun updateCategoriesForAge() {
        val sistemasDisponibles = getSistemasDisponiblesParaEdad(rangoEdadSeleccionado)
        categoryAdapter.updateCategories(sistemasDisponibles)
        
        if (sistemasDisponibles.isNotEmpty()) {
            loadNecropsiaForCategory(sistemasDisponibles[0])
        }
    }

    private fun getSistemasDisponiblesParaEdad(edad: String): List<String> {
        val sistemas = mutableSetOf<String>()
        
        symptomToSystemByAge.forEach { (sintoma, info) ->
            if (edad == "Todas las edades" || info.edades.contains(edad)) {
                sistemas.add(info.sistema)
            }
        }
        
        return sistemas.sorted()
    }

    private fun setupRecyclerViews() {
        val sistemasIniciales = getSistemasDisponiblesParaEdad(rangoEdadSeleccionado)
        categoryAdapter = CategoryAdapter(sistemasIniciales) { category ->
            loadNecropsiaForCategory(category)
        }
        binding.categoriesRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(this@NecropsiaActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        necropsiaAdapter = NecropsiaAdapter(emptyList()) { selectedClass ->
            selectedClass?.let { 
                startActivity(Intent(this, it))
            }
        }
        binding.necropsiaRecyclerView.apply {
            adapter = necropsiaAdapter
            layoutManager = LinearLayoutManager(this@NecropsiaActivity)
        }
    }

    private fun loadNecropsiaForCategory(category: String) {
        val sintomasDisponibles = getSintomasDisponiblesParaSistemaYEdad(category, rangoEdadSeleccionado)
        
        val necropsia = sintomasDisponibles.map { sintoma ->
            NecropsiaItem(
                sintoma,
                symptomDescriptions[sintoma] ?: "Descripción no disponible",
                "SÍNTOMA",
                null // Por ahora no hay actividad específica para cada síntoma
            )
        }
        
        Log.d("NecropsiaActivity", "Número de síntomas cargados para $category: ${necropsia.size}")
        necropsiaAdapter.updateNecropsia(necropsia)
    }

    private fun getSintomasDisponiblesParaSistemaYEdad(sistema: String, edad: String): List<String> {
        return symptomToSystemByAge.filter { (sintoma, info) ->
            info.sistema == sistema && (edad == "Todas las edades" || info.edades.contains(edad))
        }.keys.toList().sorted()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 