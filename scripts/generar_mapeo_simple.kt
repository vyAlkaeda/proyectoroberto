import java.io.File

// Lista de síntomas del usuario (primeros 50 para prueba)
val sintomasUsuario = listOf(
    "ABCESOS CUTANEOS", "ABCESOS EN BAZO", "ABCESOS EN RIÑONES", "ABCESOS HEPATICOS", "ABCESOS PULMONARES",
    "ABDOMEN PURPURA", "ABORTO", "ABORTO EN CUALQUIER ETAPA", "ABORTO TERCER TERCIO", "ABSCESOS CUTANEOS",
    "ADELGAZAMIENTO DE PAREDES INTESTINALES", "ADENOMATOSIS INTESTINAL", "ADHERENCIAS EN PULMON Y COSTILLAS",
    "ADHERENCIAS FIBRINONECROTICAS EN ABDOMEN", "ADIPSIA", "AGALAXIA", "AMONTONAMIENTO DE CERDOS", "ANEMIA",
    "ANESTRO", "ANOREXIA", "APARENCIA SUCIA DEL LECHON", "AREAS DE CONSOLIDACION PURPURA-GRIS",
    "AREAS DE NECROSIS CIRCULARES Y CONCENTRICAS EN INTESTINOS", "ARTRITIS", "ARTRITIS PURULENTA",
    "ARTRITIS SUPURATIVA", "ASCITIS", "ATAXIA", "ATROFIA DE VELLOSIDADES INTESTINALES", "ATROFIA TESTICULAR",
    "BAJO NUMERO DE NACIDOS VIVOS", "BANDAS CICATRICIALES EN PULMONES", "BOTONES PESTOSOS EN INTESTINO DELGADO",
    "BRAQUIGNATISMO SUPERIOR", "BRONCONEUMONIA", "BRONCONEUMONIA CATARRAL PURULENTA", "BRONCONEUMONIA PURULENTA",
    "BRONCONEUMONIA SUPURATIVA LOBULAR", "BRONQIOLITIS EXUDATIVA", "BRONQUITIS", "BRONQUITIS SUPURATIVA",
    "BULLAS PULMONARES", "BURSITIS", "CABEZA AGACHADA", "CALAMBRES ABDOMINALES", "CANCER", "CAQUEXIA",
    "CEGUERA", "CELO SILENCIOSO", "CELO TARDIO POSTDESTETE", "CERDAS PRIMERIZAS SON MÁS AFECTADAS"
)

// Enfermedades y síntomas (versión simplificada)
val enfermedadesConSintomas = mapOf(
    "Estreptococosis" to listOf("FIEBRE", "ANOREXIA", "DEPRESION", "COJERA", "ARTRITIS", "CONVULSIONES"),
    "Pleuroneumonia" to listOf("FIEBRE", "ANOREXIA", "TOS", "DISNEA", "VOMITO", "MUERTE SUBITA"),
    "PRRS" to listOf("FIEBRE", "ANOREXIA", "TOS", "ABORTO", "LETARGIA", "LECHONES MOMIFICADOS"),
    "Circovirus" to listOf("FIEBRE", "ANOREXIA", "EMACIACION", "DIARREA", "MUERTE SUBITA"),
    "Salmonelosis" to listOf("DIARREA", "FIEBRE", "DESHIDRATACION", "ANOREXIA", "VOMITO"),
    "Brucelosis" to listOf("ABORTO", "COJERA", "ANESTRO", "MASTITIS"),
    "Erisipela" to listOf("FIEBRE", "ANOREXIA", "COJERA", "ABORTO", "ARTRITIS"),
    "Clostridiosis" to listOf("DIARREA", "DEPRESION", "MUERTE SUBITA"),
    "ZEARALENONA" to listOf("ABORTO EN CUALQUIER ETAPA", "LECHONES NACIDOS CON PATAS ABIERTAS (SPLAYLEGS)"),
    "AFLATOXINA" to listOf("ANOREXIA", "EQUIMOSIS"),
    "OCRATOXINA" to listOf("ANOREXIA", "DIARREA", "RETRASO DEL CRECIMIENTO"),
    "TRICOTECENOS" to listOf("ANOREXIA", "VOMITO", "DIARREA"),
    "FUMONISINA" to listOf("ANOREXIA", "ICTERICIA", "DISNEA", "TOS"),
    "CAMPILOBACTERIOSIS" to listOf("ANOREXIA", "DIARREA MUCOSA O CREMOSA", "DOLOR ABDOMINAL"),
    "COCCIDIOSIS" to listOf("DIARREA GRASOSA", "ANOREXIA", "DEBILIDAD", "MUERTE"),
    "ASCARIS SUUM" to listOf("ANOREXIA", "ICTERICIA", "TOS ASMATICA", "DIARREA AGUDA AMARILLA"),
    "ESTRONGILOIDES" to listOf("ANEMIA", "DIARREA AGUDA AMARILLA", "EMACIACION")
)

