package com.example.myapplication.data

data class SymptomSystemAge(
    val sistema: String,
    val edades: List<String>
)

val symptomToSystemByAge = mapOf(
    // ZEARALENONA
    "ABORTO EN CUALQUIER ETAPA" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    "LECHONES NACIDOS CON PATAS ABIERTAS (SPLAYLEGS)" to SymptomSystemAge("Musculoesquelético", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    "REDUCCIÓN DE CELO EN CERDAS" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    "ULCERAS EN LA PIEL" to SymptomSystemAge("Dermatológico", listOf("Todas las edades")),
    "MASTITIS INTERSTICIAL" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    "PSEUDOPREÑEZ" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    "EDEMA ESCROTAL" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    "EDEMA PREPUCIAL" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    "MANCHAS  BLANCAS (DE CAL) EN PLACENTA" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    "NECROSIS DE LA COLA" to SymptomSystemAge("Dermatológico", listOf("Todas las edades")),
    "NECROSIS EN LAS OREJAS" to SymptomSystemAge("Dermatológico", listOf("Todas las edades")),
    "VULVOVAGINITIS" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    // AFLATOXINA
    "ANOREXIA" to SymptomSystemAge("Digestivo", listOf("Todas las edades")),
    "AGALACTIA" to SymptomSystemAge("Reproductivo", listOf("Adultos/Reproductores")),
    "EQUIMOSIS" to SymptomSystemAge("Hematopoyético", listOf("Todas las edades")),
    // OCRATOXINA
    "POLIDIPSIA" to SymptomSystemAge("Renal/Metabólico", listOf("Todas las edades")),
    "DIARREA" to SymptomSystemAge("Digestivo", listOf("Todas las edades")),
    "RETRASO DEL CRECIMIENTO" to SymptomSystemAge("General", listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")),
    // TRICOTECENOS
    "RECHAZO DEL ALIMENTO" to SymptomSystemAge("Digestivo", listOf("Todas las edades")),
    "VOMITO" to SymptomSystemAge("Digestivo", listOf("Todas las edades")),
    // FUMONISINA
    "RECHAZO DE ALIMENTO" to SymptomSystemAge("Digestivo", listOf("Todas las edades")),
    "ICTERICIA" to SymptomSystemAge("Hepático", listOf("Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")),
    "DISNEA" to SymptomSystemAge("Respiratorio", listOf("Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")),
    "TOS PRODUCTIVA" to SymptomSystemAge("Respiratorio", listOf("Crecimiento/Engorda (57 días a mercado)", "Adultos/Reproductores")),
    // CAMPILOBACTERIOSIS
    "DIARREA MUCOSA O CREMOSA" to SymptomSystemAge("Digestivo", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    "DESHIDRATACION" to SymptomSystemAge("General", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    "FIEBRE" to SymptomSystemAge("General", listOf("Todas las edades")),
    "TENESMO" to SymptomSystemAge("Digestivo", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    "VOMITO CON MANCHAS DE SANGRE" to SymptomSystemAge("Digestivo", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    "DOLOR ABDOMINAL" to SymptomSystemAge("Digestivo", listOf("Todas las edades")),
    "CALAMBRES ABDOMINALES" to SymptomSystemAge("Digestivo", listOf("Todas las edades")),
    // COCCIDIOSIS
    "DIARREA GRASOSA" to SymptomSystemAge("Digestivo", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    "DIARREA AMARILLENTA O BLANQUECINA" to SymptomSystemAge("Digestivo", listOf("Lechones lactantes (0-21 días)")),
    "DEBILIDAD" to SymptomSystemAge("General", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    "OLOR ACIDO DEL EXCREMENTO" to SymptomSystemAge("Digestivo", listOf("Lechones lactantes (0-21 días)")),
    "CAQUEXIA" to SymptomSystemAge("General", listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")),
    "MUERTE" to SymptomSystemAge("General", listOf("Todas las edades")),
    "PELO HIRSUTO" to SymptomSystemAge("General", listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")),
    // ASCARIS SUUM
    "TOS ASMATICA" to SymptomSystemAge("Respiratorio", listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")),
    "DIARREA AGUDA AMARILLA" to SymptomSystemAge("Digestivo", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    "LORDOSIS" to SymptomSystemAge("Musculoesquelético", listOf("Crecimiento/Engorda (57 días a mercado)")),
    "VIENTRE PROMINENTE" to SymptomSystemAge("Digestivo", listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")),
    "RAQUITISMO" to SymptomSystemAge("Musculoesquelético", listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")),
    "DESNUTRICION" to SymptomSystemAge("General", listOf("Lechones destetados (22-56 días)", "Crecimiento/Engorda (57 días a mercado)")),
    "CONVULSIONES" to SymptomSystemAge("Nervioso", listOf("Lechones lactantes (0-21 días)", "Lechones destetados (22-56 días)")),
    // ESTRONGILOIDES
    "ANEMIA" to SymptomSystemAge("Hematopoyético", listOf("Lechones lactantes (0-21 días)")),
    "DIARREA  AGUDA AMARILLA" to SymptomSystemAge("Digestivo", listOf("Lechones lactantes (0-21 días)")),
    "EMACIACION" to SymptomSystemAge("General", listOf("Lechones lactantes (0-21 días)")),
    "MUERTE SÚBITA" to SymptomSystemAge("General", listOf("Lechones lactantes (0-21 días)")),
    // Otros síntomas ya existentes pueden agregarse aquí...
) 