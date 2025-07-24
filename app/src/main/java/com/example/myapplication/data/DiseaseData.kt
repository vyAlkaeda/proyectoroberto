package com.example.myapplication.data

data class DiseaseData(
    val nombre: String,
    val descripcion: String? = null,
    val sintomas: List<String> = emptyList(),
    val edades: List<String> = emptyList(),
    val sistemas: List<String> = emptyList(),
    val tipo: String = "",
    val remedios: List<String> = emptyList(),
    val tratamientos: List<String> = emptyList(),
    val prevencion: List<String> = emptyList()
) 