// Función para buscar similitudes
fun buscarSimilitud(sintomaUsuario: String, sintomaEnfermedad: String): Boolean {
    val normalizadoUsuario = sintomaUsuario.uppercase().trim()
    val normalizadoEnfermedad = sintomaEnfermedad.uppercase().trim()
    
    // Coincidencia exacta
    if (normalizadoUsuario == normalizadoEnfermedad) return true
    
    // Coincidencia parcial
    if (normalizadoUsuario.contains(normalizadoEnfermedad) || normalizadoEnfermedad.contains(normalizadoUsuario)) return true
    
    // Palabras clave
    val palabrasClave = mapOf(
        "FIEBRE" to listOf("FIEBRE"),
        "DIARREA" to listOf("DIARREA"),
        "VOMITO" to listOf("VOMITO"),
        "TOS" to listOf("TOS"),
        "ANOREXIA" to listOf("ANOREXIA"),
        "ABORTO" to listOf("ABORTO"),
        "ANEMIA" to listOf("ANEMIA"),
        "ARTRITIS" to listOf("ARTRITIS"),
        "COJERA" to listOf("COJERA"),
        "CONVULSIONES" to listOf("CONVULSIONES"),
        "DISNEA" to listOf("DISNEA"),
        "LETARGIA" to listOf("LETARGIA"),
        "DEPRESION" to listOf("DEPRESION"),
        "MUERTE" to listOf("MUERTE"),
        "EMACIACION" to listOf("EMACIACION"),
        "ICTERICIA" to listOf("ICTERICIA"),
        "DEBILIDAD" to listOf("DEBILIDAD"),
        "DOLOR ABDOMINAL" to listOf("DOLOR ABDOMINAL"),
        "CALAMBRES ABDOMINALES" to listOf("CALAMBRES ABDOMINALES"),
        "LECHONES NACIDOS CON PATAS ABIERTAS (SPLAYLEGS)" to listOf("LECHONES NACIDOS CON PATAS ABIERTAS (SPLAYLEGS)"),
        "DIARREA MUCOSA O CREMOSA" to listOf("DIARREA MUCOSA O CREMOSA"),
        "DIARREA GRASOSA" to listOf("DIARREA GRASOSA"),
        "DIARREA AGUDA AMARILLA" to listOf("DIARREA AGUDA AMARILLA"),
        "TOS ASMATICA" to listOf("TOS ASMATICA"),
        "ABORTO EN CUALQUIER ETAPA" to listOf("ABORTO EN CUALQUIER ETAPA"),
        "RETRASO DEL CRECIMIENTO" to listOf("RETRASO DEL CRECIMIENTO"),
        "EQUIMOSIS" to listOf("EQUIMOSIS"),
        "ABCESOS" to listOf("ABCESOS"),
        "BRONCONEUMONIA" to listOf("BRONCONEUMONIA"),
        "BRONQUITIS" to listOf("BRONQUITIS"),
        "BULLAS PULMONARES" to listOf("BULLAS PULMONARES"),
        "BURSITIS" to listOf("BURSITIS"),
        "CABEZA AGACHADA" to listOf("CABEZA AGACHADA"),
        "CANCER" to listOf("CANCER"),
        "CAQUEXIA" to listOf("CAQUEXIA"),
        "CEGUERA" to listOf("CEGUERA"),
        "CELO SILENCIOSO" to listOf("CELO SILENCIOSO"),
        "ANESTRO" to listOf("ANESTRO"),
        "AGALAXIA" to listOf("AGALAXIA"),
        "ADIPSIA" to listOf("ADIPSIA"),
        "ATAXIA" to listOf("ATAXIA"),
        "ASCITIS" to listOf("ASCITIS"),
        "ARTRITIS PURULENTA" to listOf("ARTRITIS PURULENTA"),
        "ARTRITIS SUPURATIVA" to listOf("ARTRITIS SUPURATIVA"),
        "ADENOMATOSIS INTESTINAL" to listOf("ADENOMATOSIS INTESTINAL"),
        "ADELGAZAMIENTO DE PAREDES INTESTINALES" to listOf("ADELGAZAMIENTO DE PAREDES INTESTINALES"),
        "ABSCESOS CUTANEOS" to listOf("ABSCESOS CUTANEOS"),
        "ABORTO TERCER TERCIO" to listOf("ABORTO TERCER TERCIO"),
        "ABDOMEN PURPURA" to listOf("ABDOMEN PURPURA"),
        "ABCESOS PULMONARES" to listOf("ABCESOS PULMONARES"),
        "ABCESOS HEPATICOS" to listOf("ABCESOS HEPATICOS"),
    )
    
    // Buscar coincidencias por palabras clave
    for ((clave, sinonimos) in palabrasClave) {
        if (sinonimos.any { normalizadoUsuario.contains(it) } && 
            sinonimos.any { normalizadoEnfermedad.contains(it) }) {
            return true
        }
    }
    
    return false
}

