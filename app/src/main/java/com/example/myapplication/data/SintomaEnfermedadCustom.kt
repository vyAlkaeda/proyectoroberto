package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class SintomasEnfermedadesCustom(
    val sistemas: Map<String, SistemaSintomas>
)

data class SistemaSintomas(
    val sintomas: List<SintomaCustom>
)

data class SintomaCustom(
    val nombre: String,
    val enfermedades: List<String>,
    val edades: List<String>
) 