package com.example.myapplication.data

data class SistemaOrganizado(
    val nombre: String,
    val descripcion: String,
    val icono: String,
    val color: String,
    val subcategorias: List<String> = emptyList()
)

object SistemaManager {
    
    // Sistemas organizados profesionalmente
    fun obtenerSistemasOrganizados(): List<SistemaOrganizado> {
        return listOf(
            SistemaOrganizado(
                nombre = "Sistema Respiratorio",
                descripcion = "Síntomas relacionados con vías respiratorias, pulmones y ventilación",
                icono = "ic_lungs",
                color = "#E3F2FD",
                subcategorias = listOf("Vías respiratorias superiores", "Pulmones", "Pleura")
            ),
            SistemaOrganizado(
                nombre = "Sistema Digestivo",
                descripcion = "Síntomas del tracto gastrointestinal y digestión",
                icono = "ic_stomach",
                color = "#E8F5E8",
                subcategorias = listOf("Estómago", "Intestinos", "Hígado", "Apetito")
            ),
            SistemaOrganizado(
                nombre = "Sistema Nervioso",
                descripcion = "Síntomas neurológicos y comportamentales",
                icono = "ic_brain",
                color = "#FFF3E0",
                subcategorias = listOf("Central", "Periférico", "Comportamiento", "Coordinación")
            ),
            SistemaOrganizado(
                nombre = "Sistema Reproductivo",
                descripcion = "Síntomas relacionados con reproducción y fertilidad",
                icono = "ic_reproductive",
                color = "#FCE4EC",
                subcategorias = listOf("Gestación", "Parto", "Lactancia", "Fertilidad")
            ),
            SistemaOrganizado(
                nombre = "Sistema Tegumentario",
                descripcion = "Síntomas de piel, pelo y estructuras relacionadas",
                icono = "ic_skin",
                color = "#F3E5F5",
                subcategorias = listOf("Piel", "Pelo", "Temperatura", "Lesiones cutáneas")
            ),
            SistemaOrganizado(
                nombre = "Sistema Musculoesquelético",
                descripcion = "Síntomas de músculos, huesos y articulaciones",
                icono = "ic_bone",
                color = "#E0F2F1",
                subcategorias = listOf("Músculos", "Huesos", "Articulaciones", "Locomoción")
            ),
            SistemaOrganizado(
                nombre = "Sistema Cardiovascular",
                descripcion = "Síntomas del corazón y circulación",
                icono = "ic_heart",
                color = "#FFEBEE",
                subcategorias = listOf("Corazón", "Circulación", "Presión arterial")
            ),
            SistemaOrganizado(
                nombre = "Sistema Urinario",
                descripcion = "Síntomas relacionados con riñones y tracto urinario",
                icono = "ic_kidney",
                color = "#E1F5FE",
                subcategorias = listOf("Riñones", "Vejiga", "Micción")
            ),
            SistemaOrganizado(
                nombre = "Sistema Oftalmológico",
                descripcion = "Síntomas oculares y visuales",
                icono = "ic_eye",
                color = "#F9FBE7",
                subcategorias = listOf("Globo ocular", "Párpados", "Visión")
            ),
            SistemaOrganizado(
                nombre = "Síntomas Generales",
                descripcion = "Síntomas sistémicos y generales",
                icono = "ic_general",
                color = "#FAFAFA",
                subcategorias = listOf("Estado general", "Fiebre", "Peso corporal")
            )
        )
    }
    