// Generar mapeo
val mapeoCompleto = mutableMapOf<String, MutableList<String>>()

// Inicializar
sintomasUsuario.forEach { sintoma ->
    mapeoCompleto[sintoma] = mutableListOf()
}

// Buscar enfermedades
sintomasUsuario.forEach { sintomaUsuario ->
    enfermedadesConSintomas.forEach { (enfermedad, sintomasEnfermedad) ->
        sintomasEnfermedad.forEach { sintomaEnfermedad ->
            if (buscarSimilitud(sintomaUsuario, sintomaEnfermedad)) {
                if (!mapeoCompleto[sintomaUsuario]!!.contains(enfermedad)) {
                    mapeoCompleto[sintomaUsuario]!!.add(enfermedad)
                }
            }
        }
    }
}

// Generar JSON
val jsonBuilder = StringBuilder()
jsonBuilder.append("{\n")
jsonBuilder.append("  \"symptomToDiseases\": {\n")

val entries = mapeoCompleto.entries.toList()
entries.forEachIndexed { index, (sintoma, enfermedades) ->
    jsonBuilder.append("    \"$sintoma\": [\n")
    if (enfermedades.isNotEmpty()) {
        enfermedades.forEachIndexed { i, enfermedad ->
            val coma = if (i < enfermedades.size - 1) "," else ""
            jsonBuilder.append("      \"$enfermedad\"$coma\n")
        }
    }
    val coma = if (index < entries.size - 1) "," else ""
    jsonBuilder.append("    ]$coma\n")
}

jsonBuilder.append("  }\n")
jsonBuilder.append("}")

// Guardar archivo
File("symptom_disease_mapping_sample.json").writeText(jsonBuilder.toString())

println("Archivo 'symptom_disease_mapping_sample.json' generado!")
println("Síntomas procesados: ${mapeoCompleto.size}")
println("Síntomas con enfermedades: ${mapeoCompleto.count { it.value.isNotEmpty() }}")
println("Síntomas sin enfermedades: ${mapeoCompleto.count { it.value.isEmpty() }}")

// Mostrar algunos ejemplos
println("\nEJEMPLOS DE MAPEO:")
mapeoCompleto.filter { it.value.isNotEmpty() }.take(5).forEach { (sintoma, enfermedades) ->
    println("$sintoma -> ${enfermedades.joinToString(", ")}")
} 