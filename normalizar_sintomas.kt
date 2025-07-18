import java.io.File

// Lista de síntomas proporcionada por el usuario (normalizada)
val sintomasUsuario = listOf(
    "ABCESOS CUTANEOS",
    "ABCESOS EN BAZO",
    "ABCESOS EN RIÑONES", 
    "ABCESOS HEPATICOS",
    "ABCESOS PULMONARES",
    "ABDOMEN PURPURA",
    "ABORTO",
    "ABORTO EN CUALQUIER ETAPA",
    "ABORTO TERCER TERCIO",
    "ABSCESOS CUTANEOS",
    "ADELGAZAMIENTO DE PAREDES INTESTINALES",
    "ADENOMATOSIS INTESTINAL",
    "ADHERENCIAS EN PULMON Y COSTILLAS",
    "ADHERENCIAS FIBRINONECROTICAS EN ABDOMEN",
    "ADIPSIA",
    "AGALAXIA",
    "AMONTONAMIENTO DE CERDOS",
    "ANEMIA",
    "ANESTRO",
    "ANOREXIA",
    "APARENCIA SUCIA DEL LECHON",
    "AREAS DE CONSOLIDACION PURPURA-GRIS",
    "AREAS DE NECROSIS CIRCULARES Y CONCENTRICAS EN INTESTINOS",
    "ARTRITIS",
    "ARTRITIS PURULENTA",
    "ARTRITIS SUPURATIVA",
    "ASCITIS",
    "ATAXIA",
    "ATROFIA DE VELLOSIDADES INTESTINALES",
    "ATROFIA TESTICULAR",
    "BAJO NUMERO DE NACIDOS VIVOS",
    "BANDAS CICATRICIALES EN PULMONES",
    "BOTONES PESTOSOS EN INTESTINO DELGADO",
    "BRAQUIGNATISMO SUPERIOR",
    "BRONCONEUMONIA",
    "BRONCONEUMONIA CATARRAL PURULENTA",
    "BRONCONEUMONIA PURULENTA",
    "BRONCONEUMONIA SUPURATIVA LOBULAR",
    "BRONQIOLITIS EXUDATIVA",
    "BRONQUITIS",
    "BRONQUITIS SUPURATIVA",
    "BULLAS PULMONARES",
    "BURSITIS",
    "CABEZA AGACHADA",
    "CALAMBRES ABDOMINALES",
    "CANCER",
    "CAQUEXIA",
    "CEGUERA",
    "CELO SILENCIOSO",
    "CELO TARDIO POSTDESTETE",
    "CERDAS PRIMERIZAS SON MÁS AFECTADAS",
    "CIANOSIS",
    "CIANOSIS EN PECHO Y ESTOMAGO",
    "COJERA",
    "COLITIS CATARRAL",
    "CONGESTION Y EDEMA DE COLON",
    "CONGESTION PULMONAR",
    "CONJUNTIVITIS",
    "CONJUNTIVITIS DEL RECIEN NACIDO",
    "CONSOLIDACION CRÁNEO VENTRAL PURPURA-GRIS EN PULMONES",
    "CONSUMO DE ORINA",
    "CONVERSION ALIMENTICIA ELEVADA",
    "CONVULSIONES",
    "CORAZON BLANDO",
    "COSTRAS SECAS EN PIEL",
    "CRECIMIENTO LENTO",
    "DEBILIDAD",
    "DECOLORACION DEL MUSCULO DORSAL",
    "DECUBITO LATERAL",
    "DEFORMACION DE ARTICULACIONES",
    "DEPRESION",
    "DERMATITIS",
    "DERMATITIS HEMORRAGICA NECORTIZANTE",
    "DESCARGA NASAL",
    "DESCARGA NASAL SANGUINOLENTA",
    "DESCARGA VAGINAL COLOR CAFÉ-GRISASEO",
    "DESCARGA VAGINAL SANGUINOLENTA",
    "DESHIDRATACION",
    "DESMEDRO",
    "DESNUTRICION",
    "DIARREA",
    "DIARREA AGUDA AMARILLA",
    "DIARREA ACUOSA",
    "DIARREA ACUOSA AMARILLA",
    "DIARREA AMARILLA",
    "DIARREA AMARILLA LIQUIDA",
    "DIARREA BLANQUECINA",
    "DIARREA CAFÉ",
    "DIARREA COLOR ALQUITRAN",
    "DIARREA COLOR GRIS CEMENTO",
    "DIARREA CON SANGRE COLOR OBSCURO Y ALQUITRANADO",
    "DIARREA CON SANGRE Y MOCO",
    "DIARREA PERNICIOSA",
    "DIARREA GRASOSA",
    "DIARREA GRIS PASTOSA",
    "DIARREA HEMORRAGICA",
    "DIARREA MUCOSA O CREMOSA",
    "MANCHA DED DIARREA EN LA PIEL DEL PERINÉ",
    "DIARREA SANGUINOLENTA",
    "DIARREA VERDOSA",
    "DIARREA VERDOSA CON OLOR FETIDO",
    "DILATACION DEL INTESTINO DELGADO",
    "DILATACION GASTRICA",
    "DISMINUCION DE CRECIMIENTO",
    "DISNEA",
    "DISTENSION INTESTINAL HEMORRAGICA",
    "DISTENSION RENAL",
    "DOLOR ABDOMINAL",
    "ECCEMA CON SARNA",
    "EDEMA CONJUNTIVAL",
    "EDEMA DE COLON",
    "EDEMA DE EXTREMIDADES POSTERIORES",
    "EDEMA DE LENGUA",
    "EDEMA DE LINFONODULOS BRONQUIALES",
    "EDEMA DE LINFONODULOS ILEOCECALES",
    "EDEMA DE LINFONODULOS MESENTERICOS",
    "EDEMA DE VESICULA BILIAR",
    "EDEMA DEL MESOCOLON",
    "EDEMA ESCROTAL",
    "EDEMA FIBRINOHEMORRAGICO DE INTESTINO DELGADO",
    "EDEMA INTESTINAL",
    "EDEMA MESENTERICO",
    "EDEMA PALPEBRAL",
    "EDEMA PREPUCIAL",
    "EDEMA PULMONAR",
    "EDEMA RENAL",
    "EDEMA SUBCUTANEO",
    "EDEMA TESTICULAR",
    "EDEMA Y CONGESTION DEL MESENTERIO",
    "EMACIACION",
    "ENCEFALITIS",
    "ENCEFALOMALASIA FOCAL",
    "ENCEFALOMIELITIS",
    "ENCORVAMIENTO",
    "ENDOCARDITIS",
    "ENDOCARDITIS MURAL",
    "ENDOCARDITIS VALVULAR",
    "ENDOCARDITIS VEGETATIVA",
    "ENDOMETRITIS",
    "ENFISEMA DE YEYUNO",
    "ENFISEMA PULMONAR",
    "ENTERITIS",
    "ENTERITIS CATARRAL",
    "ENTERITIS FIBRINONECROTICA",
    "ENTEROCOLITIS",
    "EPICARDITIS FIBRINOPURULENTA",
    "EPIDIDIMITIS",
    "EPIFORA",
    "EPISTAXIS (HEMORRAGIA NASAL)",
    "EQUIMOSIS",
    "EQUIMOSIS CUTANEA",
    "EQUIMOSIS RENAL",
    "ERITEMA CUTANEO",
    "ESPASMOS",
    "ESPLENOMEGALIA",
    "ESPUMA BLANCA EN TRAQUEA",
    "ESPUMA SANGUINOLENTA",
    "ESTOMAGO CONGESTIONADO",
    "ESTORNUDO",
    "ESTREÑIMIENTO SEGUIDO DE DIARREA",
    "EXPULSION TARDIA DE LECHONES",
    "EXTRACCION DIFICIL DE PLACENTA RETENIDA",
    "EXUDADO MUCOSO EN TRAQUEA",
    "EXUDADO SANGUINOLENTO EN INTESTINO DELGADO",
    "EXUDADO SANGUINOLENTO EN INTESTINO GRUESO",
    "FALLA REPRODUCTIVA",
    "FIBRINA INTESTINAL",
    "FIEBRE",
    "FIEBRE (43°c)",
    "FRACTURAS OSEAS",
    "GANGLIOS RENALES AGRANDADOS HEMORRAGICOS",
    "GANGRENA GASEOSA",
    "HECES SECAS",
    "HEMORRAGIA CEREBRAL",
    "HEMORRAGIA DE LINFONODULOS PULMONARES",
    "HEMORRAGIA DEL CORDON UMBILICAL",
    "HEMORRAGIA ENDOMETRIAL",
    "HEMORRAGIA HEPATICA",
    "HEMORRAGIA OCULAR",
    "HEMORRAGIA PLACENTARIA",
    "HEMORRAGIA PULMONAR",
    "HEMORRAGIA RENAL",
    "HEMORRAGIA SUBCUTANEA",
    "HEMORRAGIA TESTICULAR",
    "HEMORRAGIAS VASCULARES (COLOR VIOLETA EN LA PIEL)",
    "HEPATITIS",
    "HEPATOMEGALIA",
    "HIDROPERICARDIO",
    "HIDROTORAX",
    "HINCHAZON DE CUELLO",
    "HINCHAZON DOLOROSA EN ESTOMAGO",
    "HIPEREMIA CUTANEA",
    "HIPEREXCITACION",
    "HIPERPLASIA DE LINFONODULOS",
    "HIPERTROFIA DEL APARATO REPRODUCTOR DE LA CERDA",
    "HIPOGALAXIA",
    "HIPOTERMIA",
    "ICTERICIA",
    "INCOORDINACION",
    "INFARTO DE VESICULA BILIAR",
    "INFARTO ESPLENICO",
    "INFARTOS VENOSOS EN CURVATURA MAYOR DEL ESTOMAGO",
    "INFERTILIDAD",
    "INFLAMACION HEMORRAGICA DE ESTOMAGO",
    "INFLAMACION HEMORRAGICA DE INTESTINO",
    "INTESTINO DELGADO ROJO OBSCURO",
    "INTESTINO DELGADO TRANSPARENTE",
    "IRRITACION PERIANAL",
    "MUERTE FETAL EN DISTINTAS EDADES",
    "LAGAÑEO",
    "LECHE SIN DIGERIR EN ESTOMAGO",
    "LECHONES MOMIFICADOS",
    "LECHONES NACIDOS BLANCOS",
    "LECHONES NACIDOS CON PATAS ABIERTAS (SPLAYLEGS)",
    "LECHONES NACIDOS DEBILES",
    "LECHONES NACIDOS MUERTOS",
    "LESION HORIZONTAL EN PEZUÑAS",
    "LESIONES MULTIFOCALES EN HIGADO",
    "LESIONES MULTIFOCALES EN PULMON",
    "LETARGIA",
    "LINFADENITIS",
    "LINFADENOMEGALIA",
    "LINFONODULOS INGUINALES GRANDES Y NEGROS",
    "LINFONODULOS MESENTERICOS EDEMATOSOS",
    "LINFONODULOS PULMONARES GRANDES Y NEGROS",
    "LINFONODULOS RENALES AGRANDADOS HEMORRAGICOS",
    "LIQUIDO SINOVIAL HEMORRAGICO",
    "LIQUIDO SINOVIAL PURULENTO",
    "LORDOSIS",
    "LOS LECHONES SE ACUESTAN SOBRE LA CERDA",
    "MALFORMACIONES FETALES",
    "MANCHAS BLANCAS (DE CAL) EN PLACENTA",
    "MANCHAS BLANCAS EN RIÑONES",
    "MANCHAS DE LECHE EN HIGADO",
    "MANCHAS HEMORRAGICAS EN LA PIEL",
    "MANCHAS MORADAS",
    "MANCHAS ROJAS",
    "MARCHA EN CIRCULOS",
    "MARCHA EN REVERSA",
    "MASTITIS",
    "MASTITIS INTERSTICIAL",
    "MENINGES ENGROSADAS",
    "MENINGITIS",
    "MENINGOENCEFALITIS",
    "METRITIS",
    "MIOCARDITIS",
    "MIOCARDITIS HEMORRAGICA",
    "MORTALIDAD DEL 100% EN LECHONES LACTANTES",
    "MORTINATOS",
    "MUERTE",
    "MUERTE FETAL EN DISTINTOS MOMENTOS",
    "MUERTE FETAL DESPUES DE LOS 70 DIAS DE GESTACION",
    "MUERTE SUBITA",
    "NECROSIS CUTANEA",
    "NECROSIS DE EPIGLOTIS",
    "NECROSIS DE LA COLA",
    "NECROSIS DE LAS OREJAS",
    "NECROSIS DEL INTESTINO GRUESO",
    "NECROSIS DEL MIOCARDIO",
    "NECROSIS DEL MUSCULO DORSAL",
    "NECROSIS EN LAS OREJAS",
    "NECROSIS ESPLENICA",
    "NECROSIS FOCAL DE HIGADO",
    "NECROSIS HEPATICA",
    "NECROSIS PANCREATICA",
    "NECROSIS PULMONAR",
    "NECROSIS PULMONAR BILATERAL",
    "NEFRITIS",
    "NEFRITIS INTERSTICIAL",
    "NEUMONIA BRONCO INTERSTICIAL",
    "NEUMONIA EMBOLICA",
    "NEUMONIA EOSINOFILICA",
    "NEUMONIA FIBRINO PURULENTA",
    "NEUMONIA FIBRINO-NECROTICA",
    "NEUMONIA HEMORRAGICA",
    "NEUMONIA INTERSTICIAL MULTIFOCAL",
    "NEUMONIA INTERSTICIAL",
    "NEUMOTORAX",
    "NISTAGMO",
    "OCLUSION INTESTINAL",
    "OJOS HUNDIDOS",
    "OLIGURIA",
    "OLOR ACIDO DEL EXCREMENTO",
    "ONFALITIS",
    "ONFALOFLEBITIS",
    "OPACIDAD DE CORNEA",
    "OPISTOTONO",
    "OREJA AZUL",
    "ORQUITIS",
    "OSCILACION DE LA CABEZA",
    "OSTEOMIELITIS PURULENTA",
    "PALIDEZ",
    "PANCREATITIS",
    "PARALISIS",
    "PARALISIS POSTERIOR",
    "PARED INTESTINAL ADELGAZADA (TRANSPARENTE)",
    "PARESIA",
    "PARTO PREMATURO",
    "PEDALEO",
    "PELO HIRSUTO",
    "PERDIDA DE PESO",
    "PERICARDITIS",
    "PERICARDITIS FIBRINOSA",
    "PERITONITIS",
    "PERITONITIS FIBRINOSA",
    "PETEQUIA ESPLENICA",
    "PETEQUIA CARDIACA",
    "PETEQUIA PULMONAR",
    "PETEQUIA RENAL",
    "PIEL APERGAMINADA Y SUCIA",
    "PIEL PALIDA",
    "PIEL SECA Y AGRIETADA",
    "PLACENTITIS",
    "PLEURESIA",
    "PLEURITIS",
    "PLEURITIS FIBRINOSA",
    "PLEURITIS SEROSA",
    "POLIARTRITIS",
    "POLIDIPSIA",
    "POLISEROSITIS",
    "POLISEROSITIS SEROFIBRINOSA",
    "POSICION PERRO SENTADO",
    "POSTRACIÓN",
    "POSTURA ANORMAL",
    "PRESENCIA DE GAS EN EL INTESTINO GRUESO",
    "PRESENCIA DE MOCO EN INTESTINO GRUESO",
    "PRESENCIA DE PUS SECUESTRADA",
    "PROLAPSO RECTAL",
    "PROLAPSO VAGINAL",
    "PSEUDOPREÑEZ",
    "PUNTOS BLANCOS EN RIÑONES",
    "QUEMOSIS",
    "RAQUITISMO",
    "RECHAZO DE ALIMENTO",
    "REDUCCIÓN DE CELO EN CERDAS",
    "REPETICIONES DE CELO 21-35 DIAS",
    "RETENCION PLACENTARIA",
    "RETRASO DEL CRECIMIENTO",
    "RETRASO EN LA FECHA DE PARTO",
    "RIGIDEZ MUSCULAR",
    "RONCHAS ROJIZAS",
    "SANGRE EN LA ORINA",
    "SEPTICEMIA",
    "SIALORREA",
    "SINOVITIS",
    "SORDERA",
    "SUPRESION INMUNOLOGICA",
    "TAQUICARDIA",
    "TAQUIPNEA",
    "TEMBLORES",
    "TENDINITIS",
    "TENESMO",
    "TIFLITIS NECROTICA",
    "TIFLOCOLITIS",
    "TONSILITIS NECROTICA",
    "TORSION DEL TABIQUE NASAL",
    "TOS",
    "TOS ASMATICA",
    "TOS CRONICA SECA",
    "TOS DE AHOGO",
    "TOS PAROXISTICA",
    "TOS PRODUCTIVA",
    "TOS SECA",
    "TRAQUEITIS",
    "TREMOR CONGENITO",
    "TROMBOSIS CORONARIA",
    "TROMBOSIS DE VASOS SANGUINEOS DEL PULMON",
    "ULCERACION FOCAL DE INTESTINO DELGADO",
    "ULCERAS BOTONOSAS EN INTESTINO DELGADO Y GRUESO",
    "ULCERAS EN LA PIEL",
    "ULCERAS ORALES",
    "URETRA PURULENTA",
    "URETRITIS",
    "URTICARIA ROMBOIDE",
    "VARIACION DEL TAMAÑO DE LA CAMADA",
    "VEJIGA ENGROSADA",
    "VEJIGA HEMORRAGICA",
    "VEJIGA ULCEROSA",
    "VESICULA BILIAR HEMORRAGICA",
    "VESICULA BILIAR INFLAMADA COLOR VERDE OBSCURO",
    "VIENTRE PROMINENTE",
    "VOMITO",
    "VOMITO BLANQUECINO",
    "VOMITO CON MANCHAS DE SANGRE",
    "VULVOVAGINITIS"
)

