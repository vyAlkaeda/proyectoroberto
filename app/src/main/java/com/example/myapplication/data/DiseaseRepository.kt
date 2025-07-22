package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.models.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class DiseaseRepository(private val context: Context) {
    
    companion object {
        private const val DISEASES_JSON_FILE = "enfermedades.json"
    }
    
    private val gson = Gson()
    private var cachedSystems: Map<String, VeterinarySystem>? = null
    private var cachedDiseases: Map<String, Disease>? = null
    
    suspend fun loadVeterinarySystems(): Map<String, VeterinarySystem> = withContext(Dispatchers.IO) {
        cachedSystems?.let { return@withContext it }
        
        try {
            val inputStream = context.assets.open(DISEASES_JSON_FILE)
            val reader = InputStreamReader(inputStream)
            val jsonData = gson.fromJson(reader, JsonObject::class.java)
            
            val systems = mutableMapOf<String, VeterinarySystem>()
            val sistemasJson = jsonData.getAsJsonObject("sistemas")
            
            sistemasJson?.entrySet()?.forEach { (systemName, systemData) ->
                val systemObj = systemData.asJsonObject
                val sintomasArray = systemObj.getAsJsonArray("sintomas")
                
                val symptoms = mutableListOf<Symptom>()
                sintomasArray?.forEach { sintomaElement ->
                    val sintomaObj = sintomaElement.asJsonObject
                    val symptom = Symptom(
                        nombre = sintomaObj.get("nombre")?.asString ?: "",
                        enfermedades = gson.fromJson(sintomaObj.get("enfermedades"), 
                            object : TypeToken<List<String>>() {}.type) ?: emptyList(),
                        edades = gson.fromJson(sintomaObj.get("edades"), 
                            object : TypeToken<List<String>>() {}.type) ?: emptyList(),
                        descripcion = sintomaObj.get("descripcion")?.asString ?: "",
                        tipo = sintomaObj.get("tipo")?.asString ?: "",
                        sistema = systemName
                    )
                    symptoms.add(symptom)
                }
                
                val commonDiseases = symptoms.flatMap { it.enfermedades }.distinct()
                
                systems[systemName] = VeterinarySystem(
                    name = systemName,
                    description = getSystemDescription(systemName),
                    symptoms = symptoms,
                    commonDiseases = commonDiseases
                )
            }
            
            cachedSystems = systems
            reader.close()
            systems
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    suspend fun loadDiseases(): Map<String, Disease> = withContext(Dispatchers.IO) {
        cachedDiseases?.let { return@withContext it }
        
        val systems = loadVeterinarySystems()
        val diseases = mutableMapOf<String, Disease>()
        
        systems.values.forEach { system ->
            system.symptoms.forEach { symptom ->
                symptom.enfermedades.forEach { diseaseName ->
                    if (!diseases.containsKey(diseaseName)) {
                        diseases[diseaseName] = Disease(
                            name = diseaseName,
                            symptoms = emptyList(),
                            description = getDiseaseDescription(diseaseName),
                            system = system.name,
                            affectedAges = symptom.edades,
                            severity = getDiseaseSeverity(diseaseName),
                            treatment = getDiseaseTeatemet(diseaseName),
                            prevention = getDiseasePrevention(diseaseName),
                            transmissionType = getDiseaseTransmissionType(diseaseName)
                        )
                    }
                }
            }
        }
        
        // Update diseases with their symptoms
        val updatedDiseases = diseases.mapValues { (diseaseName, disease) ->
            val diseaseSymptoms = systems.values.flatMap { system ->
                system.symptoms.filter { it.enfermedades.contains(diseaseName) }
                    .map { it.nombre }
            }
            disease.copy(symptoms = diseaseSymptoms)
        }
        
        cachedDiseases = updatedDiseases
        updatedDiseases
    }
    
    suspend fun getDiseasesBySymptoms(selectedSymptoms: List<String>): List<DiagnosisResult> = withContext(Dispatchers.IO) {
        val diseases = loadDiseases()
        val systems = loadVeterinarySystems()
        
        val results = mutableListOf<DiagnosisResult>()
        
        diseases.values.forEach { disease ->
            val matchingSymptoms = disease.symptoms.intersect(selectedSymptoms.toSet()).toList()
            
            if (matchingSymptoms.isNotEmpty()) {
                val probability = calculateProbability(matchingSymptoms, disease.symptoms, selectedSymptoms)
                val recommendations = generateRecommendations(disease, matchingSymptoms.size)
                
                results.add(DiagnosisResult(
                    disease = disease,
                    matchingSymptoms = matchingSymptoms,
                    probability = probability,
                    recommendedActions = recommendations
                ))
            }
        }
        
        results.sortedByDescending { it.probability }
    }
    
    suspend fun getSymptomsByAge(ageGroup: String): List<Symptom> = withContext(Dispatchers.IO) {
        val systems = loadVeterinarySystems()
        val allSymptoms = systems.values.flatMap { it.symptoms }
        
        if (ageGroup == "Todas") {
            allSymptoms
        } else {
            allSymptoms.filter { it.edades.contains(ageGroup) }
        }
    }
    
    suspend fun getSymptomsBySystem(systemName: String, ageGroup: String? = null): List<Symptom> = withContext(Dispatchers.IO) {
        val systems = loadVeterinarySystems()
        val system = systems[systemName] ?: return@withContext emptyList()
        
        if (ageGroup == null || ageGroup == "Todas") {
            system.symptoms
        } else {
            system.symptoms.filter { it.edades.contains(ageGroup) }
        }
    }
    
    private fun calculateProbability(matchingSymptoms: List<String>, diseaseSymptoms: List<String>, selectedSymptoms: List<String>): Double {
        val precision = matchingSymptoms.size.toDouble() / selectedSymptoms.size
        val recall = matchingSymptoms.size.toDouble() / diseaseSymptoms.size
        
        return if (precision + recall > 0) {
            2 * (precision * recall) / (precision + recall) * 100
        } else {
            0.0
        }
    }
    
    private fun generateRecommendations(disease: Disease, matchingCount: Int): List<String> {
        val recommendations = mutableListOf<String>()
        
        when {
            matchingCount >= 3 -> {
                recommendations.add("Consulta inmediata con veterinario")
                recommendations.add("Aislar al animal afectado")
                if (disease.treatment.isNotEmpty()) {
                    recommendations.add("Tratamiento: ${disease.treatment}")
                }
            }
            matchingCount >= 2 -> {
                recommendations.add("Monitoreo constante del animal")
                recommendations.add("Considerar consulta veterinaria")
                recommendations.add("Aplicar medidas de bioseguridad")
            }
            else -> {
                recommendations.add("Observación del animal")
                recommendations.add("Registrar síntomas adicionales")
                recommendations.add("Mantener medidas preventivas")
            }
        }
        
        if (disease.prevention.isNotEmpty()) {
            recommendations.add("Prevención: ${disease.prevention}")
        }
        
        return recommendations
    }
    
    private fun getSystemDescription(systemName: String): String {
        return when (systemName) {
            "Sistema Respiratorio" -> "Sistema encargado de la respiración y intercambio gaseoso en los cerdos"
            "Sistema Digestivo" -> "Sistema responsable de la digestión y absorción de nutrientes"
            "Sistema Nervioso" -> "Sistema que controla las funciones corporales y el comportamiento"
            "Sistema Reproductivo" -> "Sistema responsable de la reproducción"
            "Sistema Circulatorio" -> "Sistema encargado del transporte de sangre y nutrientes"
            "Sistema Locomotor" -> "Sistema responsable del movimiento y soporte corporal"
            else -> "Sistema veterinario especializado"
        }
    }
    
    private fun getDiseaseDescription(diseaseName: String): String {
        // Esta función podría expandirse con descripciones específicas de enfermedades
        return "Enfermedad que afecta a los cerdos y requiere atención veterinaria."
    }
    
    private fun getDiseaseSeverity(diseaseName: String): Severity {
        // Clasificación basada en el nombre de la enfermedad
        return when {
            diseaseName.contains("SEPTICEMIA", true) || 
            diseaseName.contains("HEMORRAGICA", true) -> Severity.CRITICAL
            diseaseName.contains("PNEUMONIA", true) || 
            diseaseName.contains("GASTROENTERITIS", true) -> Severity.HIGH
            diseaseName.contains("DERMATITIS", true) -> Severity.MODERATE
            else -> Severity.MODERATE
        }
    }
    
    private fun getDiseaseTeatemet(diseaseName: String): String {
        return "Consulte con un veterinario para el tratamiento adecuado de $diseaseName"
    }
    
    private fun getDiseasePrevention(diseaseName: String): String {
        return "Mantener buenas prácticas de bioseguridad y manejo sanitario"
    }
    
    private fun getDiseaseTransmissionType(diseaseName: String): TransmissionType {
        return when {
            diseaseName.contains("INFLUENZA", true) -> TransmissionType.AIRBORNE
            diseaseName.contains("SALMONELA", true) -> TransmissionType.CONTAMINATED_FOOD
            diseaseName.contains("SARNA", true) -> TransmissionType.DIRECT_CONTACT
            else -> TransmissionType.UNKNOWN
        }
    }
}