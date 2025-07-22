import java.io.File

// Función para determinar las edades apropiadas para cada síntoma
fun determinarEdades(sintoma: String): List<String> {
    return when {
        // Síntomas específicos de lechones lactantes (0-21 días)
        sintoma.contains("LECHON") && (sintoma.contains("NACIDO") || sintoma.contains("RECIEN") || sintoma.contains("LACTANTE")) ||
        sintoma.contains("CONJUNTIVITIS DEL RECIEN NACIDO") ||
        sintoma.contains("CORDON UMBILICAL") ||
        sintoma.contains("TREMOR CONGENITO") ||
        sintoma.contains("LECHONES MOMIFICADOS") ||
        sintoma.contains("MORTINATOS") ||
        sintoma.contains("LECHONES NACIDOS") ||
        sintoma.contains("MALFORMACIONES FETALES") ||
        sintoma.contains("MUERTE FETAL") ||
        sintoma.contains("EXPULSION TARDIA") ||
        sintoma.contains("LECHE SIN DIGERIR") ||
        sintoma.contains("AMONTONAMIENTO") ||
        sintoma.contains("LOS LECHONES SE ACUESTAN") -> 
            listOf("Lechones lactantes (0-21 días)")
        
        // Síntomas específicos de reproductores/adultos
        sintoma.contains("ABORTO") ||
        sintoma.contains("CELO") ||
        sintoma.contains("PLACENTA") ||
        sintoma.contains("REPRODUCTOR") ||
        sintoma.contains("VAGINAL") ||
        sintoma.contains("TESTICULAR") ||
        sintoma.contains("ESCROTAL") ||
        sintoma.contains("PREPUCIAL") ||
        sintoma.contains("MASTITIS") ||
        sintoma.contains("ENDOMETRITIS") ||
        sintoma.contains("METRITIS") ||
        sintoma.contains("ORQUITIS") ||
        sintoma.contains("EPIDIDIMITIS") ||
        sintoma.contains("VULVOVAGINITIS") ||
        sintoma.contains("PSEUDOPREÑEZ") ||
        sintoma.contains("INFERTILIDAD") ||
        sintoma.contains("HIPOGALAXIA") ||
        sintoma.contains("AGALAXIA") ||
        sintoma.contains("PROLAPSO") ||
        sintoma.contains("PLACENTITIS") ||
        sintoma.contains("RETENCION") ||
        sintoma.contains("PARTO") ||
        sintoma.contains("CAMADA") ||
        sintoma.contains("VARIACION") && sintoma.contains("CAMADA") ||
        sintoma.contains("CERDAS PRIMERIZAS") ||
        sintoma.contains("BAJO NUMERO DE NACIDOS") ||
        sintoma.contains("ATROFIA TESTICULAR") ||
        sintoma.contains("BAJA LIBIDO") ||
        sintoma.contains("BAJO CONTEO ESPERMATICO") ||
        sintoma.contains("ANESTRO") -> 
            listOf("Adultos/Reproductores")
        
        // Síntomas específicos de lechones destetados (22-56 días)
        sintoma.contains("DESTETE") ||
        sintoma.contains("POSTDESTETE") ||
        sintoma.contains("MORTALIDAD POST-DESTETE") ||
        sintoma.contains("ADELGAZAMIENTO") && sintoma.contains("PAREDES") ||
        sintoma.contains("ATROFIA") && sintoma.contains("VELLOSIDADES") ||
        sintoma.contains("DIARREA") && (sintoma.contains("AGUDA") || sintoma.contains("AMARILLA") || sintoma.contains("GRASOSA")) ||
        sintoma.contains("COCCIDIOSIS") ||
        sintoma.contains("ASCARIS") ||
        sintoma.contains("ESTRONGILOIDES") -> 
            listOf("Lechones destetados (22-56 días)")
        
        // Síntomas específicos de crecimiento/engorda (57 días a mercado)
        sintoma.contains("ENGORDA") ||
        sintoma.contains("CRECIMIENTO") ||
        sintoma.contains("CONVERSION ALIMENTICIA") ||
        sintoma.contains("CANCER") ||
        sintoma.contains("CAQUEXIA") ||
        sintoma.contains("EMACIACION") ||
        sintoma.contains("DESMEDRO") ||
        sintoma.contains("DESNUTRICION") ||
        sintoma.contains("PERDIDA DE PESO") ||
        sintoma.contains("CRECIMIENTO LENTO") ||
        sintoma.contains("RETRASO") && sintoma.contains("CRECIMIENTO") ||
        sintoma.contains("DISMINUCION") && sintoma.contains("CRECIMIENTO") -> 
            listOf("Crecimiento/Engorda (57 días a mercado)")
        
        // Síntomas respiratorios (comunes en destete y engorda)
        sintoma.contains("BRONCONEUMONIA") ||
        sintoma.contains("BRONQUITIS") ||
        sintoma.contains("NEUMONIA") ||
        sintoma.contains("PLEURITIS") ||
        sintoma.contains("TRAQUEITIS") ||
        sintoma.contains("TOS") ||
        sintoma.contains("DISNEA") ||
        sintoma.contains("TAQUIPNEA") ||
        sintoma.contains("ESTORNUDO") ||
        sintoma.contains("DESCARGA NASAL") ||
        sintoma.contains("EPISTAXIS") ||
        sintoma.contains("ESPUMA") ||
        sintoma.contains("CONGESTION PULMONAR") ||
        sintoma.contains("EDEMA PULMONAR") ||
        sintoma.contains("NECROSIS PULMONAR") ||
        sintoma.contains("BULLAS PULMONARES") ||
        sintoma.contains("ADHERENCIAS") && sintoma.contains("PULMON") -> 
            listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")
        
        // Síntomas digestivos (comunes en todas las edades excepto lactantes)
        sintoma.contains("DIARREA") ||
        sintoma.contains("ENTERITIS") ||
        sintoma.contains("COLITIS") ||
        sintoma.contains("GASTRITIS") ||
        sintoma.contains("VOMITO") ||
        sintoma.contains("ANOREXIA") ||
        sintoma.contains("DOLOR ABDOMINAL") ||
        sintoma.contains("CALAMBRES ABDOMINALES") ||
        sintoma.contains("DESHIDRATACION") ||
        sintoma.contains("ADIPSIA") ||
        sintoma.contains("POLIDIPSIA") ||
        sintoma.contains("SIALORREA") ||
        sintoma.contains("TENESMO") ||
        sintoma.contains("PROLAPSO RECTAL") ||
        sintoma.contains("OCLUSION INTESTINAL") ||
        sintoma.contains("DILATACION") ||
        sintoma.contains("EDEMA") && (sintoma.contains("INTESTINAL") || sintoma.contains("COLON") || sintoma.contains("MESENTERICO")) ||
        sintoma.contains("NECROSIS") && (sintoma.contains("INTESTINO") || sintoma.contains("COLON")) ||
        sintoma.contains("ULCERAS") && sintoma.contains("INTESTINO") ||
        sintoma.contains("EXUDADO") && sintoma.contains("INTESTINO") ||
        sintoma.contains("HEMORRAGIA") && (sintoma.contains("INTESTINO") || sintoma.contains("ESTOMAGO")) ||
        sintoma.contains("CONGESTION") && (sintoma.contains("INTESTINO") || sintoma.contains("ESTOMAGO")) -> 
            listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")
        
        // Síntomas nerviosos (comunes en todas las edades)
        sintoma.contains("CONVULSIONES") ||
        sintoma.contains("ATAXIA") ||
        sintoma.contains("PARALISIS") ||
        sintoma.contains("PARESIA") ||
        sintoma.contains("TEMBLORES") ||
        sintoma.contains("PEDALEO") ||
        sintoma.contains("MARCHA") ||
        sintoma.contains("OPISTOTONO") ||
        sintoma.contains("NISTAGMO") ||
        sintoma.contains("CABEZA") && (sintoma.contains("AGACHADA") || sintoma.contains("LADO")) ||
        sintoma.contains("DECUBITO LATERAL") ||
        sintoma.contains("POSTURA ANORMAL") ||
        sintoma.contains("POSTRACION") ||
        sintoma.contains("DEPRESION") ||
        sintoma.contains("LETARGIA") ||
        sintoma.contains("INCOORDINACION") ||
        sintoma.contains("DESCOORDINACION") ||
        sintoma.contains("RIGIDEZ MUSCULAR") ||
        sintoma.contains("ESPASMOS") ||
        sintoma.contains("MENINGITIS") ||
        sintoma.contains("ENCEFALITIS") ||
        sintoma.contains("MENINGOENCEFALITIS") ||
        sintoma.contains("HEMORRAGIA CEREBRAL") ||
        sintoma.contains("ENCEFALOMALASIA") ||
        sintoma.contains("ENCEFALOMIELITIS") -> 
            listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")
        
        // Síntomas musculoesqueléticos (comunes en destete y engorda)
        sintoma.contains("ARTRITIS") ||
        sintoma.contains("COJERA") ||
        sintoma.contains("BURSITIS") ||
        sintoma.contains("TENDINITIS") ||
        sintoma.contains("SINOVITIS") ||
        sintoma.contains("OSTEOMIELITIS") ||
        sintoma.contains("FRACTURAS") ||
        sintoma.contains("RAQUITISMO") ||
        sintoma.contains("LORDOSIS") ||
        sintoma.contains("BRAQUIGNATISMO") ||
        sintoma.contains("DEFORMACION") && sintoma.contains("ARTICULACIONES") ||
        sintoma.contains("LIQUIDO SINOVIAL") -> 
            listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")
        
        // Síntomas dermatológicos (comunes en todas las edades)
        sintoma.contains("DERMATITIS") ||
        sintoma.contains("ABCESOS") && sintoma.contains("CUTANEOS") ||
        sintoma.contains("NECROSIS") && sintoma.contains("CUTANEA") ||
        sintoma.contains("ULCERAS") && sintoma.contains("PIEL") ||
        sintoma.contains("MANCHAS") ||
        sintoma.contains("RONCHAS") ||
        sintoma.contains("EQUIMOSIS") ||
        sintoma.contains("ERITEMA") ||
        sintoma.contains("EDEMA") && sintoma.contains("PIEL") ||
        sintoma.contains("HEMORRAGIA") && sintoma.contains("SUBCUTANEA") ||
        sintoma.contains("COSTRAS") ||
        sintoma.contains("PIEL") && (sintoma.contains("PALIDA") || sintoma.contains("SECA") || sintoma.contains("GRASOSA") || sintoma.contains("AMORATADA")) ||
        sintoma.contains("PELO HIRSUTO") ||
        sintoma.contains("ECCEMA") ||
        sintoma.contains("SARNA") ||
        sintoma.contains("URTICARIA") -> 
            listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")
        
        // Síntomas urinarios (comunes en todas las edades)
        sintoma.contains("HEMATURIA") ||
        sintoma.contains("OLIGURIA") ||
        sintoma.contains("ANURIA") ||
        sintoma.contains("SANGRE") && sintoma.contains("ORINA") ||
        sintoma.contains("VEJIGA") ||
        sintoma.contains("URETRA") ||
        sintoma.contains("URETRITIS") ||
        sintoma.contains("NEFRITIS") ||
        sintoma.contains("EDEMA RENAL") ||
        sintoma.contains("HEMORRAGIA RENAL") ||
        sintoma.contains("MANCHAS") && sintoma.contains("RIÑONES") ||
        sintoma.contains("PUNTOS") && sintoma.contains("RIÑONES") ||
        sintoma.contains("GANGLIOS") && sintoma.contains("RENALES") ||
        sintoma.contains("DISTENSION RENAL") -> 
            listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")
        
        // Síntomas sensoriales (ojos/oído)
        sintoma.contains("CEGUERA") ||
        sintoma.contains("SORDERA") ||
        sintoma.contains("CONJUNTIVITIS") ||
        sintoma.contains("EDEMA CONJUNTIVAL") ||
        sintoma.contains("OPACIDAD") && sintoma.contains("CORNEA") ||
        sintoma.contains("EPIFORA") ||
        sintoma.contains("LAGAÑEO") ||
        sintoma.contains("QUEMOSIS") ||
        sintoma.contains("EDEMA PALPEBRAL") ||
        sintoma.contains("HEMORRAGIA OCULAR") ||
        sintoma.contains("OREJA") && (sintoma.contains("AZUL") || sintoma.contains("NECROSIS")) -> 
            listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")
        
        // Síntomas cardiovasculares
        sintoma.contains("CORAZON") ||
        sintoma.contains("MIOCARDITIS") ||
        sintoma.contains("ENDOCARDITIS") ||
        sintoma.contains("PERICARDITIS") ||
        sintoma.contains("EPICARDITIS") ||
        sintoma.contains("TAQUICARDIA") ||
        sintoma.contains("TROMBOSIS CORONARIA") ||
        sintoma.contains("HIDROPERICARDIO") ||
        sintoma.contains("PETEQUIA CARDIACA") -> 
            listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")
        
        // Síntomas generales/sistémicos
        sintoma.contains("FIEBRE") ||
        sintoma.contains("HIPOTERMIA") ||
        sintoma.contains("SEPTICEMIA") ||
        sintoma.contains("MUERTE") ||
        sintoma.contains("MUERTE SUBITA") ||
        sintoma.contains("MORTALIDAD") ||
        sintoma.contains("DEBILIDAD") ||
        sintoma.contains("ANEMIA") ||
        sintoma.contains("PALIDEZ") ||
        sintoma.contains("ICTERICIA") ||
        sintoma.contains("CIANOSIS") ||
        sintoma.contains("HINCHAZON") ||
        sintoma.contains("EDEMA") && !sintoma.contains("INTESTINAL") && !sintoma.contains("PULMONAR") && !sintoma.contains("RENAL") && !sintoma.contains("PIEL") ||
        sintoma.contains("HEMORRAGIA") && !sintoma.contains("INTESTINO") && !sintoma.contains("RENAL") && !sintoma.contains("OCULAR") && !sintoma.contains("SUBCUTANEA") ||
        sintoma.contains("ESPLENOMEGALIA") ||
        sintoma.contains("HEPATOMEGALIA") ||
        sintoma.contains("LINFADENITIS") ||
        sintoma.contains("LINFADENOMEGALIA") ||
        sintoma.contains("LINFONODULOS") ||
        sintoma.contains("GANGLIOS") ||
        sintoma.contains("POLISEROSITIS") ||
        sintoma.contains("PERITONITIS") ||
        sintoma.contains("PLEURESIA") ||
        sintoma.contains("HIDROTORAX") ||
        sintoma.contains("NEUMOTORAX") ||
        sintoma.contains("ASCITIS") ||
        sintoma.contains("VIENTRE PROMINENTE") ||
        sintoma.contains("POSICION PERRO SENTADO") ||
        sintoma.contains("ENCORVAMIENTO") -> 
            listOf("Todas las edades")
        
        // Por defecto, aplicar a todas las edades
        else -> listOf("Todas las edades")
    }
}