// Función para normalizar nombres de síntomas
fun normalizarSintoma(sintoma: String): String {
    return sintoma.trim().uppercase()
        .replace("Á", "A")
        .replace("É", "E") 
        .replace("Í", "I")
        .replace("Ó", "O")
        .replace("Ú", "U")
        .replace("Ñ", "N")
        .replace("  ", " ")
}

// Función para asignar sistema según el síntoma
fun asignarSistema(sintoma: String): String {
    return when {
        sintoma.contains("ABORTO") || sintoma.contains("CELO") || sintoma.contains("LECHONES") || 
        sintoma.contains("PLACENTA") || sintoma.contains("REPRODUCTOR") || sintoma.contains("VAGINAL") ||
        sintoma.contains("TESTICULAR") || sintoma.contains("ESCROTAL") || sintoma.contains("PREPUCIAL") ||
        sintoma.contains("MOMIFICADOS") || sintoma.contains("MORTINATOS") || sintoma.contains("FETAL") ||
        sintoma.contains("GESTACION") || sintoma.contains("MASTITIS") || sintoma.contains("ENDOMETRITIS") ||
        sintoma.contains("METRITIS") || sintoma.contains("ORQUITIS") || sintoma.contains("EPIDIDIMITIS") ||
        sintoma.contains("VULVOVAGINITIS") || sintoma.contains("PSEUDOPREÑEZ") || sintoma.contains("INFERTILIDAD") ||
        sintoma.contains("HIPOGALAXIA") || sintoma.contains("AGALAXIA") || sintoma.contains("PROLAPSO") ||
        sintoma.contains("PLACENTITIS") || sintoma.contains("RETENCION") || sintoma.contains("PARTO") ||
        sintoma.contains("CAMADA") -> "Reproductivo"
        
        sintoma.contains("DIARREA") || sintoma.contains("VOMITO") || sintoma.contains("INTESTINO") ||
        sintoma.contains("ESTOMAGO") || sintoma.contains("COLON") || sintoma.contains("DIGESTIVO") ||
        sintoma.contains("GASTRICA") || sintoma.contains("ENTERITIS") || sintoma.contains("COLITIS") ||
        sintoma.contains("TIFLITIS") || sintoma.contains("TIFLOCOLITIS") || sintoma.contains("ENTEROCOLITIS") ||
        sintoma.contains("DILATACION") || sintoma.contains("OCLUSION") || sintoma.contains("ULCERAS") ||
        sintoma.contains("ULCERACION") || sintoma.contains("BOTONES") || sintoma.contains("PESTOSOS") ||
        sintoma.contains("FIBRINA") || sintoma.contains("EXUDADO") || sintoma.contains("SANGUINOLENTO") ||
        sintoma.contains("CONGESTION") || sintoma.contains("EDEMA") && (sintoma.contains("INTESTINAL") || sintoma.contains("MESENTERICO") || sintoma.contains("COLON")) ||
        sintoma.contains("HECES") || sintoma.contains("OLOR ACIDO") || sintoma.contains("TENESMO") ||
        sintoma.contains("CALAMBRES") || sintoma.contains("DOLOR ABDOMINAL") || sintoma.contains("ANOREXIA") ||
        sintoma.contains("RECHAZO") || sintoma.contains("ADIPSIA") || sintoma.contains("SIALORREA") ||
        sintoma.contains("LECHE") || sintoma.contains("VESICULA BILIAR") || sintoma.contains("HIGADO") ||
        sintoma.contains("HEPATITIS") || sintoma.contains("HEPATOMEGALIA") || sintoma.contains("PANCREATITIS") ||
        sintoma.contains("GANGRENA") || sintoma.contains("ENFISEMA") || sintoma.contains("YEYUNO") ||
        sintoma.contains("PERITONITIS") || sintoma.contains("ASCITIS") || sintoma.contains("VIENTRE") ||
        sintoma.contains("HINCHAZON") && sintoma.contains("ESTOMAGO") -> "Digestivo"
        
        sintoma.contains("PULMON") || sintoma.contains("BRONQUI") || sintoma.contains("TRAQUEA") ||
        sintoma.contains("NEUMONIA") || sintoma.contains("TOS") || sintoma.contains("DISNEA") ||
        sintoma.contains("TAQUIPNEA") || sintoma.contains("RESPIRATORIO") || sintoma.contains("ESPUMA") ||
        sintoma.contains("EXUDADO MUCOSO") || sintoma.contains("DESCARGA NASAL") || sintoma.contains("EPISTAXIS") ||
        sintoma.contains("ESTORNUDO") || sintoma.contains("POSICION PERRO SENTADO") || sintoma.contains("ADHERENCIAS") ||
        sintoma.contains("BANDAS") || sintoma.contains("CICATRICIALES") || sintoma.contains("BULLAS") ||
        sintoma.contains("ENFISEMA PULMONAR") || sintoma.contains("NEUMOTORAX") || sintoma.contains("HIDROTORAX") ||
        sintoma.contains("PLEURITIS") || sintoma.contains("PLEURESIA") || sintoma.contains("TRAQUEITIS") ||
        sintoma.contains("TONSILITIS") || sintoma.contains("BRONQUIOLITIS") || sintoma.contains("CONGESTION PULMONAR") ||
        sintoma.contains("CONSOLIDACION") || sintoma.contains("AREAS") && sintoma.contains("PURPURA") ||
        sintoma.contains("LESIONES MULTIFOCALES") && sintoma.contains("PULMON") || sintoma.contains("ABCESOS PULMONARES") ||
        sintoma.contains("NECROSIS PULMONAR") || sintoma.contains("HEMORRAGIA PULMONAR") || sintoma.contains("PETEQUIA PULMONAR") ||
        sintoma.contains("TROMBOSIS") && sintoma.contains("PULMON") -> "Respiratorio"
        
        sintoma.contains("CORAZON") || sintoma.contains("CARDIACO") || sintoma.contains("ENDOCARDITIS") ||
        sintoma.contains("EPICARDITIS") || sintoma.contains("PERICARDITIS") || sintoma.contains("MIOCARDITIS") ||
        sintoma.contains("TAQUICARDIA") || sintoma.contains("PETEQUIA CARDIACA") || sintoma.contains("TROMBOSIS CORONARIA") ||
        sintoma.contains("HIDROPERICARDIO") || sintoma.contains("NECROSIS DEL MIOCARDIO") -> "Cardiovascular"
        
        sintoma.contains("NERVIO") || sintoma.contains("CEREBRAL") || sintoma.contains("ENCEFALITIS") ||
        sintoma.contains("MENINGITIS") || sintoma.contains("MENINGOENCEFALITIS") || sintoma.contains("CONVULSIONES") ||
        sintoma.contains("ATAXIA") || sintoma.contains("PARALISIS") || sintoma.contains("PARESIA") ||
        sintoma.contains("TREMOR") || sintoma.contains("TEMBLORES") || sintoma.contains("NISTAGMO") ||
        sintoma.contains("OPISTOTONO") || sintoma.contains("MARCHA") || sintoma.contains("CABEZA AGACHADA") ||
        sintoma.contains("DECUBITO") || sintoma.contains("POSTURA") || sintoma.contains("POSTRACIÓN") ||
        sintoma.contains("DEPRESION") || sintoma.contains("LETARGIA") || sintoma.contains("HIPEREXCITACION") ||
        sintoma.contains("OSCILACION") || sintoma.contains("ENCORVAMIENTO") || sintoma.contains("PEDALEO") ||
        sintoma.contains("SORDERA") || sintoma.contains("INCOORDINACION") || sintoma.contains("ESPASMOS") ||
        sintoma.contains("HEMORRAGIA CEREBRAL") || sintoma.contains("ENCEFALOMALASIA") || sintoma.contains("ENCEFALOMIELITIS") ||
        sintoma.contains("MENINGES ENGROSADAS") -> "Nervioso"
        
        sintoma.contains("ARTRITIS") || sintoma.contains("BURSITIS") || sintoma.contains("TENDINITIS") ||
        sintoma.contains("SINOVITIS") || sintoma.contains("OSTEOMIELITIS") || sintoma.contains("FRACTURAS") ||
        sintoma.contains("COJERA") || sintoma.contains("RIGIDEZ MUSCULAR") || sintoma.contains("DEFORMACION") ||
        sintoma.contains("LORDOSIS") || sintoma.contains("RAQUITISMO") || sintoma.contains("BRAQUIGNATISMO") ||
        sintoma.contains("LIQUIDO SINOVIAL") || sintoma.contains("POLIARTRITIS") || sintoma.contains("ATROFIA") && sintoma.contains("MUSCULAR") ||
        sintoma.contains("DECOLORACION") && sintoma.contains("MUSCULO") || sintoma.contains("NECROSIS") && sintoma.contains("MUSCULO") ||
        sintoma.contains("LESION") && sintoma.contains("PEZUÑAS") -> "Musculoesquelético"
        
        sintoma.contains("PIEL") || sintoma.contains("DERMATITIS") || sintoma.contains("ECCEMA") ||
        sintoma.contains("SARNA") || sintoma.contains("URTICARIA") || sintoma.contains("RONCHAS") ||
        sintoma.contains("MANCHAS") || sintoma.contains("ERITEMA") || sintoma.contains("HIPEREMIA") ||
        sintoma.contains("COSTRAS") || sintoma.contains("QUEMOSIS") || sintoma.contains("NECROSIS") && (sintoma.contains("CUTANEA") || sintoma.contains("PIEL") || sintoma.contains("OREJAS") || sintoma.contains("COLA")) ||
        sintoma.contains("ABCESOS CUTANEOS") || sintoma.contains("ABSCESOS CUTANEOS") || sintoma.contains("ULCERAS") && sintoma.contains("PIEL") ||
        sintoma.contains("OREJA AZUL") || sintoma.contains("EDEMA SUBCUTANEO") || sintoma.contains("HEMORRAGIA SUBCUTANEA") ||
        sintoma.contains("GANGRENA") && sintoma.contains("GASEOSA") -> "Dermatológico"
        
        sintoma.contains("RIÑON") || sintoma.contains("RENAL") || sintoma.contains("NEFRITIS") ||
        sintoma.contains("VEJIGA") || sintoma.contains("URETRA") || sintoma.contains("URETRITIS") ||
        sintoma.contains("ORINA") || sintoma.contains("OLIGURIA") || sintoma.contains("SANGRE EN LA ORINA") ||
        sintoma.contains("HEMORRAGIA RENAL") || sintoma.contains("PETEQUIA RENAL") || sintoma.contains("DISTENSION RENAL") ||
        sintoma.contains("EDEMA RENAL") || sintoma.contains("EQUIMOSIS RENAL") || sintoma.contains("ABCESOS EN RIÑONES") ||
        sintoma.contains("MANCHAS BLANCAS EN RIÑONES") || sintoma.contains("PUNTOS BLANCOS EN RIÑONES") ||
        sintoma.contains("LINFONODULOS RENALES") || sintoma.contains("GANGLIOS RENALES") -> "Urinario"
        
        sintoma.contains("BAZO") || sintoma.contains("ESPLEN") || sintoma.contains("ESPLENOMEGALIA") ||
        sintoma.contains("INFARTO ESPLENICO") || sintoma.contains("NECROSIS ESPLENICA") || sintoma.contains("PETEQUIA ESPLENICA") ||
        sintoma.contains("ABCESOS EN BAZO") -> "Hematopoyético"
        
        sintoma.contains("LINFONODULOS") || sintoma.contains("LINFADEN") || sintoma.contains("GANGLIOS") ||
        sintoma.contains("HIPERPLASIA") && sintoma.contains("LINFONODULOS") || sintoma.contains("LINFADENOMEGALIA") ||
        sintoma.contains("LINFADENITIS") || sintoma.contains("SUPRESION INMUNOLOGICA") -> "Inmunológico"
        
        sintoma.contains("OJO") || sintoma.contains("CONJUNTIVITIS") || sintoma.contains("CEGUERA") ||
        sintoma.contains("OPACIDAD") || sintoma.contains("CORNEA") || sintoma.contains("LAGAÑEO") ||
        sintoma.contains("EPIFORA") || sintoma.contains("EDEMA CONJUNTIVAL") || sintoma.contains("EDEMA PALPEBRAL") ||
        sintoma.contains("HEMORRAGIA OCULAR") || sintoma.contains("QUEMOSIS") -> "Ocular"
        
        sintoma.contains("FIEBRE") || sintoma.contains("HIPOTERMIA") || sintoma.contains("SEPTICEMIA") ||
        sintoma.contains("POLISEROSITIS") || sintoma.contains("CANCER") || sintoma.contains("CAQUEXIA") ||
        sintoma.contains("EMACIACION") || sintoma.contains("DESMEDRO") || sintoma.contains("DESNUTRICION") ||
        sintoma.contains("PERDIDA DE PESO") || sintoma.contains("CRECIMIENTO LENTO") || sintoma.contains("RETRASO") ||
        sintoma.contains("DISMINUCION") && sintoma.contains("CRECIMIENTO") || sintoma.contains("CONVERSION ALIMENTICIA") ||
        sintoma.contains("MORTALIDAD") || sintoma.contains("MUERTE") || sintoma.contains("MUERTE SUBITA") ||
        sintoma.contains("ANEMIA") || sintoma.contains("PALIDEZ") || sintoma.contains("DEBILIDAD") ||
        sintoma.contains("FALLA REPRODUCTIVA") || sintoma.contains("VARIACION") && sintoma.contains("CAMADA") ||
        sintoma.contains("AMONTONAMIENTO") || sintoma.contains("LOS LECHONES SE ACUESTAN") ||
        sintoma.contains("MALFORMACIONES") || sintoma.contains("TREMOR CONGENITO") || sintoma.contains("ABDOMEN PURPURA") ||
        sintoma.contains("CIANOSIS") || sintoma.contains("ICTERICIA") || sintoma.contains("HINCHAZON") && sintoma.contains("CUELLO") ||
        sintoma.contains("EDEMA") && !sintoma.contains("INTESTINAL") && !sintoma.contains("MESENTERICO") && !sintoma.contains("COLON") ||
        sintoma.contains("DOLOR") && !sintoma.contains("ABDOMINAL") -> "General"
        
        else -> "General"
    }
}

