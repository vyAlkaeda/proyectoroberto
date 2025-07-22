import java.io.File

/**
 * Script para convertir el JSON de mapeo síntomas-enfermedades a formato CSV
 */

// Leer el archivo JSON existente
val jsonFile = File("symptom_disease_mapping_complete.json")
val jsonContent = jsonFile.readText()

// Función para extraer los datos del JSON (simplificada)
fun extraerDatosDelJSON(json: String): Map<String, List<String>> {
    val mapeo = mutableMapOf<String, List<String>>()
    
    // Buscar patrones de "SINTOMA": [ENFERMEDADES]
    val regex = "\"([^\"]+)\":\\s*\\[(.*?)\\]".toRegex(RegexOption.DOT_MATCHES_ALL)
    
    regex.findAll(json).forEach { matchResult ->
        val sintoma = matchResult.groupValues[1]
        val enfermedadesStr = matchResult.groupValues[2]
        
        val enfermedades = if (enfermedadesStr.trim().isEmpty()) {
            emptyList()
        } else {
            enfermedadesStr.split(",")
                .map { it.trim().removeSurrounding("\"") }
                .filter { it.isNotEmpty() }
        }
        
        mapeo[sintoma] = enfermedades
    }
    
    return mapeo
}

// Extraer datos
val mapeoSintomas = extraerDatosDelJSON(jsonContent)

// Generar CSV
val csvBuilder = StringBuilder()
csvBuilder.append("SÍNTOMA,ENFERMEDADES,CONTEO_ENFERMEDADES\n")

mapeoSintomas.forEach { (sintoma, enfermedades) ->
    val enfermedadesStr = if (enfermedades.isEmpty()) {
        "SIN ENFERMEDADES ASOCIADAS"
    } else {
        enfermedades.joinToString("; ")
    }
    val conteo = enfermedades.size
    
    csvBuilder.append("\"$sintoma\",\"$enfermedadesStr\",$conteo\n")
}

// Guardar archivo CSV
val csvFile = File("sintomas_enfermedades.csv")
csvFile.writeText(csvBuilder.toString())

println("✅ Archivo CSV generado: sintomas_enfermedades.csv")
println("📊 Estadísticas:")
println("   • Total de síntomas: ${mapeoSintomas.size}")
println("   • Síntomas con enfermedades: ${mapeoSintomas.count { it.value.isNotEmpty() }}")
println("   • Síntomas sin enfermedades: ${mapeoSintomas.count { it.value.isEmpty() }}")

// Mostrar algunos ejemplos
println("\n📋 Ejemplos de mapeo:")
mapeoSintomas.filter { it.value.isNotEmpty() }.take(5).forEach { (sintoma, enfermedades) ->
    println("   • $sintoma → ${enfermedades.joinToString(", ")}")
}

println("\n⚠️  Síntomas sin enfermedades (primeros 5):")
mapeoSintomas.filter { it.value.isEmpty() }.take(5).forEach { (sintoma, _) ->
    println("   • $sintoma")
} 