package com.example.myapplication.data

data class SymptomSystemInfo(
    val sistema: String,
    val edades: List<String>
)

val symptomToSystemByAge = mapOf(
    "ABSCESOS PULMONARES" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "BRONCONEUMONIA" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "TOS" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DISNEA" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DIARREA" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "VOMITO" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "ANOREXIA" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "FIEBRE" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "ABORTO" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "COJERA" to SymptomSystemInfo("Locomotor", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "ARTRITIS" to SymptomSystemInfo("Locomotor", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "CONVULSIONES" to SymptomSystemInfo("Nervioso", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "ATAXIA" to SymptomSystemInfo("Nervioso", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DEPRESION" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "CIANOSIS" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "CONJUNTIVITIS" to SymptomSystemInfo("Ocular", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DESHIDRATACION" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "EMACIACION" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "MUERTE SUBITA" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "LECHONES MOMIFICADOS" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "BAJO NUMERO DE NACIDOS VIVOS" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "CELO SILENCIO" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "CELO TARDIO POSTDESTETE" to SymptomSystemInfo("Reproductivo", listOf("Lechones destetados")),
    "AGALAXIA" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "MASTITIS" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "ABSCESOS EN BAZO" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "ABSCESOS EN RIÑONES" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "BULLAS PULMONARES" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DIARREA AMARILLA" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "ABDOMEN PURPURA" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "HECES SECAS" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DIARREA GRIS PASTOSA" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DIARREA HEMORRAGICA" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "EQUIMOSIS" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "POLIDIPSIA" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "RETRASO DEL CRECIMIENTO" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "RECHAZO DE ALIMENTO" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "LECHONES NACIDOS CON PATAS ABIERTAS (SPLAYLEGS)" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "ICTERICIA" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DIARREA MUCOSA O CREMOSA" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "CALAMBRES ABDOMINALES" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "VOMITO CON MANCHAS DE SANGRE" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DIARREA GRASOSA" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DEBILIDAD" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "MUERTE" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DIARREA AGUDA AMARILLA" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "TOS ASMATICA" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DOLOR ABDOMINAL" to SymptomSystemInfo("Digestivo", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "ABORTO EN CUALQUIER ETAPA" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "MASTITIS INTERSTICIAL" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "NEUMONIA INTERSTICIAL" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "BRONCONEUMONIA PURULENTA" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "TOS PRODUCTIVA" to SymptomSystemInfo("Respiratorio", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "LETARGIA" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "DESMEDRO" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "ANESTRO" to SymptomSystemInfo("Reproductivo", listOf("Adultos/Reproductores")),
    "CABEZA AGACHADA" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "CEGUERA" to SymptomSystemInfo("Ocular", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores")),
    "SORDERA" to SymptomSystemInfo("General", listOf("Lechones lactantes", "Lechones destetados", "Crecimiento/Engorda", "Adultos/Reproductores"))
)

val symptomDescriptions = mapOf(
    "ABSCESOS PULMONARES" to "Acumulación de pus en los pulmones",
    "BRONCONEUMONIA" to "Inflamación de los bronquios y pulmones",
    "TOS" to "Expulsión brusca de aire de los pulmones",
    "DISNEA" to "Dificultad para respirar",
    "DIARREA" to "Heces líquidas y frecuentes",
    "VOMITO" to "Expulsión del contenido del estómago",
    "ANOREXIA" to "Pérdida del apetito",
    "FIEBRE" to "Elevación de la temperatura corporal",
    "ABORTO" to "Interrupción del embarazo",
    "COJERA" to "Dificultad para caminar",
    "ARTRITIS" to "Inflamación de las articulaciones",
    "CONVULSIONES" to "Movimientos involuntarios del cuerpo",
    "ATAXIA" to "Falta de coordinación en los movimientos",
    "DEPRESION" to "Estado de tristeza y apatía",
    "CIANOSIS" to "Coloración azulada de la piel",
    "CONJUNTIVITIS" to "Inflamación de la membrana del ojo",
    "DESHIDRATACION" to "Pérdida excesiva de líquidos",
    "EMACIACION" to "Pérdida extrema de peso",
    "MUERTE SUBITA" to "Fallecimiento sin síntomas previos",
    "LECHONES MOMIFICADOS" to "Fetos muertos y secos en el útero",
    "BAJO NUMERO DE NACIDOS VIVOS" to "Reducción en la cantidad de lechones nacidos vivos",
    "CELO SILENCIO" to "Celo sin manifestaciones externas",
    "CELO TARDIO POSTDESTETE" to "Retraso en el inicio del celo después del destete",
    "AGALAXIA" to "Ausencia de producción de leche",
    "MASTITIS" to "Inflamación de las glándulas mamarias",
    "ABSCESOS EN BAZO" to "Acumulación de pus en el bazo",
    "ABSCESOS EN RIÑONES" to "Acumulación de pus en los riñones",
    "BULLAS PULMONARES" to "Cavidades llenas de aire en los pulmones",
    "DIARREA AMARILLA" to "Heces líquidas de color amarillo",
    "ABDOMEN PURPURA" to "Coloración púrpura del abdomen",
    "HECES SECAS" to "Heces con poca humedad",
    "DIARREA GRIS PASTOSA" to "Heces líquidas de color gris y consistencia pastosa",
    "DIARREA HEMORRAGICA" to "Heces con presencia de sangre",
    "EQUIMOSIS" to "Manchas moradas en la piel por hemorragia",
    "POLIDIPSIA" to "Sed excesiva",
    "RETRASO DEL CRECIMIENTO" to "Desarrollo más lento de lo normal",
    "RECHAZO DE ALIMENTO" to "Negativa a comer",
    "LECHONES NACIDOS CON PATAS ABIERTAS (SPLAYLEGS)" to "Lechones con patas abiertas al nacer",
    "ICTERICIA" to "Coloración amarilla de la piel y mucosas",
    "DIARREA MUCOSA O CREMOSA" to "Heces con presencia de moco o aspecto cremoso",
    "CALAMBRES ABDOMINALES" to "Dolor y contracciones en el abdomen",
    "VOMITO CON MANCHAS DE SANGRE" to "Vómito que contiene sangre",
    "DIARREA GRASOSA" to "Heces con alto contenido de grasa",
    "DEBILIDAD" to "Falta de fuerza y energía",
    "MUERTE" to "Fallecimiento del animal",
    "DIARREA AGUDA AMARILLA" to "Diarrea severa de color amarillo",
    "TOS ASMATICA" to "Tos característica del asma",
    "DOLOR ABDOMINAL" to "Dolor en la región del abdomen",
    "ABORTO EN CUALQUIER ETAPA" to "Interrupción del embarazo en cualquier momento",
    "MASTITIS INTERSTICIAL" to "Inflamación del tejido intersticial de las mamas",
    "NEUMONIA INTERSTICIAL" to "Inflamación del tejido intersticial de los pulmones",
    "BRONCONEUMONIA PURULENTA" to "Bronconeumonía con presencia de pus",
    "TOS PRODUCTIVA" to "Tos que produce expectoración",
    "LETARGIA" to "Estado de somnolencia y apatía",
    "DESMEDRO" to "Pérdida de peso y condición corporal",
    "ANESTRO" to "Ausencia de ciclos reproductivos",
    "CABEZA AGACHADA" to "Posición de la cabeza hacia abajo",
    "CEGUERA" to "Pérdida de la visión",
    "SORDERA" to "Pérdida de la audición"
) 