// Normalizar todos los síntomas
val sintomasNormalizados = sintomasUsuario.map { normalizarSintoma(it) }.distinct().sorted()

println("SÍNTOMAS NORMALIZADOS:")
sintomasNormalizados.forEach { println(it) }

println("\n\nSÍNTOMAS POR SISTEMA:")
val sintomasPorSistema = sintomasNormalizados.groupBy { asignarSistema(it) }
sintomasPorSistema.forEach { (sistema, sintomas) ->
    println("\n$sistema (${sintomas.size} síntomas):")
    sintomas.forEach { println("  - $it") }
}

// Generar el código Kotlin para symptomToSystem
println("\n\nCÓDIGO PARA symptomToSystem:")
println("val symptomToSystem = mapOf(")
sintomasNormalizados.forEachIndexed { index, sintoma ->
    val sistema = asignarSistema(sintoma)
    val coma = if (index < sintomasNormalizados.size - 1) "," else ""
    println("    \"$sintoma\" to \"$sistema\"$coma")
}
println(")")

// Generar el código Kotlin para symptomToSystemByAge
println("\n\nCÓDIGO PARA symptomToSystemByAge:")
println("val symptomToSystemByAge = mapOf(")
sintomasNormalizados.forEachIndexed { index, sintoma ->
    val sistema = asignarSistema(sintoma)
    val edades = when {
        sintoma.contains("ABORTO") || sintoma.contains("CELO") || sintoma.contains("PLACENTA") || 
        sintoma.contains("REPRODUCTOR") || sintoma.contains("VAGINAL") || sintoma.contains("TESTICULAR") ||
        sintoma.contains("ESCROTAL") || sintoma.contains("PREPUCIAL") || sintoma.contains("MOMIFICADOS") ||
        sintoma.contains("MORTINATOS") || sintoma.contains("FETAL") || sintoma.contains("GESTACION") ||
        sintoma.contains("MASTITIS") || sintoma.contains("ENDOMETRITIS") || sintoma.contains("METRITIS") ||
        sintoma.contains("ORQUITIS") || sintoma.contains("EPIDIDIMITIS") || sintoma.contains("VULVOVAGINITIS") ||
        sintoma.contains("PSEUDOPREÑEZ") || sintoma.contains("INFERTILIDAD") || sintoma.contains("HIPOGALAXIA") ||
        sintoma.contains("AGALAXIA") || sintoma.contains("PROLAPSO") || sintoma.contains("PLACENTITIS") ||
        sintoma.contains("RETENCION") || sintoma.contains("PARTO") || sintoma.contains("CAMADA") -> 
            "listOf(\"Adultos/Reproductores\")"
        else -> "listOf(\"Todas las edades\")"
    }
    val coma = if (index < sintomasNormalizados.size - 1) "," else ""
    println("    \"$sintoma\" to SymptomSystemAge(\"$sistema\", $edades)$coma")
}
println(")")