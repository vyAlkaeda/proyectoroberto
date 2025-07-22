package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityModernDiagnosisResultsBinding
import com.example.myapplication.databinding.ItemDiagnosisResultBinding
import com.example.myapplication.models.DiagnosisResult
import com.example.myapplication.models.Severity
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ModernDiagnosisResultsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityModernDiagnosisResultsBinding
    private lateinit var resultsAdapter: DiagnosisResultsAdapter
    
    private var diagnosisResults: List<DiagnosisResult> = emptyList()
    private var selectedAge: String = ""
    private var diagnosisType: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModernDiagnosisResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Obtener datos del intent
        diagnosisResults = intent.getParcelableArrayListExtra("DIAGNOSIS_RESULTS") ?: emptyList()
        selectedAge = intent.getStringExtra("SELECTED_AGE") ?: ""
        diagnosisType = intent.getStringExtra("DIAGNOSIS_TYPE") ?: ""
        
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        displayResults()
        applyTheme()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Resultados del Diagnóstico"
    }
    
    private fun setupRecyclerView() {
        resultsAdapter = DiagnosisResultsAdapter { result ->
            showDiseaseDetails(result)
        }
        
        binding.recyclerViewResults.apply {
            layoutManager = LinearLayoutManager(this@ModernDiagnosisResultsActivity)
            adapter = resultsAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnNewDiagnosis.setOnClickListener {
            finish()
        }
        
        binding.btnShareResults.setOnClickListener {
            shareResults()
        }
        
        binding.fabStatistics.setOnClickListener {
            showStatistics()
        }
    }
    
    private fun displayResults() {
        resultsAdapter.submitList(diagnosisResults)
        
        // Mostrar información general
        binding.tvResultsCount.text = "${diagnosisResults.size} enfermedades encontradas"
        binding.tvSelectedAge.text = if (selectedAge == "Todas") "Todas las edades" else selectedAge
        
        // Mostrar estadísticas rápidas
        val highProbability = diagnosisResults.count { it.probability >= 70.0 }
        val mediumProbability = diagnosisResults.count { it.probability >= 40.0 && it.probability < 70.0 }
        val lowProbability = diagnosisResults.count { it.probability < 40.0 }
        
        binding.tvHighProbability.text = "$highProbability"
        binding.tvMediumProbability.text = "$mediumProbability"
        binding.tvLowProbability.text = "$lowProbability"
        
        // Mostrar mensaje según los resultados
        when {
            diagnosisResults.isEmpty() -> {
                binding.tvResultMessage.text = "No se encontraron enfermedades con los síntomas seleccionados"
                binding.cardRecommendations.visibility = View.GONE
            }
            highProbability > 0 -> {
                binding.tvResultMessage.text = "Se encontraron enfermedades con alta probabilidad. Consulta veterinaria recomendada."
                binding.tvRecommendations.text = getHighPriorityRecommendations()
            }
            mediumProbability > 0 -> {
                binding.tvResultMessage.text = "Se encontraron enfermedades con probabilidad moderada. Monitoreo recomendado."
                binding.tvRecommendations.text = getMediumPriorityRecommendations()
            }
            else -> {
                binding.tvResultMessage.text = "Las enfermedades encontradas tienen baja probabilidad."
                binding.tvRecommendations.text = getLowPriorityRecommendations()
            }
        }
        
        // Mostrar síntomas únicos encontrados
        val uniqueSymptoms = diagnosisResults.flatMap { it.matchingSymptoms }.distinct()
        binding.chipGroupSymptoms.removeAllViews()
        uniqueSymptoms.forEach { symptom ->
            val chip = Chip(this).apply {
                text = symptom
                isClickable = false
            }
            binding.chipGroupSymptoms.addView(chip)
        }
    }
    
    private fun showDiseaseDetails(result: DiagnosisResult) {
        val message = """
            Enfermedad: ${result.disease.name}
            
            Probabilidad: ${"%.1f".format(result.probability)}%
            
            Síntomas coincidentes:
            ${result.matchingSymptoms.joinToString("\n• ", "• ")}
            
            Descripción:
            ${result.disease.description}
            
            Sistema afectado: ${result.disease.system}
            
            Severidad: ${getSeverityText(result.disease.severity)}
            
            Recomendaciones:
            ${result.recommendedActions.joinToString("\n• ", "• ")}
        """.trimIndent()
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Detalles de la Enfermedad")
            .setMessage(message)
            .setPositiveButton("Cerrar", null)
            .setNeutralButton("Compartir") { _, _ ->
                shareDisease(result)
            }
            .show()
    }
    
    private fun showStatistics() {
        val avgProbability = if (diagnosisResults.isNotEmpty()) {
            diagnosisResults.map { it.probability }.average()
        } else 0.0
        
        val totalSymptoms = diagnosisResults.flatMap { it.matchingSymptoms }.distinct().size
        val systemsAffected = diagnosisResults.map { it.disease.system }.distinct().size
        
        val message = """
            Estadísticas del Diagnóstico:
            
            • Enfermedades encontradas: ${diagnosisResults.size}
            • Síntomas únicos analizados: $totalSymptoms
            • Sistemas afectados: $systemsAffected
            • Probabilidad promedio: ${"%.1f".format(avgProbability)}%
            
            Distribución por probabilidad:
            • Alta (≥70%): ${diagnosisResults.count { it.probability >= 70.0 }}
            • Media (40-69%): ${diagnosisResults.count { it.probability >= 40.0 && it.probability < 70.0 }}
            • Baja (<40%): ${diagnosisResults.count { it.probability < 40.0 }}
            
            Severidad de enfermedades:
            • Crítica: ${diagnosisResults.count { it.disease.severity == Severity.CRITICAL }}
            • Alta: ${diagnosisResults.count { it.disease.severity == Severity.HIGH }}
            • Moderada: ${diagnosisResults.count { it.disease.severity == Severity.MODERATE }}
            • Baja: ${diagnosisResults.count { it.disease.severity == Severity.LOW }}
        """.trimIndent()
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Estadísticas Detalladas")
            .setMessage(message)
            .setPositiveButton("Cerrar", null)
            .show()
    }
    
    private fun shareResults() {
        val shareText = buildString {
            appendLine("Resultados de Diagnóstico Veterinario")
            appendLine("========================================")
            appendLine("Edad: ${if (selectedAge == "Todas") "Todas las edades" else selectedAge}")
            appendLine("Tipo: ${if (diagnosisType == "NECROPSIA") "Necropsia" else "Síntomas"}")
            appendLine("Enfermedades encontradas: ${diagnosisResults.size}")
            appendLine()
            
            diagnosisResults.forEachIndexed { index, result ->
                appendLine("${index + 1}. ${result.disease.name}")
                appendLine("   Probabilidad: ${"%.1f".format(result.probability)}%")
                appendLine("   Síntomas: ${result.matchingSymptoms.joinToString(", ")}")
                appendLine()
            }
            
            appendLine("Generado por App de Diagnóstico Veterinario Porcino")
        }
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Resultados de Diagnóstico Veterinario")
        }
        
        startActivity(Intent.createChooser(shareIntent, "Compartir resultados"))
    }
    
    private fun shareDisease(result: DiagnosisResult) {
        val shareText = """
            Enfermedad: ${result.disease.name}
            Probabilidad: ${"%.1f".format(result.probability)}%
            Síntomas: ${result.matchingSymptoms.joinToString(", ")}
            Sistema: ${result.disease.system}
            
            Recomendaciones:
            ${result.recommendedActions.joinToString("\n• ", "• ")}
        """.trimIndent()
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Diagnóstico: ${result.disease.name}")
        }
        
        startActivity(Intent.createChooser(shareIntent, "Compartir diagnóstico"))
    }
    
    private fun getHighPriorityRecommendations(): String {
        return """
            • Consulta veterinaria inmediata
            • Aislar al animal afectado
            • Implementar medidas de bioseguridad estrictas
            • Documentar todos los síntomas
            • Monitorear otros animales del grupo
        """.trimIndent()
    }
    
    private fun getMediumPriorityRecommendations(): String {
        return """
            • Monitoreo constante del animal
            • Considerar consulta veterinaria
            • Aplicar medidas preventivas
            • Observar evolución de síntomas
            • Revisar condiciones ambientales
        """.trimIndent()
    }
    
    private fun getLowPriorityRecommendations(): String {
        return """
            • Observación continua del animal
            • Registrar nuevos síntomas si aparecen
            • Mantener medidas de higiene
            • Considerar factores ambientales
            • Evaluar la necesidad de seguimiento
        """.trimIndent()
    }
    
    private fun getSeverityText(severity: Severity): String {
        return when (severity) {
            Severity.CRITICAL -> "Crítica"
            Severity.HIGH -> "Alta"
            Severity.MODERATE -> "Moderada"
            Severity.LOW -> "Baja"
        }
    }
    
    private fun applyTheme() {
        val primaryColor = if (diagnosisType == "NECROPSIA") {
            ContextCompat.getColor(this, R.color.necro_primary)
        } else {
            ContextCompat.getColor(this, R.color.primary)
        }
        
        binding.toolbar.setBackgroundColor(primaryColor)
        binding.fabStatistics.backgroundTintList = ContextCompat.getColorStateList(this,
            if (diagnosisType == "NECROPSIA") R.color.necro_primary else R.color.primary
        )
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

// Adapter para mostrar los resultados del diagnóstico
class DiagnosisResultsAdapter(
    private val onResultClick: (DiagnosisResult) -> Unit
) : RecyclerView.Adapter<DiagnosisResultsAdapter.ResultViewHolder>() {
    
    private var results: List<DiagnosisResult> = emptyList()
    
    fun submitList(newResults: List<DiagnosisResult>) {
        results = newResults
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemDiagnosisResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ResultViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(results[position])
    }
    
    override fun getItemCount(): Int = results.size
    
    inner class ResultViewHolder(
        private val binding: ItemDiagnosisResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(result: DiagnosisResult) {
            binding.tvDiseaseName.text = result.disease.name
            binding.tvProbability.text = "${"%.1f".format(result.probability)}%"
            binding.tvSystem.text = result.disease.system
            binding.tvMatchingSymptoms.text = "${result.matchingSymptoms.size} síntomas coincidentes"
            
            // Configurar color según probabilidad
            val (backgroundColor, textColor) = when {
                result.probability >= 70.0 -> Pair(R.color.error_light, R.color.error_dark)
                result.probability >= 40.0 -> Pair(R.color.warning_light, R.color.warning_dark)
                else -> Pair(R.color.info_light, R.color.info_dark)
            }
            
            binding.cardResult.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, backgroundColor))
            binding.tvProbability.setTextColor(ContextCompat.getColor(binding.root.context, textColor))
            
            // Configurar indicador de severidad
            val severityColor = when (result.disease.severity) {
                Severity.CRITICAL -> R.color.severity_critical
                Severity.HIGH -> R.color.severity_high
                Severity.MODERATE -> R.color.severity_medium
                Severity.LOW -> R.color.severity_low
            }
            binding.viewSeverityIndicator.setBackgroundColor(
                ContextCompat.getColor(binding.root.context, severityColor)
            )
            
            // Configurar chip de severidad
            binding.chipSeverity.text = when (result.disease.severity) {
                Severity.CRITICAL -> "Crítica"
                Severity.HIGH -> "Alta"
                Severity.MODERATE -> "Moderada"
                Severity.LOW -> "Baja"
            }
            
            binding.chipSeverity.setChipBackgroundColorResource(severityColor)
            
            // Mostrar síntomas coincidentes
            binding.chipGroupSymptoms.removeAllViews()
            result.matchingSymptoms.take(3).forEach { symptom ->
                val chip = Chip(binding.root.context).apply {
                    text = symptom
                    isClickable = false
                    textSize = 10f
                }
                binding.chipGroupSymptoms.addView(chip)
            }
            
            if (result.matchingSymptoms.size > 3) {
                val moreChip = Chip(binding.root.context).apply {
                    text = "+${result.matchingSymptoms.size - 3} más"
                    isClickable = false
                    textSize = 10f
                }
                binding.chipGroupSymptoms.addView(moreChip)
            }
            
            binding.root.setOnClickListener {
                onResultClick(result)
            }
        }
    }
}