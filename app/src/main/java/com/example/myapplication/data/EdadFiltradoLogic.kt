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

// Data classes para la nueva estructura del JSON (compatible con claves en MAYÚSCULAS)
data class SintomaJson(
    val NOMBRE: String,
    val ENFERMEDADES: List<String>,
    val DESCRIPCION: String,
    val EDADES: List<String>,
    val TIPO: String
)

data class SistemaJson(
    val SINTOMAS: List<SintomaJson>
)

data class EnfermedadesJson(
    val SISTEMAS: Map<String, SistemaJson>
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
    
    // Cargar síntomas desde enfermedades.json con la nueva estructura
    fun cargarSintomasCompletos(context: Context): List<SintomaPorEdad> {
        return try {
            val inputStream = context.assets.open("enfermedades.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            
            val gson = Gson()
            val enfermedadesData: EnfermedadesJson = gson.fromJson(json, EnfermedadesJson::class.java)
            
            val sintomasCompletos = mutableListOf<SintomaPorEdad>()
            
            // Iterar sobre cada sistema y sus síntomas
            enfermedadesData.SISTEMAS.forEach { (nombreSistema, sistemaData) ->
                sistemaData.SINTOMAS.forEach { sintomaJson ->
                    val sintomaPorEdad = SintomaPorEdad(
                        sintoma = sintomaJson.NOMBRE,
                        enfermedades = sintomaJson.ENFERMEDADES,
                        descripcion = sintomaJson.DESCRIPCION,
                        sistema = nombreSistema,
                        tipo = sintomaJson.TIPO,
                        etapas = sintomaJson.EDADES
                    )
                    sintomasCompletos.add(sintomaPorEdad)
                }
            }
            
            sintomasCompletos
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
                sintoma.etapas.contains(edadMapeada) || sintoma.etapas.contains("TODAS LAS EDADES")
            }
        }
    }
    
    // Filtrar síntomas por tipo (SINTOMA/SIGNOS o LESION)
    fun filtrarSintomasPorTipo(sintomas: List<SintomaPorEdad>, tipo: String): List<SintomaPorEdad> {
        return sintomas.filter { sintoma ->
            cumpleTipoFiltro(sintoma.tipo, tipo)
        }
    }
    
    // Filtrar síntomas por edad y tipo
    fun filtrarSintomasPorEdadYTipo(sintomas: List<SintomaPorEdad>, edadSeleccionada: String, tipo: String): List<SintomaPorEdad> {
        android.util.Log.d("EdadFiltradoLogic", "Filtrando síntomas - Edad: $edadSeleccionada, Tipo: $tipo")
        android.util.Log.d("EdadFiltradoLogic", "Total síntomas antes del filtro: ${sintomas.size}")
        
        val resultado = if (edadSeleccionada == "Todas") {
            // Si se selecciona "Todas", solo filtrar por tipo
            sintomas.filter { sintoma ->
                cumpleTipoFiltro(sintoma.tipo, tipo)
            }
        } else {
            sintomas.filter { sintoma ->
                val edadMapeada = mapearEdadSeleccionada(edadSeleccionada)
                val cumpleEdad = sintoma.etapas.contains(edadMapeada) || sintoma.etapas.contains("TODAS LAS EDADES")
                val cumpleTipo = cumpleTipoFiltro(sintoma.tipo, tipo)
                cumpleEdad && cumpleTipo
            }
        }
        
        android.util.Log.d("EdadFiltradoLogic", "Total síntomas después del filtro: ${resultado.size}")
        return resultado
    }
    
    // Función auxiliar para verificar si un tipo cumple con el filtro
    private fun cumpleTipoFiltro(tipoSintoma: String, tipoFiltro: String): Boolean {
        return when (tipoFiltro.uppercase()) {
            "SINTOMA" -> {
                // Para síntomas incluir también SIGNOS NEUROLOGICOS y SIGNOS CLINICOS
                tipoSintoma.equals("SINTOMA", ignoreCase = true) ||
                tipoSintoma.equals("SIGNO NEUROLOGICO", ignoreCase = true) ||
                tipoSintoma.equals("SIGNO CLINICO", ignoreCase = true)
            }
            "LESION" -> {
                tipoSintoma.equals("LESION", ignoreCase = true)
            }
            else -> {
                tipoSintoma.equals(tipoFiltro, ignoreCase = true)
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
    
    // Obtener todos los sistemas disponibles del JSON completo
    fun obtenerTodosLosSistemas(context: Context): List<String> {
        return try {
            val inputStream = context.assets.open("enfermedades.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            
            val gson = Gson()
            val enfermedadesData: EnfermedadesJson = gson.fromJson(json, EnfermedadesJson::class.java)
            
            // Obtener todos los nombres de sistemas y ordenarlos
            enfermedadesData.SISTEMAS.keys.toList().sorted()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    // Mapear edad seleccionada a formato del JSON (compatible con valores en MAYÚSCULAS)
    private fun mapearEdadSeleccionada(edad: String): String {
        val edadMapeada = when (edad) {
            "Todas" -> "TODAS LAS EDADES"
            "Lechones lactantes" -> "LECHONES LACTANTES"
            "Lechones destetados" -> "LECHONES DESTETADOS"
            "Crecimiento/Engorda" -> "CRECIMIENTO/ENGORDA"
            "Adultos/Reproductores" -> "ADULTOS/REPRODUCTORES"
            else -> edad
        }
        android.util.Log.d("EdadFiltradoLogic", "Mapeando edad: '$edad' -> '$edadMapeada'")
        return edadMapeada
    }
    
    // Obtener descripción de una edad específica
    fun obtenerDescripcionEdad(context: Context, etapa: String): String {
        val clasificaciones = cargarClasificacionEdades(context)
        return clasificaciones.find { it.etapa == etapa }?.descripcion ?: ""
    }
    
    // Determinar sistema basado en el nombre del síntoma (ya no necesario con la nueva estructura)
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