package com.example.myapplication.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Disease(
    val name: String = "",
    val symptoms: List<String> = emptyList(),
    val description: String = "",
    val system: String = "",
    val affectedAges: List<String> = emptyList(),
    val severity: Severity = Severity.MODERATE,
    val treatment: String = "",
    val prevention: String = "",
    val transmissionType: TransmissionType = TransmissionType.UNKNOWN,
    val incubationPeriod: String = "",
    val mortality: String = "",
    val imageUrl: String = ""
) : Parcelable

@Parcelize
enum class Severity : Parcelable {
    LOW, MODERATE, HIGH, CRITICAL
}

@Parcelize
enum class TransmissionType : Parcelable {
    DIRECT_CONTACT,
    AIRBORNE,
    VECTOR_BORNE,
    CONTAMINATED_FOOD,
    SEXUAL,
    VERTICAL,
    UNKNOWN
}

@Parcelize
data class Symptom(
    val nombre: String = "",
    val enfermedades: List<String> = emptyList(),
    val edades: List<String> = emptyList(),
    val descripcion: String = "",
    val tipo: String = "",
    val sistema: String = "",
    val severity: Int = 1 // 1-5 scale
) : Parcelable

@Parcelize
data class DiagnosisResult(
    val disease: Disease,
    val matchingSymptoms: List<String>,
    val probability: Double,
    val recommendedActions: List<String>
) : Parcelable

@Parcelize
data class VeterinarySystem(
    val name: String = "",
    val description: String = "",
    val symptoms: List<Symptom> = emptyList(),
    val commonDiseases: List<String> = emptyList()
) : Parcelable 