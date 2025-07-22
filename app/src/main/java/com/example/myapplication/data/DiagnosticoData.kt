package com.example.myapplication.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiagnosticoData(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val fechaHora: Timestamp = Timestamp.now(),
    
    // Pregunta inicial
    val animalVivo: String = "",
    
    // Información epidemiológica
    val edadCerdos: String = "",
    val numeroAfectados: String = "",
    val incrementoMortalidad: String = "",
    val areaProduccion: String = "",
    
    // Síntomas seleccionados
    val sintomasNerviosos: List<String> = emptyList(),
    val sintomasMusculoesqueleticos: List<String> = emptyList(),
    val sintomasDigestivos: List<String> = emptyList(),
    val sintomasReproductivos: List<String> = emptyList(),
    val sintomasTegumentarios: List<String> = emptyList(),
    val sintomasRespiratorios: List<String> = emptyList(),
    
    // Diagnóstico generado
    val diagnosticoGenerado: String = "",
    val enfermedadesPosibles: List<String> = emptyList(),
    val recomendaciones: List<String> = emptyList(),
    
    // Nuevos campos para diagnóstico optimizado
    val confianzaDiagnostico: String = "",
    val probabilidadMaxima: Double = 0.0
) : Parcelable 