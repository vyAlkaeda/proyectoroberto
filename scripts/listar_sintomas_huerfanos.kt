import com.example.myapplication.data.symptomToSystemByAge
import com.example.myapplication.data.DiseaseData
import java.text.Normalizer
import java.io.File

fun normalizarSintoma(s: String): String {
    return Normalizer.normalize(s, Normalizer.Form.NFD)
        .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        .replace("[()\\[\\]{}.,:;°\\-]".toRegex(), "")
        .replace("\\s+".toRegex(), " ")
        .trim()
        .uppercase()
}

fun main() {
    val sintomasDefinidos = symptomToSystemByAge.keys.map { it to normalizarSintoma(it) }.toMap()
    val sintomasEnfermedades = DiseaseData.diseases
        .flatMap { it.symptoms }
        .map { normalizarSintoma(it) }
        .toSet()

    val sintomasNoEnlazados = sintomasDefinidos.filter { (_, normalizado) -> normalizado !in sintomasEnfermedades }

    val file = File("sintomas_huerfanos.txt")
    file.printWriter().use { out ->
        out.println("Síntomas definidos en symptomToSystemByAge que NO están enlazados a ninguna enfermedad (nombre original):\n")
        sintomasNoEnlazados.forEach { (original, normalizado) ->
            out.println(original)
        }
    }
    println("Archivo sintomas_huerfanos.txt generado con ${sintomasNoEnlazados.size} síntomas huérfanos.")
} 