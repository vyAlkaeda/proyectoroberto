package com.example.myapplication.data

data class SymptomDiseaseMapping(
    val symptomToDiseases: Map<String, List<String>> = emptyMap()
)

data class DiseaseInfo(
    val name: String,
    val matchingSymptoms: List<String>,
    val score: Int,
    val probability: Double
)

data class DiagnosticResult(
    val diseases: List<DiseaseInfo>,
    val totalSymptoms: Int,
    val confidence: String
)