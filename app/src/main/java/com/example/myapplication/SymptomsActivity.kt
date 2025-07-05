package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.Enfermedad
import com.example.myapplication.data.cargarEnfermedadesDesdeJson
import com.example.myapplication.data.symptomToSystem
import com.example.myapplication.databinding.ActivitySymptomsBinding
import com.example.myapplication.PeloHirsutoDetailActivity
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.view.ViewGroup
import java.text.Normalizer

class SymptomsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySymptomsBinding
    private lateinit var symptomsAdapter: SymptomAdapter
    private lateinit var multiSelectAdapter: MultiSelectSymptomAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private var isMultiSelectMode = false
    private var selectedSymptoms = mutableListOf<String>()
    private lateinit var consultButton: View
    private lateinit var enfermedades: List<Enfermedad>
    private var sintomasPorSistema: Map<String, List<String>> = emptyMap()
    private var sistemasDisponibles: List<String> = emptyList()
    private val rangosEdad = listOf(
        "Todas las edades",
        "Lechones lactantes (0-21 días)",
        "Lechones destetados (22-56 días)",
        "Crecimiento/Engorda (57 días a mercado)",
        "Adultos/Reproductores"
    )
    private var rangoEdadSeleccionado = "Todas las edades"

    private val categories = listOf(
        "Sistémico",
        "Digestivo",
        "Nervioso",
        "Musculoesquelético",
        "Respiratorio",
        "Cardiovascular",
        "Hematopoyético",
        "General",
        "Dermatológico",
        "Ocular",
        "Hepático",
        "Inmunológico"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la edad seleccionada desde el Intent, si existe
        val edadIntent = intent.getStringExtra("EDAD_SELECCIONADA")
        if (edadIntent != null && rangosEdad.contains(edadIntent)) {
            rangoEdadSeleccionado = edadIntent
        }

        setupToolbar()
        setupAgeSpinner()
        setupRecyclerViews()
        if (sistemasDisponibles.isNotEmpty()) {
            loadSymptomsForSystem(sistemasDisponibles[0])
        }
        consultButton = binding.consultButton
        consultButton.visibility = View.GONE
        consultButton.setOnClickListener {
            val enfermedades = com.example.myapplication.data.DiseaseData.diseases.map {
                com.example.myapplication.data.Enfermedad(
                    nombre = it.name,
                    sintomas = it.symptoms,
                    rangoEdad = listOf("Todas")
                )
            }
            val selectedNormalized = selectedSymptoms.map { normalizarTexto(it) }
            val enfermedadesConContador = enfermedades.mapNotNull { enfermedad ->
                val coincidencias = enfermedad.sintomas.count { sintoma ->
                    val sintomaNorm = normalizarTexto(sintoma)
                    selectedNormalized.any { sel ->
                        sintomaNorm.contains(sel) || sel.contains(sintomaNorm)
                    }
                }
                if (coincidencias > 0) enfermedad.nombre to coincidencias else null
            }
            // Filtrar según la cantidad de síntomas seleccionados
            val enfermedadesFiltradas = if (selectedSymptoms.size >= 2) {
                enfermedadesConContador.filter { it.second > 1 }
            } else {
                enfermedadesConContador
            }
            val intent = Intent(this, FoundDiseasesActivity::class.java)
            intent.putExtra("enfermedades_con_contador", HashMap(enfermedadesFiltradas.toMap()))
            startActivity(intent)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Signos y Síntomas"
    }

    private fun setupRecyclerViews() {
        categoryAdapter = CategoryAdapter(sistemasDisponibles) { sistema ->
            loadSymptomsForSystem(sistema)
        }
        binding.categoriesRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(this@SymptomsActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        multiSelectAdapter = MultiSelectSymptomAdapter(emptyList()) { selected ->
            selectedSymptoms.clear()
            selectedSymptoms.addAll(selected)
            onSymptomsSelectionChanged()
        }
        binding.symptomsRecyclerView.apply {
            adapter = multiSelectAdapter
            layoutManager = LinearLayoutManager(this@SymptomsActivity)
        }
    }

    private fun loadSymptomsForSystem(sistema: String) {
        val sintomas = sintomasPorSistema[sistema]?.map { nombre ->
            SymptomItem(
                nombre,
                com.example.myapplication.data.symptomDescriptions[nombre] ?: "",
                "SÍNTOMA",
                null
            )
        } ?: emptyList()
        multiSelectAdapter.updateSymptoms(sintomas)
    }

    private fun onSymptomsSelectionChanged() {
        if (selectedSymptoms.isNotEmpty()) {
            Snackbar.make(binding.root, "Posible enfermedad encontrada", Snackbar.LENGTH_SHORT).show()
            consultButton.visibility = View.VISIBLE
        } else {
            consultButton.visibility = View.GONE
        }
        invalidateOptionsMenu()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.symptoms_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val multiSelectItem = menu?.findItem(R.id.action_multi_select)
        val searchItem = menu?.findItem(R.id.action_search_diseases)
        val clearItem = menu?.findItem(R.id.action_clear_selection)

        // Ocultar el ítem de selección múltiple
        multiSelectItem?.isVisible = false
        // Mostrar los otros solo si hay selección
        searchItem?.isVisible = selectedSymptoms.isNotEmpty()
        clearItem?.isVisible = selectedSymptoms.isNotEmpty()

        return true
    }

    private fun obtenerSintomasPorSistema(
        enfermedades: List<Enfermedad>,
        rangoEdad: String
    ): Map<String, List<String>> {
        val sintomas = if (rangoEdad == "Todas") {
            enfermedades.flatMap { it.sintomas }
        } else {
            enfermedades.filter { it.rangoEdad.contains(rangoEdad) }.flatMap { it.sintomas }
        }.distinct()
        return sintomas.groupBy { sintoma ->
            when (val sistema = com.example.myapplication.data.symptomToSystem[sintoma]) {
                is String -> sistema
                is Map<*, *> -> "Otros" // Para síntomas con clasificación por edad
                else -> "Otros"
            }
        }
    }

    // Función para normalizar texto: minúsculas, sin tildes, sin paréntesis ni caracteres especiales
    private fun normalizarTexto(texto: String): String {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
            .replace("[()\\[\\]{}.,:;°\\-]".toRegex(), "")
            .replace("\\s+", " ")
            .trim()
            .lowercase()
    }

    private fun setupAgeSpinner() {
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, rangosEdad)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.ageSpinner.adapter = adapter
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
        // Filtrar sistemas y síntomas según la edad seleccionada
        val sintomasFiltrados = if (rangoEdadSeleccionado == "Todas las edades") {
            com.example.myapplication.data.symptomToSystem.keys.toList()
        } else {
            com.example.myapplication.data.symptomToSystemByAge.filter { (_, info) ->
                info.edades.contains(rangoEdadSeleccionado)
            }.keys.toList()
        }
        
        // Obtener sistemas de manera segura, manejando tanto String como Map<String, String>
        val sistemasFiltrados = sintomasFiltrados.mapNotNull { sintoma ->
            when (val sistema = com.example.myapplication.data.symptomToSystem[sintoma]) {
                is String -> sistema
                is Map<*, *> -> {
                    // Si es un Map, obtener el sistema para la edad seleccionada
                    if (rangoEdadSeleccionado == "Todas las edades") {
                        sistema.values.firstOrNull() as? String
                    } else {
                        sistema[rangoEdadSeleccionado] as? String
                    }
                }
                else -> null
            }
        }.distinct().sorted()
        
        sistemasDisponibles = sistemasFiltrados
        sintomasPorSistema = sistemasFiltrados.associateWith { sistema ->
            sintomasFiltrados.filter { sintoma ->
                when (val sistemaEncontrado = com.example.myapplication.data.symptomToSystem[sintoma]) {
                    is String -> sistemaEncontrado == sistema
                    is Map<*, *> -> {
                        if (rangoEdadSeleccionado == "Todas las edades") {
                            sistemaEncontrado.values.contains(sistema)
                        } else {
                            sistemaEncontrado[rangoEdadSeleccionado] == sistema
                        }
                    }
                    else -> false
                }
            }
        }
        categoryAdapter.updateCategories(sistemasFiltrados)
        if (sistemasFiltrados.isNotEmpty()) {
            loadSymptomsForSystem(sistemasFiltrados[0])
        } else {
            multiSelectAdapter.updateSymptoms(emptyList())
        }
    }
} 