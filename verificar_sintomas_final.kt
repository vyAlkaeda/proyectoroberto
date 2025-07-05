import com.example.myapplication.data.DiseaseData
import com.example.myapplication.data.symptomToSystem

fun normalizarSintoma(sintoma: String): String {
    return sintoma.lowercase()
        .replace("[()\\[\\]{}.,:;°\\-]".toRegex(), "")
        .replace("\\s+".toRegex(), " ")
        .trim()
}

fun main() {
    // Obtener todos los síntomas de las enfermedades
    val sintomasEnfermedades = DiseaseData.diseases.flatMap { it.symptoms }.distinct()
    
    // Obtener todos los síntomas definidos en symptomToSystem
    val sintomasDefinidos = symptomToSystem.keys.toList()
    
    // Normalizar ambos conjuntos para comparación
    val sintomasEnfermedadesNorm = sintomasEnfermedades.map { it to normalizarSintoma(it) }.toMap()
    val sintomasDefinidosNorm = sintomasDefinidos.map { it to normalizarSintoma(it) }.toMap()
    
    // Encontrar síntomas definidos que NO están en enfermedades
    val sintomasHuerfanos = sintomasDefinidos.filter { sintomaDefinido ->
        val sintomaNorm = normalizarSintoma(sintomaDefinido)
        !sintomasEnfermedadesNorm.values.contains(sintomaNorm)
    }
    
    println("=== SÍNTOMAS HUÉRFANOS ===\n")
    if (sintomasHuerfanos.isEmpty()) {
        println("✅ Todos los síntomas definidos están enlazados a enfermedades")
    } else {
        println("❌ Síntomas definidos que NO están en enfermedades:")
        sintomasHuerfanos.forEach { sintoma ->
            println("- $sintoma")
        }
        println("\nTotal de síntomas huérfanos: ${sintomasHuerfanos.size}")
    }
} 