    // Función mejorada para determinar sistema con mayor precisión
    fun determinarSistemaAvanzado(sintoma: String): String {
        val sintomaLower = sintoma.uppercase().trim()
        
        // Palabras clave específicas por sistema
        val sistemasKeywords = mapOf(
            "Sistema Respiratorio" to listOf(
                "PULMON", "BRONQU", "RESPIR", "TRAQUEA", "PLEURA", "TOS", "DISNEA", 
                "TAQUIPNEA", "NEUMONIA", "ESTORNUDO", "NASAL", "EPISTAXIS", "MUERTE SÚBITA"
            ),
            "Sistema Digestivo" to listOf(
                "INTESTIN", "ESTOMAG", "COLON", "DIARREA", "VOMITO", "DIGEST", "ANOREXIA",
                "ABDOMEN", "EXCREMENTO", "ESTREÑIMIENTO", "PROLAPSO RECTAL", "APETITO",
                "PESO", "EMACIACION", "CRECIMIENTO"
            ),
            "Sistema Nervioso" to listOf(
                "CEREBRO", "NERVIO", "CONVULS", "ATAXIA", "PARALISIS", "TEMBLOR", "NISTAGMO",
                "CABEZA", "OPISTOTONO", "PEDALEO", "DEPRESION", "LETARGO", "MARCHA",
                "COORDINACION", "DEBILIDAD", "RIGIDEZ"
            ),
            "Sistema Reproductivo" to listOf(
                "ABORTO", "REPRODUCT", "TESTIC", "UTER", "OVARIO", "CELO", "GESTACION",
                "PARTO", "LECHON", "MAMA", "MASTITIS", "AGALAXIA", "VULVA", "PLACENTA",
                "METRITIS", "ORQUITIS", "LIBIDO", "ESPERMA"
            ),
            "Sistema Tegumentario" to listOf(
                "PIEL", "DERMAT", "COSTRA", "ERUPCION", "FIEBRE", "TEMPERATURA", "EDEMA",
                "HEMORRAGIA", "CIANOSIS", "ICTERICIA", "ANEMIA", "ERITEMA", "NECROSIS",
                "ULCERA", "MORETONES", "EQUIMOSIS"
            ),
            "Sistema Musculoesquelético" to listOf(
                "ARTICUL", "ARTRITIS", "COJERA", "MUSCULO", "HUESO", "BURSITIS", 
                "RESISTENCIA", "MOVERSE", "LOCOMOTOR", "ARTICULACIONES"
            ),
            "Sistema Cardiovascular" to listOf(
                "CORAZON", "CARDIAC", "MIOCARD", "CIRCULACION", "PRESION"
            ),
            "Sistema Urinario" to listOf(
                "RIÑON", "RENAL", "URINARIO", "ORINA", "MICCION", "OLIGURIA", "ANURIA"
            ),
            "Sistema Oftalmológico" to listOf(
                "OJO", "CONJUNTIV", "CEGUERA", "LAGRIMEO", "EPIFORA", "PARPADO", 
                "CORNEA", "QUERATITIS", "LAGAÑAS"
            )
        )
        
        // Buscar coincidencias con mayor prioridad por número de matches
        val matches = sistemasKeywords.mapValues { (_, keywords) ->
            keywords.count { keyword -> sintomaLower.contains(keyword) }
        }
        
        // Retornar el sistema con más coincidencias
        val mejorMatch = matches.maxByOrNull { it.value }
        return if (mejorMatch != null && mejorMatch.value > 0) {
            mejorMatch.key
        } else {
            "Síntomas Generales"
        }
    }
    
    // Obtener información detallada de un sistema
    fun obtenerInfoSistema(nombreSistema: String): SistemaOrganizado? {
        return obtenerSistemasOrganizados().find { it.nombre == nombreSistema }
    }
    
    // Contar síntomas por sistema
    fun contarSintomasPorSistema(sintomas: List<SintomaPorEdad>): Map<String, Int> {
        return sintomas.groupBy { determinarSistemaAvanzado(it.sintoma) }
            .mapValues { it.value.size }
    }
    
    // Obtener orden recomendado para mostrar sistemas
    fun obtenerOrdenRecomendado(): List<String> {
        return listOf(
            "Síntomas Generales",
            "Sistema Respiratorio", 
            "Sistema Digestivo",
            "Sistema Nervioso",
            "Sistema Tegumentario",
            "Sistema Reproductivo",
            "Sistema Musculoesquelético",
            "Sistema Cardiovascular",
            "Sistema Urinario",
            "Sistema Oftalmológico"
        )
    }
}