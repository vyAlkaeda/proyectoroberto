package com.example.myapplication.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EnfermedadRepository(private val context: Context) {
    
    private val gson = Gson()
    
    suspend fun obtenerEnfermedadPorNombre(nombreEnfermedad: String): DiseaseData? = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("enfermedades.json").bufferedReader().use { it.readText() }
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
            val sistemas = jsonObject.getAsJsonObject("SISTEMAS")
            
            val sintomasEncontrados = mutableListOf<String>()
            val edadesEncontradas = mutableSetOf<String>()
            val sistemasEncontrados = mutableSetOf<String>()
            var descripcionEncontrada = ""
            
            // Buscar la enfermedad en todos los sistemas
            sistemas.entrySet().forEach { (nombreSistema, sistemaObj) ->
                val sintomas = sistemaObj.asJsonObject.getAsJsonArray("SINTOMAS")
                
                sintomas.forEach { sintomaElement ->
                    val sintoma = sintomaElement.asJsonObject
                    val enfermedades = sintoma.getAsJsonArray("ENFERMEDADES")
                    
                    if (enfermedades.any { it.asString.equals(nombreEnfermedad, ignoreCase = true) }) {
                        // Encontramos un síntoma asociado a esta enfermedad
                        val nombreSintoma = sintoma.get("NOMBRE").asString
                        sintomasEncontrados.add(nombreSintoma)
                        
                        // Obtener edades
                        val edades = sintoma.getAsJsonArray("EDADES")
                        edades.forEach { edadElement ->
                            edadesEncontradas.add(edadElement.asString)
                        }
                        
                        // Obtener descripción del primer síntoma encontrado
                        if (descripcionEncontrada.isEmpty()) {
                            descripcionEncontrada = sintoma.get("DESCRIPCION").asString
                        }
                        
                        sistemasEncontrados.add(nombreSistema)
                    }
                }
            }
            
            if (sintomasEncontrados.isNotEmpty()) {
                DiseaseData(
                    nombre = nombreEnfermedad,
                    descripcion = descripcionEncontrada,
                    sintomas = sintomasEncontrados.distinct(),
                    edades = edadesEncontradas.toList(),
                    sistemas = sistemasEncontrados.toList(),
                    tipo = "ENFERMEDAD"
                )
            } else {
                null
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun obtenerTodasLasEnfermedades(): List<String> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("enfermedades.json").bufferedReader().use { it.readText() }
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
            val sistemas = jsonObject.getAsJsonObject("SISTEMAS")
            
            val enfermedades = mutableSetOf<String>()
            
            sistemas.entrySet().forEach { (_, sistemaObj) ->
                val sintomas = sistemaObj.asJsonObject.getAsJsonArray("SINTOMAS")
                
                sintomas.forEach { sintomaElement ->
                    val sintoma = sintomaElement.asJsonObject
                    val enfermedadesSintoma = sintoma.getAsJsonArray("ENFERMEDADES")
                    
                    enfermedadesSintoma.forEach { enfermedadElement ->
                        enfermedades.add(enfermedadElement.asString)
                    }
                }
            }
            
            enfermedades.toList().sorted()
            
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
} 