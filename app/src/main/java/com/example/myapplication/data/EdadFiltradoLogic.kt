package com.example.myapplication.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Data class para el archivo de clasificación de edades
data class EdadClasificacion(
    val etapa: String,
    val descripcion: String
)

// Data class para síntomas filtrados por edad
data class SintomaPorEdad(
    val sintoma: String,
    val enfermedades: List<String>,
    val descripcion: String,
    val sistema: String,
    val tipo: String,
    val etapas: List<String>
)

object EdadFiltradoLogic {
    
    // Cargar clasificación de edades desde JSON
    fun cargarClasificacionEdades(context: Context): List<EdadClasificacion> {
        return try {
            val inputStream = context.assets.open("edades_clasificacion.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            
            val gson = Gson()
            val type = object : TypeToken<List<EdadClasificacion>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    // Cargar síntomas desde enfermedades.json
    fun cargarSintomasCompletos(context: Context): List<SintomaPorEdad> {
        return try {
            val inputStream = context.assets.open("enfermedades.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            
            val gson = Gson()
            val type = object : TypeToken<List<Map<String, Any>>>() {}.type
            val sintomasRaw: List<Map<String, Any>> = gson.fromJson(json, type)
            
            sintomasRaw.map { sintoma ->
                val nombreSintoma = sintoma["sintoma"] as String
                SintomaPorEdad(
                    sintoma = nombreSintoma,
                    enfermedades = (sintoma["enfermedades"] as List<*>).map { it.toString() },
                    descripcion = sintoma["descripcion"] as? String ?: "",
                    sistema = sintoma["sistema"] as? String ?: determinarSistema(nombreSintoma),
                    tipo = sintoma["tipo"] as? String ?: "Síntoma",
                    etapas = (sintoma["etapas"] as? List<*>)?.map { it.toString() } ?: listOf("Todas")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    // Filtrar síntomas por edad específica
    fun filtrarSintomasPorEdad(sintomas: List<SintomaPorEdad>, edadSeleccionada: String): List<SintomaPorEdad> {
        return if (edadSeleccionada == "Todas") {
            // Si se selecciona "Todas", devolver todos los síntomas sin filtrado por edad
            sintomas
        } else {
            sintomas.filter { sintoma ->
                // Mapear la edad seleccionada a los rangos del JSON
                val edadMapeada = mapearEdadSeleccionada(edadSeleccionada)
                sintoma.etapas.contains(edadMapeada) || sintoma.etapas.contains("Todas")
            }
        }
    }
    
    // Filtrar síntomas por tipo (Síntoma o Lesión)
    fun filtrarSintomasPorTipo(sintomas: List<SintomaPorEdad>, tipo: String): List<SintomaPorEdad> {
        return sintomas.filter { sintoma ->
            sintoma.tipo.equals(tipo, ignoreCase = true)
        }
    }
    
    // Filtrar síntomas por edad y tipo
    fun filtrarSintomasPorEdadYTipo(sintomas: List<SintomaPorEdad>, edadSeleccionada: String, tipo: String): List<SintomaPorEdad> {
        return if (edadSeleccionada == "Todas") {
            // Si se selecciona "Todas", solo filtrar por tipo
            sintomas.filter { sintoma ->
                sintoma.tipo.equals(tipo, ignoreCase = true)
            }
        } else {
            sintomas.filter { sintoma ->
                val edadMapeada = mapearEdadSeleccionada(edadSeleccionada)
                (sintoma.etapas.contains(edadMapeada) || sintoma.etapas.contains("Todas")) &&
                sintoma.tipo.equals(tipo, ignoreCase = true)
            }
        }
    }
    
    // Agrupar síntomas por sistema
    fun agruparSintomasPorSistema(sintomas: List<SintomaPorEdad>): Map<String, List<SintomaPorEdad>> {
        return sintomas.groupBy { it.sistema }
    }
    
    // Obtener sistemas disponibles para una edad
    fun obtenerSistemasDisponibles(sintomas: List<SintomaPorEdad>): List<String> {
        return sintomas.map { it.sistema }.distinct().sorted()
    }
    
    // Mapear edad seleccionada a formato del JSON
    private fun mapearEdadSeleccionada(edad: String): String {
        return when (edad) {
            "Todas" -> "Todas"
            "Lechones lactantes" -> "Lechones lactantes"
            "Lechones destetados" -> "Lechones destetados"
            "Crecimiento/Engorda" -> "Crecimiento/Engorda"
            "Adultos/Reproductores" -> "Adultos/Reproductores"
            else -> edad
        }
    }
    
    // Obtener descripción de una edad específica
    fun obtenerDescripcionEdad(context: Context, etapa: String): String {
        val clasificaciones = cargarClasificacionEdades(context)
        return clasificaciones.find { it.etapa == etapa }?.descripcion ?: ""
    }
    
    // Determinar sistema basado en el nombre del síntoma
    fun determinarSistema(sintoma: String): String {
        val sintomaLower = sintoma.lowercase()
        return when {
            sintomaLower.contains("pulmon") || sintomaLower.contains("bronqu") || sintomaLower.contains("respir") || 
            sintomaLower.contains("traquea") || sintomaLower.contains("pleura") -> "Respiratorio"
            
            sintomaLower.contains("intestin") || sintomaLower.contains("estomag") || sintomaLower.contains("colon") ||
            sintomaLower.contains("diarrea") || sintomaLower.contains("vomito") || sintomaLower.contains("digest") ->
                "Digestivo"
            
            sintomaLower.contains("articul") || sintomaLower.contains("artritis") || sintomaLower.contains("cojera") ||
            sintomaLower.contains("musculo") || sintomaLower.contains("hueso") -> "Locomotor"
            
            sintomaLower.contains("piel") || sintomaLower.contains("dermat") || sintomaLower.contains("costra") ||
            sintomaLower.contains("erupcion") -> "Tegumentario"
            
            sintomaLower.contains("ojo") || sintomaLower.contains("conjuntiv") || sintomaLower.contains("ceguera") ->
                "Oftalmológico"
            
            sintomaLower.contains("cerebro") || sintomaLower.contains("nervio") || sintomaLower.contains("convuls") ||
            sintomaLower.contains("ataxia") || sintomaLower.contains("paralisis") -> "Nervioso"
            
            sintomaLower.contains("corazon") || sintomaLower.contains("cardiac") || sintomaLower.contains("miocard") ->
                "Cardiovascular"
            
            sintomaLower.contains("riñon") || sintomaLower.contains("renal") || sintomaLower.contains("urinario") ->
                "Urinario"
            
            sintomaLower.contains("hígado") || sintomaLower.contains("hepatic") || sintomaLower.contains("bazo") ->
                "Hepático"
            
            sintomaLower.contains("aborto") || sintomaLower.contains("reproduct") || sintomaLower.contains("testic") ||
            sintomaLower.contains("uter") || sintomaLower.contains("ovario") -> "Reproductivo"
            
            else -> "General"
        }
    }
} 