// Leer el archivo CSV original
val csvFile = File("sintomas_enfermedades.csv")
val lines = csvFile.readLines()

// Procesar cada línea del CSV
val sintomasConEdades = mutableListOf<String>()

for (i in 1 until lines.size) { // Saltar la primera línea (encabezados)
    val line = lines[i]
    val parts = line.split(",")
    if (parts.size >= 2) {
        val sintoma = parts[0].trim().removeSurrounding("\"")
        val enfermedades = parts[1].trim().removeSurrounding("\"")
        val conteo = parts[2].trim().toIntOrNull() ?: 0
        
        // Determinar edades para este síntoma
        val edades = determinarEdades(sintoma)
        
        // Generar el objeto JSON
        val enfermedadesList = if (enfermedades == "SIN ENFERMEDADES ASOCIADAS") {
            "[]"
        } else {
            "[\"${enfermedades.split("; ").joinToString("\", \"")}\"]"
        }
        
        val edadesList = "[\"${edades.joinToString("\", \"")}\"]"
        
        val jsonObject = """  {
    "sintoma": "$sintoma",
    "enfermedades": $enfermedadesList,
    "descripcion": "",
    "sistema": "General",
    "edades": $edadesList
  }"""
        
        sintomasConEdades.add(jsonObject)
    }
}

// Generar el archivo JSON completo
val jsonContent = "[\n${sintomasConEdades.joinToString(",\n")}\n]"

// Guardar el archivo
val outputFile = File("app/src/main/assets/sintomas_enfermedades_custom.json")
outputFile.writeText(jsonContent)

println("✅ Archivo JSON generado exitosamente!")
println("📊 Total de síntomas procesados: ${sintomasConEdades.size}")

// Mostrar estadísticas por rango de edad
val estadisticasEdades = mutableMapOf<String, Int>()
sintomasConEdades.forEach { sintomaJson ->
    val edadesMatch = Regex("\"edades\": \\[([^\\]]+)\\]").find(sintomaJson)
    if (edadesMatch != null) {
        val edades = edadesMatch.groupValues[1].split(", ").map { it.removeSurrounding("\"") }
        edades.forEach { edad ->
            estadisticasEdades[edad] = estadisticasEdades.getOrDefault(edad, 0) + 1
        }
    }
}

println("\n📈 Estadísticas por rango de edad:")
estadisticasEdades.forEach { (edad, count) ->
    println("   • $edad: $count síntomas")
} 