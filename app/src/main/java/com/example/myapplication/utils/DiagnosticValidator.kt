package com.example.myapplication.utils

import android.content.Context
import android.util.Log

class DiagnosticValidator(private val context: Context) {
    
    private val diagnosticManager = DiagnosticManager(context)
    
    fun validateDiagnosticSystem(): ValidationResult {
        val results = mutableListOf<String>()
        var hasErrors = false
        
        try {
            // Test 1: Verificar carga de datos
            val allSymptoms = diagnosticManager.getAllSymptoms()
            val allDiseases = diagnosticManager.getAllDiseases()
            
            if (allSymptoms.isEmpty()) {
                results.add("❌ Error: No se pudieron cargar los síntomas")
                hasErrors = true
            } else {
                results.add("✅ Síntomas cargados: ${allSymptoms.size}")
            }
            
            if (allDiseases.isEmpty()) {
                results.add("❌ Error: No se pudieron cargar las enfermedades")
                hasErrors = true
            } else {
                results.add("✅ Enfermedades cargadas: ${allDiseases.size}")
            }
            
            // Test 2: Diagnóstico con síntomas comunes
            val testSymptoms = listOf("FIEBRE", "TOS", "DIARREA")
            val diagnosis = diagnosticManager.performDiagnosis(
                symptoms = testSymptoms,
                age = "1-4 semanas",
                area = "Maternidad",
                mortality = "Más del 5%"
            )
            
            if (diagnosis.diseases.isNotEmpty()) {
                results.add("✅ Diagnóstico generado correctamente")
                results.add("   - Enfermedades encontradas: ${diagnosis.diseases.size}")
                results.add("   - Confianza: ${diagnosis.confidence}")
                diagnosis.diseases.take(3).forEach { disease ->
                    results.add("   - ${disease.name}: ${disease.probability}%")
                }
            } else {
                results.add("⚠️ Advertencia: No se encontraron enfermedades para síntomas de prueba")
            }
            
            // Test 3: Búsqueda específica de síntomas
            val feverdiseases = diagnosticManager.getDiseasesForSymptom("FIEBRE")
            if (feverdiseases.isNotEmpty()) {
                results.add("✅ Búsqueda de síntomas funciona: FIEBRE -> ${feverdiseases.size} enfermedades")
            } else {
                results.add("⚠️ Advertencia: No se encontraron enfermedades para FIEBRE")
            }
            
            // Test 4: Diagnóstico sin síntomas
            val emptyDiagnosis = diagnosticManager.performDiagnosis(emptyList())
            if (emptyDiagnosis.confidence == "INSUFICIENTE") {
                results.add("✅ Manejo correcto de diagnóstico sin síntomas")
            } else {
                results.add("⚠️ Advertencia: Manejo incorrecto de diagnóstico vacío")
            }
            
        } catch (e: Exception) {
            results.add("❌ Error crítico en validación: ${e.message}")
            hasErrors = true
            Log.e("DiagnosticValidator", "Error en validación", e)
        }
        
        return ValidationResult(
            isValid = !hasErrors,
            messages = results,
            totalSymptoms = diagnosticManager.getAllSymptoms().size,
            totalDiseases = diagnosticManager.getAllDiseases().size
        )
    }
    
    fun runPerformanceTest(): PerformanceResult {
        val iterations = 100
        val startTime = System.currentTimeMillis()
        
        repeat(iterations) {
            diagnosticManager.performDiagnosis(
                symptoms = listOf("FIEBRE", "TOS", "DIARREA", "ANOREXIA", "LETARGO"),
                age = "5-10 semanas",
                area = "Destete"
            )
        }
        
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime
        val avgTime = totalTime / iterations
        
        return PerformanceResult(
            iterations = iterations,
            totalTimeMs = totalTime,
            averageTimeMs = avgTime,
            isAcceptable = avgTime < 100 // Menos de 100ms por diagnóstico
        )
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val messages: List<String>,
    val totalSymptoms: Int,
    val totalDiseases: Int
)

data class PerformanceResult(
    val iterations: Int,
    val totalTimeMs: Long,
    val averageTimeMs: Long,
    val isAcceptable: Boolean
)