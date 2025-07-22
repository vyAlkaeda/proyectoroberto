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
    
    // Encontrar síntomas de enfermedades que NO están definidos
    val sintomasEnfermedadesSinDefinir = sintomasEnfermedades.filter { sintomaEnfermedad ->
        val sintomaNorm = normalizarSintoma(sintomaEnfermedad)
        !sintomasDefinidosNorm.values.contains(sintomaNorm)
    }
    
    println("=== ANÁLISIS DE SÍNTOMAS ===\n")
    
    println("Total de síntomas en enfermedades: ${sintomasEnfermedades.size}")
    println("Total de síntomas definidos: ${sintomasDefinidos.size}\n")
    
    println("=== SÍNTOMAS DEFINIDOS QUE NO ESTÁN EN ENFERMEDADES (HUÉRFANOS) ===\n")
    if (sintomasHuerfanos.isEmpty()) {
        println("✅ Todos los síntomas definidos están enlazados a enfermedades")
    } else {
        sintomasHuerfanos.forEach { sintoma ->
            println("❌ $sintoma")
        }
        println("\nTotal de síntomas huérfanos: ${sintomasHuerfanos.size}")
    }
    
    println("\n=== SÍNTOMAS DE ENFERMEDADES QUE NO ESTÁN DEFINIDOS ===\n")
    if (sintomasEnfermedadesSinDefinir.isEmpty()) {
        println("✅ Todos los síntomas de enfermedades están definidos")
    } else {
        sintomasEnfermedadesSinDefinir.forEach { sintoma ->
            println("⚠️  $sintoma")
        }
        println("\nTotal de síntomas sin definir: ${sintomasEnfermedadesSinDefinir.size}")
    }
    
    // Mostrar equivalencias para síntomas similares
    println("\n=== POSIBLES EQUIVALENCIAS ===\n")
    sintomasEnfermedadesSinDefinir.forEach { sintomaEnfermedad ->
        val sintomaNorm = normalizarSintoma(sintomaEnfermedad)
        val posiblesEquivalencias = sintomasDefinidos.filter { sintomaDefinido ->
            val definidoNorm = normalizarSintoma(sintomaDefinido)
            sintomaNorm.contains(definidoNorm) || definidoNorm.contains(sintomaNorm)
        }
        if (posiblesEquivalencias.isNotEmpty()) {
            println("$sintomaEnfermedad → Posibles equivalencias: ${posiblesEquivalencias.joinToString(", ")}")
        }
    }
} 