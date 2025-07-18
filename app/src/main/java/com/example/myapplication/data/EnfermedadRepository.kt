package com.example.myapplication.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Nueva estructura de datos para el JSON
data class SintomaEnfermedad(
    val sintoma: String,
    val enfermedades: List<String>,
    val descripcion: String,
    val edades: List<String>? = null // Nuevo campo opcional para edades
)

fun cargarSintomasEnfermedadesDesdeJson(context: Context): List<SintomaEnfermedad> {
    val json = context.assets.open("sintomas_enfermedades_custom.json").bufferedReader().use { it.readText() }
    val gson = Gson()
    val type = object : TypeToken<List<SintomaEnfermedad>>() {}.type
    return gson.fromJson(json, type)
}

fun filtrarSintomasPorEdad(sintomas: List<SintomaEnfermedad>, edad: String): List<SintomaEnfermedad> {
    return sintomas.filter { it.edades?.contains(edad) ?: (edad == "Todas las edades") }
}

fun cargarSintomasEnfermedadesCustomDesdeJson(context: android.content.Context): SintomasEnfermedadesCustom {
    return try {
        val inputStream = context.assets.open("enfermedades.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        
        val gson = Gson()
        gson.fromJson(json, SintomasEnfermedadesCustom::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        SintomasEnfermedadesCustom(emptyMap())
    }
}

fun obtenerSistemasDisponiblesParaEdad(sintomasData: SintomasEnfermedadesCustom, edad: String): List<String> {
    val sistemas = mutableSetOf<String>()
    
    sintomasData.sistemas.forEach { (nombreSistema, sistema) ->
        sistema.sintomas.forEach { sintoma ->
            if (edad == "Todas las edades" || sintoma.edades.contains(mapearEdadAppAJson(edad))) {
                sistemas.add(nombreSistema)
            }
        }
    }
    
    return sistemas.sorted()
}

fun obtenerSintomasParaSistemaYEdad(sintomasData: SintomasEnfermedadesCustom, sistema: String, edad: String): List<SintomaCustom> {
    val sistemaData = sintomasData.sistemas[sistema] ?: return emptyList()
    
    return sistemaData.sintomas.filter { sintoma ->
        edad == "Todas las edades" || sintoma.edades.contains(mapearEdadAppAJson(edad))
    }.sortedBy { it.nombre }
}

private fun mapearEdadAppAJson(edadApp: String): String {
    return when (edadApp) {
        "Lechones lactantes (0-21 días)" -> "Lechones lactantes"
        "Lechones destetados (22-56 días)" -> "Lechones destetados"
        "Crecimiento/Engorda (57 días a mercado)" -> "Crecimiento/Engorda"
        "Adultos/Reproductores" -> "Adultos/Reproductores"
        else -> edadApp
    }
}

fun convertirSintomaCustomASintomaEnfermedad(sintomaCustom: SintomaCustom): SintomaEnfermedad {
    return SintomaEnfermedad(
        sintoma = sintomaCustom.nombre,
        enfermedades = sintomaCustom.enfermedades,
        descripcion = "Síntoma del sistema corporal",
        edades = sintomaCustom.edades
    )
} 