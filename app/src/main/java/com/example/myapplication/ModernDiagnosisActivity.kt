package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityModernDiagnosisBinding
import com.example.myapplication.databinding.ItemModernSymptomBinding
import com.example.myapplication.databinding.ItemVeterinarySystemBinding
import com.example.myapplication.models.Symptom
import com.example.myapplication.models.VeterinarySystem
import com.example.myapplication.viewmodels.DiagnosisViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ModernDiagnosisActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityModernDiagnosisBinding
    private val viewModel: DiagnosisViewModel by viewModels()
    
    private lateinit var systemsAdapter: VeterinarySystemsAdapter
    private lateinit var symptomsAdapter: ModernSymptomsAdapter
    
    private var selectedAgeGroup: String = "Todas"
    private var selectedSystem: String? = null
    private var diagnosisType: String = "SINTOMAS"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModernDiagnosisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Obtener datos del intent
        selectedAgeGroup = intent.getStringExtra("EDAD_SELECCIONADA") ?: "Todas"
        diagnosisType = intent.getStringExtra("TIPO_DIAGNOSTICO") ?: "SINTOMAS"
        
        setupToolbar()
        setupRecyclerViews()
        setupClickListeners()
        observeViewModel()
        applyTheme()
        
        // Cargar datos iniciales
        viewModel.setCurrentAgeGroup(selectedAgeGroup)
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        val title = when (diagnosisType) {
            "NECROPSIA" -> "Diagnóstico por Necropsia"
            else -> "Diagnóstico por Síntomas"
        }
        supportActionBar?.title = title
        
        // Mostrar edad seleccionada
        binding.tvSelectedAge.text = if (selectedAgeGroup == "Todas") {
            "Todas las edades"
        } else {
            selectedAgeGroup
        }
    }
    
    private fun setupRecyclerViews() {
        // Adapter para sistemas veterinarios
        systemsAdapter = VeterinarySystemsAdapter { system ->
            selectedSystem = system.name
            viewModel.loadSymptomsBySystem(system.name, selectedAgeGroup)
            binding.tvSelectedSystem.text = system.name
            binding.tvSystemDescription.text = system.description
            binding.systemDetailsCard.visibility = View.VISIBLE
        }
        
        binding.recyclerViewSystems.apply {
            layoutManager = LinearLayoutManager(this@ModernDiagnosisActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = systemsAdapter
        }
        
        // Adapter para síntomas
        symptomsAdapter = ModernSymptomsAdapter { symptom ->
            viewModel.toggleSymptom(symptom.nombre)
        }
        
        binding.recyclerViewSymptoms.apply {
            layoutManager = GridLayoutManager(this@ModernDiagnosisActivity, 1)
            adapter = symptomsAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnPerformDiagnosis.setOnClickListener {
            if (viewModel.getSelectedSymptomsCount() > 0) {
                viewModel.performDiagnosis()
            } else {
                Toast.makeText(this, "Selecciona al menos un síntoma", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.btnClearSymptoms.setOnClickListener {
            viewModel.clearSelectedSymptoms()
            symptomsAdapter.notifyDataSetChanged()
            updateSelectedSymptomsChips()
        }
        
        binding.fabShowStats.setOnClickListener {
            showDiagnosisStats()
        }
        
        binding.btnChangeAge.setOnClickListener {
            showAgeSelectionDialog()
        }
    }
    
    private fun observeViewModel() {
        viewModel.veterinarySystems.observe(this) { systems ->
            val systemsList = systems.values.toList()
            systemsAdapter.submitList(systemsList)
            
            // Seleccionar el primer sistema por defecto
            if (systemsList.isNotEmpty() && selectedSystem == null) {
                val firstSystem = systemsList.first()
                selectedSystem = firstSystem.name
                viewModel.loadSymptomsBySystem(firstSystem.name, selectedAgeGroup)
                binding.tvSelectedSystem.text = firstSystem.name
                binding.tvSystemDescription.text = firstSystem.description
                binding.systemDetailsCard.visibility = View.VISIBLE
            }
        }
        
        viewModel.availableSymptoms.observe(this) { symptoms ->
            symptomsAdapter.submitList(symptoms)
            binding.tvSymptomsCount.text = "Síntomas disponibles: ${symptoms.size}"
        }
        
        viewModel.selectedSymptoms.observe(this) { selectedSymptoms ->
            updateSelectedSymptomsChips()
            updateDiagnosisButton(selectedSymptoms.size)
            binding.tvSelectedCount.text = "Seleccionados: ${selectedSymptoms.size}"
        }
        
        viewModel.diagnosisResults.observe(this) { results ->
            if (results.isNotEmpty()) {
                navigateToResults(results)
            }
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnPerformDiagnosis.isEnabled = !isLoading
        }
        
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
    
    private fun updateSelectedSymptomsChips() {
        binding.chipGroupSelectedSymptoms.removeAllViews()
        
        viewModel.selectedSymptoms.value?.forEach { symptomName ->
            val chip = Chip(this).apply {
                text = symptomName
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    viewModel.toggleSymptom(symptomName)
                }
            }
            binding.chipGroupSelectedSymptoms.addView(chip)
        }
        
        binding.selectedSymptomsCard.visibility = if (viewModel.getSelectedSymptomsCount() > 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    
    private fun updateDiagnosisButton(selectedCount: Int) {
        binding.btnPerformDiagnosis.apply {
            isEnabled = selectedCount > 0
            text = when (selectedCount) {
                0 -> "Selecciona síntomas"
                1 -> "Diagnosticar (1 síntoma)"
                else -> "Diagnosticar ($selectedCount síntomas)"
            }
        }
    }
    
    private fun showDiagnosisStats() {
        val stats = viewModel.getDiagnosisStats()
        val message = """
            Estadísticas del Diagnóstico:
            
            • Síntomas analizados: ${stats.symptomsAnalyzed}
            • Enfermedades encontradas: ${stats.totalDiseasesFound}
            • Alta probabilidad: ${stats.highProbabilityDiseases}
            • Probabilidad media: ${stats.mediumProbabilityDiseases}
            • Baja probabilidad: ${stats.lowProbabilityDiseases}
            • Probabilidad promedio: ${"%.1f".format(stats.averageProbability)}%
        """.trimIndent()
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Estadísticas")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showAgeSelectionDialog() {
        val ageGroups = arrayOf(
            "Todas",
            "Lechones lactantes",
            "Lechones destetados", 
            "Crecimiento/Engorda",
            "Adultos/Reproductores"
        )
        
        val currentIndex = ageGroups.indexOf(selectedAgeGroup)
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Seleccionar Edad")
            .setSingleChoiceItems(ageGroups, currentIndex) { dialog, which ->
                selectedAgeGroup = ageGroups[which]
                viewModel.setCurrentAgeGroup(selectedAgeGroup)
                binding.tvSelectedAge.text = if (selectedAgeGroup == "Todas") {
                    "Todas las edades"
                } else {
                    selectedAgeGroup
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun navigateToResults(results: List<com.example.myapplication.models.DiagnosisResult>) {
        val intent = Intent(this, ModernDiagnosisResultsActivity::class.java)
        intent.putParcelableArrayListExtra("DIAGNOSIS_RESULTS", ArrayList(results))
        intent.putExtra("SELECTED_AGE", selectedAgeGroup)
        intent.putExtra("DIAGNOSIS_TYPE", diagnosisType)
        startActivity(intent)
    }
    
    private fun applyTheme() {
        val primaryColor = if (diagnosisType == "NECROPSIA") {
            ContextCompat.getColor(this, R.color.necro_primary)
        } else {
            ContextCompat.getColor(this, R.color.primary)
        }
        
        binding.toolbar.setBackgroundColor(primaryColor)
        binding.btnPerformDiagnosis.setBackgroundColor(primaryColor)
        binding.fabShowStats.backgroundTintList = ContextCompat.getColorStateList(this, 
            if (diagnosisType == "NECROPSIA") R.color.necro_primary else R.color.primary
        )
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

// Adapter para sistemas veterinarios
class VeterinarySystemsAdapter(
    private val onSystemClick: (VeterinarySystem) -> Unit
) : RecyclerView.Adapter<VeterinarySystemsAdapter.SystemViewHolder>() {
    
    private var systems: List<VeterinarySystem> = emptyList()
    private var selectedPosition = 0
    
    fun submitList(newSystems: List<VeterinarySystem>) {
        systems = newSystems
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemViewHolder {
        val binding = ItemVeterinarySystemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SystemViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: SystemViewHolder, position: Int) {
        holder.bind(systems[position], position == selectedPosition)
    }
    
    override fun getItemCount(): Int = systems.size
    
    inner class SystemViewHolder(
        private val binding: ItemVeterinarySystemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(system: VeterinarySystem, isSelected: Boolean) {
            binding.tvSystemName.text = system.name
            binding.tvSymptomCount.text = "${system.symptoms.size} síntomas"
            
            // Aplicar estilo de selección
            if (isSelected) {
                binding.cardSystem.strokeWidth = 4
                binding.cardSystem.strokeColor = ContextCompat.getColor(binding.root.context, R.color.primary)
            } else {
                binding.cardSystem.strokeWidth = 1
                binding.cardSystem.strokeColor = ContextCompat.getColor(binding.root.context, R.color.gray_300)
            }
            
            binding.root.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onSystemClick(system)
            }
        }
    }
}

// Adapter para síntomas modernizado
class ModernSymptomsAdapter(
    private val onSymptomClick: (Symptom) -> Unit
) : RecyclerView.Adapter<ModernSymptomsAdapter.SymptomViewHolder>() {
    
    private var symptoms: List<Symptom> = emptyList()
    private var selectedSymptoms: Set<String> = emptySet()
    
    fun submitList(newSymptoms: List<Symptom>) {
        symptoms = newSymptoms
        notifyDataSetChanged()
    }
    
    fun updateSelectedSymptoms(selected: Set<String>) {
        selectedSymptoms = selected
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder {
        val binding = ItemModernSymptomBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SymptomViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: SymptomViewHolder, position: Int) {
        holder.bind(symptoms[position])
    }
    
    override fun getItemCount(): Int = symptoms.size
    
    inner class SymptomViewHolder(
        private val binding: ItemModernSymptomBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(symptom: Symptom) {
            binding.tvSymptomName.text = symptom.nombre
            binding.tvSymptomDescription.text = symptom.descripcion
            binding.tvSymptomType.text = symptom.tipo
            binding.tvDiseaseCount.text = "${symptom.enfermedades.size} enfermedades"
            
            val isSelected = selectedSymptoms.contains(symptom.nombre)
            binding.checkBoxSymptom.isChecked = isSelected
            
            // Aplicar estilo de selección
            if (isSelected) {
                binding.cardSymptom.strokeWidth = 3
                binding.cardSymptom.strokeColor = ContextCompat.getColor(binding.root.context, R.color.primary)
                binding.cardSymptom.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.primary_light))
            } else {
                binding.cardSymptom.strokeWidth = 1
                binding.cardSymptom.strokeColor = ContextCompat.getColor(binding.root.context, R.color.gray_300)
                binding.cardSymptom.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
            }
            
            // Mostrar indicador de severidad
            when (symptom.severity) {
                in 4..5 -> {
                    binding.viewSeverityIndicator.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.severity_high))
                }
                in 2..3 -> {
                    binding.viewSeverityIndicator.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.severity_medium))
                }
                else -> {
                    binding.viewSeverityIndicator.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.severity_low))
                }
            }
            
            binding.root.setOnClickListener {
                onSymptomClick(symptom)
            }
            
            binding.checkBoxSymptom.setOnClickListener {
                onSymptomClick(symptom)
            }
        }
    }
}