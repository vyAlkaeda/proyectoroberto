package com.example.myapplication.utils

import android.content.Context
import com.example.myapplication.data.DiseaseInfo
import com.example.myapplication.data.DiagnosticResult
import com.example.myapplication.data.SymptomDiseaseMapping
import com.google.gson.Gson
import java.io.IOException
import kotlin.math.roundToInt

class DiagnosticManager(private val context: Context) {
    
    private var symptomMapping: SymptomDiseaseMapping? = null
    private val gson = Gson()
    
    init {
        loadSymptomMapping()
    }
    
    private fun loadSymptomMapping() {
        try {
            val jsonString = context.assets.open("symptom_disease_mapping_complete.json")
                .bufferedReader().use { it.readText() }
            symptomMapping = gson.fromJson(jsonString, SymptomDiseaseMapping::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
            // Fallback to empty mapping
            symptomMapping = SymptomDiseaseMapping()
        }
    }
    
    fun performDiagnosis(
        symptoms: List<String>,
        age: String = "",
        area: String = "",
        mortality: String = ""
    ): DiagnosticResult {
        val mapping = symptomMapping?.symptomToDiseases ?: emptyMap()
        val diseaseScores = mutableMapOf<String, MutableList<String>>()
        
        // Normalizar síntomas a mayúsculas para coincidencias
        val normalizedSymptoms = symptoms.map { it.uppercase().trim() }
        
        // Buscar coincidencias exactas y parciales
        for (symptom in normalizedSymptoms) {
            // Búsqueda exacta
            mapping[symptom]?.forEach { disease ->
                diseaseScores.getOrPut(disease) { mutableListOf() }.add(symptom)
            }
            
            // Búsqueda parcial - síntomas que contengan palabras clave
            mapping.entries.forEach { (mappedSymptom, diseases) ->
                if (isSymptomMatch(symptom, mappedSymptom)) {
                    diseases.forEach { disease ->
                        val existingSymptoms = diseaseScores.getOrPut(disease) { mutableListOf() }
                        if (!existingSymptoms.contains(symptom)) {
                            existingSymptoms.add(symptom)
                        }
                    }
                }
            }
        }
        
        // Aplicar modificadores por edad y área
        val modifiedScores = applyContextModifiers(diseaseScores, age, area, mortality)
        
        // Convertir a DiseaseInfo y calcular probabilidades
        val diseaseInfoList = modifiedScores.map { (disease, matchingSymptoms) ->
            val score = matchingSymptoms.size
            val probability = calculateProbability(score, normalizedSymptoms.size)
            
            DiseaseInfo(
                name = disease,
                matchingSymptoms = matchingSymptoms,
                score = score,
                probability = probability
            )
        }.sortedByDescending { it.score }
        
        // Determinar nivel de confianza
        val confidence = when {
            diseaseInfoList.isEmpty() -> "INSUFICIENTE"
            diseaseInfoList.first().score >= 3 -> "ALTA"
            diseaseInfoList.first().score >= 2 -> "MEDIA"
            else -> "BAJA"
        }
        
        return DiagnosticResult(
            diseases = diseaseInfoList.take(5), // Top 5 enfermedades
            totalSymptoms = normalizedSymptoms.size,
            confidence = confidence
        )
    }
    
    private fun isSymptomMatch(userSymptom: String, mappedSymptom: String): Boolean {
        val keywords = listOf(
            "FIEBRE", "DIARREA", "VOMITO", "TOS", "DOLOR", "ANOREXIA", 
            "CONVULSIONES", "COJERA", "ABORTO", "ANEMIA", "ICTERICIA",
            "DESHIDRATACION", "LETARGO", "DISNEA", "EDEMA", "NECROSIS",
            "HEMORRAGIA", "INFLAMACION", "ULCERAS", "DERMATITIS"
        )
        
        // Verificar si contiene palabras clave importantes
        for (keyword in keywords) {
            if (userSymptom.contains(keyword) && mappedSymptom.contains(keyword)) {
                return true
            }
        }
        
        // Verificar coincidencia parcial de palabras
        val userWords = userSymptom.split(" ", "-", "(", ")")
        val mappedWords = mappedSymptom.split(" ", "-", "(", ")")
        
        val commonWords = userWords.intersect(mappedWords.toSet())
        return commonWords.size >= 2 || 
               (commonWords.size == 1 && commonWords.first().length > 4)
    }
    
    private fun applyContextModifiers(
        diseaseScores: MutableMap<String, MutableList<String>>,
        age: String,
        area: String,
        mortality: String
    ): Map<String, List<String>> {
        val modifiedScores = diseaseScores.toMutableMap()
        
        // Modificadores por edad
        when (age) {
            "1-4 semanas" -> {
                // Lechones más susceptibles a ciertas enfermedades
                addBonusSymptom(modifiedScores, "Estreptococosis", "EDAD_LECHON")
                addBonusSymptom(modifiedScores, "PRRS", "EDAD_LECHON")
                addBonusSymptom(modifiedScores, "Clostridiosis", "EDAD_LECHON")
            }
            "5-10 semanas" -> {
                addBonusSymptom(modifiedScores, "Circovirus", "EDAD_DESTETE")
                addBonusSymptom(modifiedScores, "PRRS", "EDAD_DESTETE")
            }
        }
        
        // Modificadores por área
        when (area) {
            "Maternidad" -> {
                addBonusSymptom(modifiedScores, "PRRS", "AREA_MATERNIDAD")
                addBonusSymptom(modifiedScores, "Erisipela", "AREA_MATERNIDAD")
            }
            "Gestación" -> {
                addBonusSymptom(modifiedScores, "PRRS", "AREA_GESTACION")
                addBonusSymptom(modifiedScores, "Brucelosis", "AREA_GESTACION")
            }
        }
        
        // Modificadores por mortalidad
        if (mortality == "Más del 5%") {
            addBonusSymptom(modifiedScores, "Clostridiosis", "ALTA_MORTALIDAD")
            addBonusSymptom(modifiedScores, "Estreptococosis", "ALTA_MORTALIDAD")
            addBonusSymptom(modifiedScores, "PRRS", "ALTA_MORTALIDAD")
        }
        
        return modifiedScores
    }
    
    private fun addBonusSymptom(
        scores: MutableMap<String, MutableList<String>>,
        disease: String,
        bonusType: String
    ) {
        scores.getOrPut(disease) { mutableListOf() }.add(bonusType)
    }
    
    private fun calculateProbability(score: Int, totalSymptoms: Int): Double {
        if (totalSymptoms == 0) return 0.0
        
        val basePercentage = (score.toDouble() / totalSymptoms * 100)
        val adjustedPercentage = when {
            score >= 4 -> (basePercentage * 1.2).coerceAtMost(95.0)
            score >= 3 -> (basePercentage * 1.1).coerceAtMost(85.0)
            score >= 2 -> basePercentage.coerceAtMost(75.0)
            else -> (basePercentage * 0.8).coerceAtMost(50.0)
        }
        
        return (adjustedPercentage * 100).roundToInt() / 100.0
    }
    
    fun getDiseasesForSymptom(symptom: String): List<String> {
        val mapping = symptomMapping?.symptomToDiseases ?: emptyMap()
        return mapping[symptom.uppercase().trim()] ?: emptyList()
    }
    
    fun getAllSymptoms(): List<String> {
        val mapping = symptomMapping?.symptomToDiseases ?: emptyMap()
        return mapping.keys.toList().sorted()
    }
    
    fun getAllDiseases(): List<String> {
        val mapping = symptomMapping?.symptomToDiseases ?: emptyMap()
        return mapping.values.flatten().distinct().sorted()
    }
}