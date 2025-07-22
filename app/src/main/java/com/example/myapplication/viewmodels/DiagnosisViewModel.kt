package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.DiseaseRepository
import com.example.myapplication.models.*
import kotlinx.coroutines.launch

class DiagnosisViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = DiseaseRepository(application)
    
    // LiveData para síntomas disponibles
    private val _availableSymptoms = MutableLiveData<List<Symptom>>()
    val availableSymptoms: LiveData<List<Symptom>> = _availableSymptoms
    
    // LiveData para síntomas seleccionados
    private val _selectedSymptoms = MutableLiveData<MutableList<String>>(mutableListOf())
    val selectedSymptoms: LiveData<MutableList<String>> = _selectedSymptoms
    
    // LiveData para resultados del diagnóstico
    private val _diagnosisResults = MutableLiveData<List<DiagnosisResult>>()
    val diagnosisResults: LiveData<List<DiagnosisResult>> = _diagnosisResults
    
    // LiveData para sistemas veterinarios
    private val _veterinarySystems = MutableLiveData<Map<String, VeterinarySystem>>()
    val veterinarySystems: LiveData<Map<String, VeterinarySystem>> = _veterinarySystems
    
    // LiveData para estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // LiveData para errores
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    // Variables de estado
    private var currentAgeGroup: String = "Todas"
    private var currentSystem: String? = null
    
    init {
        loadVeterinarySystems()
    }
    
    fun loadVeterinarySystems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val systems = repository.loadVeterinarySystems()
                _veterinarySystems.value = systems
                loadSymptomsByAge(currentAgeGroup)
            } catch (e: Exception) {
                _error.value = "Error al cargar sistemas veterinarios: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadSymptomsByAge(ageGroup: String) {
        currentAgeGroup = ageGroup
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val symptoms = repository.getSymptomsByAge(ageGroup)
                _availableSymptoms.value = symptoms
            } catch (e: Exception) {
                _error.value = "Error al cargar síntomas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadSymptomsBySystem(systemName: String, ageGroup: String? = null) {
        currentSystem = systemName
        val targetAgeGroup = ageGroup ?: currentAgeGroup
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val symptoms = repository.getSymptomsBySystem(systemName, targetAgeGroup)
                _availableSymptoms.value = symptoms
            } catch (e: Exception) {
                _error.value = "Error al cargar síntomas del sistema: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleSymptom(symptomName: String) {
        val currentList = _selectedSymptoms.value ?: mutableListOf()
        if (currentList.contains(symptomName)) {
            currentList.remove(symptomName)
        } else {
            currentList.add(symptomName)
        }
        _selectedSymptoms.value = currentList
    }
    
    fun clearSelectedSymptoms() {
        _selectedSymptoms.value = mutableListOf()
    }
    
    fun performDiagnosis() {
        val symptoms = _selectedSymptoms.value ?: emptyList()
        if (symptoms.isEmpty()) {
            _error.value = "Selecciona al menos un síntoma para realizar el diagnóstico"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = repository.getDiseasesBySymptoms(symptoms)
                _diagnosisResults.value = results
            } catch (e: Exception) {
                _error.value = "Error al realizar diagnóstico: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun getSymptomsBySystem(): Map<String, List<Symptom>> {
        val symptoms = _availableSymptoms.value ?: emptyList()
        return symptoms.groupBy { it.sistema }
    }
    
    fun getSelectedSymptomsCount(): Int {
        return _selectedSymptoms.value?.size ?: 0
    }
    
    fun isSymptomSelected(symptomName: String): Boolean {
        return _selectedSymptoms.value?.contains(symptomName) ?: false
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun setCurrentAgeGroup(ageGroup: String) {
        if (currentAgeGroup != ageGroup) {
            currentAgeGroup = ageGroup
            if (currentSystem != null) {
                loadSymptomsBySystem(currentSystem!!, ageGroup)
            } else {
                loadSymptomsByAge(ageGroup)
            }
        }
    }
    
    fun getCurrentAgeGroup(): String = currentAgeGroup
    
    fun getCurrentSystem(): String? = currentSystem
    
    // Función para obtener recomendaciones generales basadas en los síntomas seleccionados
    fun getGeneralRecommendations(): List<String> {
        val symptomsCount = getSelectedSymptomsCount()
        val recommendations = mutableListOf<String>()
        
        when {
            symptomsCount >= 5 -> {
                recommendations.addAll(listOf(
                    "Alta probabilidad de enfermedad - Consulta veterinaria inmediata",
                    "Aislar al animal de otros cerdos",
                    "Implementar medidas de bioseguridad estrictas",
                    "Documentar todos los síntomas observados"
                ))
            }
            symptomsCount >= 3 -> {
                recommendations.addAll(listOf(
                    "Monitoreo constante requerido",
                    "Considerar consulta veterinaria",
                    "Aplicar medidas preventivas",
                    "Observar evolución de síntomas"
                ))
            }
            symptomsCount >= 1 -> {
                recommendations.addAll(listOf(
                    "Observación continua del animal",
                    "Registrar nuevos síntomas si aparecen",
                    "Mantener medidas de higiene",
                    "Considerar factores ambientales"
                ))
            }
        }
        
        return recommendations
    }
    
    // Función para filtrar síntomas por severidad
    fun filterSymptomsBySeverity(minSeverity: Int): List<Symptom> {
        return _availableSymptoms.value?.filter { it.severity >= minSeverity } ?: emptyList()
    }
    
    // Función para obtener estadísticas de diagnóstico
    fun getDiagnosisStats(): DiagnosisStats {
        val results = _diagnosisResults.value ?: emptyList()
        val selectedCount = getSelectedSymptomsCount()
        
        return DiagnosisStats(
            totalDiseasesFound = results.size,
            highProbabilityDiseases = results.count { it.probability >= 70.0 },
            mediumProbabilityDiseases = results.count { it.probability >= 40.0 && it.probability < 70.0 },
            lowProbabilityDiseases = results.count { it.probability < 40.0 },
            symptomsAnalyzed = selectedCount,
            averageProbability = if (results.isNotEmpty()) results.map { it.probability }.average() else 0.0
        )
    }
}

data class DiagnosisStats(
    val totalDiseasesFound: Int,
    val highProbabilityDiseases: Int,
    val mediumProbabilityDiseases: Int,
    val lowProbabilityDiseases: Int,
    val symptomsAnalyzed: Int,
    val averageProbability: Double
)