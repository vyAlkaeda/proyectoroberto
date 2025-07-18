import java.io.File

fun generarActividadesSintomas() {
    val sintomasFaltantes = listOf(
        "ProlapsoVaginal" to "PROLAPSO VAGINAL" to "Reproductivo",
        "AtrofiaTesticular" to "ATROFIA TESTICULAR" to "Reproductivo",
        "RetrasoFechaParto" to "RETRASO EN LA FECHA DE PARTO" to "Reproductivo",
        "ExpulsionTardiaLechones" to "EXPULSIÓN TARDÍA DE LECHONES" to "Reproductivo",
        "AbortoTercerTercio" to "ABORTO TERCER TERCIO" to "Reproductivo",
        "RepeticionesCelo" to "REPETICIONES DE CELO 21-35 DÍAS" to "Reproductivo",
        "PartoPrematuro" to "PARTO PREMATURO" to "Reproductivo",
        "CeloTardioPostdestete" to "CELO TARDÍO POSTDESTETE" to "Reproductivo",
        "CeloSilencioso" to "CELO SILENCIOSO" to "Reproductivo",
        "LechonesNacidosBlancos" to "LECHONES NACIDOS BLANCOS" to "Reproductivo",
        "HemorragiaEndometrial" to "HEMORRAGIA ENDOMETRIAL" to "Reproductivo",
        "HemorragiaPlacentaria" to "HEMORRAGIA PLACENTARIA" to "Reproductivo",
        "DescargaVaginalSanguinolenta" to "DESCARGA VAGINAL SANGUINOLENTA" to "Reproductivo",
        "Pancreatitis" to "PANCREATITIS" to "Reproductivo",
        "PresenciaLechonesMomificados" to "PRESENCIA DE LECHONES MOMIFICADOS AL PARTO" to "Reproductivo",
        "RepeticionCelosAntes30Dias" to "REPETICIÓN DE CELOS ANTES DE 30 DÍAS DE GESTACIÓN" to "Reproductivo",
        "MuerteFetalDespues70Dias" to "MUERTE FETAL DESPUÉS DE LOS 70 DÍAS DE GESTACIÓN" to "Reproductivo",
        "MuerteFetosDistintosMomentos" to "LA MUERTE DE FETOS ES EN DISTINTOS MOMENTOS" to "Reproductivo",
        "VariacionTamanoCamada" to "VARIACIÓN DEL TAMAÑO DE LA CAMADA" to "Reproductivo",
        "CerdasPrimerizasAfectadas" to "CERDAS PRIMERIZAS SON MÁS AFECTADAS" to "Reproductivo",
        "MuerteFetalDistintosMomentos" to "MUERTE FETAL EN DISTINTOS MOMENTOS" to "Reproductivo",
        
        "UlcerasOrales" to "ULCERAS ORALES" to "Digestivo",
        "NecrosisPancreatica" to "NECROSIS PANCREÁTICA" to "Digestivo",
        "EdemaLinfonodulosIleocecales" to "EDEMA DE LINFONODULOS ILEOCECALES" to "Digestivo",
        "UlceracionFocalIntestinoDelgado" to "ULCERACIÓN FOCAL DE INTESTINO DELGADO" to "Digestivo",
        "EnteritisFibrinonecrotica" to "ENTERITIS FIBRINONECRÓTICA" to "Digestivo",
        "ManchasLecheHigado" to "MANCHAS DE LECHE EN HÍGADO" to "Digestivo",
        "EnteritisCatarrhal" to "ENTERITIS CATARRAL" to "Digestivo",
        "OclusionIntestinal" to "OCLUSIÓN INTESTINAL" to "Digestivo",
        "DiarreaAmarillenta" to "DIARREA AMARILLENTA" to "Digestivo",
        "DiarreaConstanteLechones" to "DIARREA CONSTANTE QUE NO CEDE EN LECHONES" to "Digestivo",
        "DiarreaVerdosa" to "DIARREA VERDOSA" to "Digestivo",
        "ColitisCatarrhal" to "COLITIS CATARRAL" to "Digestivo",
        "DiarreaVerdosaOlorFetido" to "DIARREA VERDOSA CON OLOR FÉTIDO" to "Digestivo",
        "AnorexiaCerdas" to "ANOREXIA EN CERDAS" to "Digestivo",
        "ParedIntestinalAdelgazada" to "PARED INTESTINAL ADELGAZADA (TRANSPARENTE)" to "Digestivo",
        "LecheSinDigerirEstomago" to "LECHE SIN DIGERIR EN ESTÓMAGO" to "Digestivo",
        "EstrenimientoSeguidoDiarrea" to "ESTREÑIMIENTO SEGUIDO DE DIARREA" to "Digestivo",
        "AreasNecrosisCircularesIntestinos" to "ÁREAS DE NECROSIS CIRCULARES Y CONCÉNTRICAS EN INTESTINOS" to "Digestivo",
        "BotonesPestososIntestinoDelgado" to "BOTONES PESTOSOS EN INTESTINO DELGADO" to "Digestivo",
        "UlcerasBotonoasIntestinos" to "ULCERAS BOTONOSAS EN INTESTINO DELGADO Y GRUESO" to "Digestivo",
        "AreasNecrosisCircularesDelimitadas" to "ÁREAS DE NECROSIS CIRCULARES Y CONCÉNTRICAS BIEN DELIMITADAS EN INTESTINOS" to "Digestivo",
        "DiarreaAmarillaLiquida" to "DIARREA AMARILLA LÍQUIDA" to "Digestivo",
        "AdelgazamientoParedesIntestinales" to "ADELGAZAMIENTO DE PAREDES INTESTINALES" to "Digestivo",
        "DiarreaAcuosa" to "DIARREA ACUOSA" to "Digestivo",
        "AparienciaSuciaLechon" to "APARIENCIA SUCIA DEL LECHÓN" to "Digestivo",
        "DistensionIntestinalHemorragica" to "DISTENSIÓN INTESTINAL HEMORRÁGICA" to "Digestivo",
        "RestosLecheCoagulada" to "RESTOS DE LECHE COAGULADA" to "Digestivo",
        "VomitoBlanquecino" to "VOMITO BLANQUECINO" to "Digestivo",
        "Vomito" to "VÓMITO" to "Digestivo",
        "VesiculaBiliarInflamadaVerde" to "VESÍCULA BILIAR INFLAMADA COLOR VERDE OSCURO" to "Digestivo",
        "VesiculaBiliarHemorragica" to "VESÍCULA BILIAR HEMORRÁGICA" to "Digestivo",
        "PresenciaMocoIntestinoGrueso" to "PRESENCIA DE MOCO EN INTESTINO GRUESO" to "Digestivo",
        "HinchazonDolorosaEstomago" to "HINCHAZÓN DOLOROSA EN ESTÓMAGO" to "Digestivo",
        "EncefalomalasiaFocal" to "ENCEFALOMALASIA FOCAL" to "Digestivo",
        "AdherenciasFibrinonecroticasAbdomen" to "ADHERENCIAS FIBRINONECRÓTICAS EN ABDOMEN" to "Digestivo",
        "Ascis" to "ASCIS" to "Digestivo",
        
        "NeumoniaIntersticialMultifocal" to "NEUMONÍA INTERSTICIAL MULTIFOCAL" to "Respiratorio",
        "PleuritisSerosa" to "PLEURITIS SEROSA" to "Respiratorio",
        "BronquiolitisExudativa" to "BRONQUIOLITIS EXUDATIVA" to "Respiratorio",
        "NeumoniaIntesticial" to "NEUMONÍA INTESTICIAL" to "Respiratorio",
        "HemorragiaLinfonodulosPulmonares" to "HEMORRAGIA DE LINFONODULOS PULMONARES" to "Respiratorio",
        "CongestionPulmonar" to "CONGESTIÓN PULMONAR" to "Respiratorio",
        "ConsolidacionCraneoVentralPurpuraGris" to "CONSOLIDACIÓN CRÁNEO VENTRAL PURPURA-GRIS EN PULMONES" to "Respiratorio",
        "BandasCicatricialesPulmones" to "BANDAS CICATRICIALES EN PULMONES" to "Respiratorio",
        "LesionesMultifocalesHigado" to "LESIONES MULTIFOCALES EN HÍGADO" to "Respiratorio",
        "TonsilitisNecrotica" to "TONSILITIS NECRÓTICA" to "Respiratorio",
        "Bronquitis" to "BRONQUITIS" to "Respiratorio",
        
        "NecrosisMiocardio" to "NECROSIS DEL MIOCARDIO" to "Cardiovascular",
        "EpicarditisFibrinopurulenta" to "EPICARDITIS FIBRINOPURULENTA" to "Cardiovascular",
        "Endocarditis" to "ENDOCARDITIS" to "Cardiovascular",
        
        "Meningoencefalitis" to "MENINGOENCEFALITIS" to "Nervioso",
        "MeningesEngrosadas" to "MENINGES ENGROSADAS" to "Nervioso",
        "Hiperexcitacion" to "HIPEREXCITACIÓN" to "Nervioso",
        "OscilacionCabeza" to "OSCILACIÓN DE LA CABEZA" to "Nervioso",
        "Encorvamiento" to "ENCORVAMIENTO" to "Nervioso",
        "MarchaCirculo" to "MARCHA EN CÍRCULO" to "Nervioso",
        "PosturaAnormal" to "POSTURA ANORMAL" to "Nervioso",
        
        "HiperemiaCutanea" to "HIPEREMIA CUTÁNEA" to "Dermatológico",
        "EritemaCutaneoOreja" to "ERITEMA CUTÁNEO EN OREJA" to "Dermatológico",
        "QuemosisGrave" to "QUEMOSIS GRAVE" to "Dermatológico",
        "OrejaAzul" to "OREJA AZUL" to "Dermatológico",
        "ManchasRojas" to "MANCHAS ROJAS" to "Dermatológico",
        "RonchasRojizas" to "RONCHAS ROJIZAS" to "Dermatológico",
        "ManchasMoradas" to "MANCHAS MORADAS" to "Dermatológico",
        "Dermatitis" to "DERMATITIS" to "Dermatológico",
        "EdemaSubcutaneo" to "EDEMA SUBCUTÁNEO" to "Dermatológico",
        "HemorragiaSubcutanea" to "HEMORRAGIA SUBCUTÁNEA" to "Dermatológico",
        "NecrosisPiel" to "NECROSIS EN LA PIEL" to "Dermatológico",
        "PielApergaminadaSucia" to "PIEL APERGAMINADA Y SUCIA" to "Dermatológico",
        "HemorragiasVasculares" to "HEMORRAGIAS VASCULARES" to "Dermatológico",
        "HemorragiasVascularesVioleta" to "HEMORRAGIAS VASCULARES (COLOR VIOLETA EN LA PIEL)" to "Dermatológico",
        "AbscesosCutaneos" to "ABSCESOS CUTÁNEOS" to "Dermatológico",
        
        "VejigaEngrosada" to "VEJIGA ENGROSADA" to "Urinario",
        "VejigaHemorragica" to "VEJIGA HEMORRÁGICA" to "Urinario",
        "VejigaUlcerosa" to "VEJIGA ULCEROSA" to "Urinario",
        "ManchasBlancasRinones" to "MANCHAS BLANCAS EN RIÑONES" to "Urinario",
        "AbscesosRinones" to "ABCESOS EN RIÑONES" to "Urinario",
        "DistensionRenal" to "DISTENSIÓN RENAL" to "Urinario",
        "LinfonodulosRenalesAgrandadosHemorragicos" to "LINFONODULOS RENALES AGRANDADOS HEMORRÁGICOS" to "Urinario",
        "GangliosRenalesAgrandadosHemorragicos" to "GANGLIOS RENALES AGRANDADOS HEMORRÁGICOS" to "Urinario",
        "Oliguria" to "OLIGURIA" to "Urinario",
        
        "PetequiaEsplenica" to "PETEQUIA ESPLÉNICA" to "Hematopoyético",
        "InfartoEsplenico" to "INFARTO ESPLÉNICO" to "Hematopoyético",
        "InfartoVesiculaBiliar" to "INFARTO DE VESÍCULA BILIAR" to "Hematopoyético",
        "NecrosisBazo" to "NECROSIS DE BAZO" to "Hematopoyético",
        "PetequiasBazo" to "PETEQUIAS EN BAZO" to "Hematopoyético",
        "HemorragiaRenal" to "HEMORRAGIA RENAL" to "Hematopoyético",
        
        "LinfonodulosInguinalesGrandesNegros" to "LINFONODULOS INGUINALES GRANDES Y NEGROS" to "Inmunológico",
        "LinfonodulosPulmonaresGrandesNegros" to "LINFONODULOS PULMONARES GRANDES Y NEGROS" to "Inmunológico",
        "HiperplasiaLinfoide" to "HIPERPLASIA LINFOIDE" to "Inmunológico",
        
        "LiquidoSinovialPurulento" to "LÍQUIDO SINOVIAL PURULENTO" to "Musculoesquelético",
        "LiquidoSinovialHemorragico" to "LÍQUIDO SINOVIAL HEMORRÁGICO" to "Musculoesquelético",
        
        "Laganeo" to "LAGAÑEO" to "Ocular",
        "HemorragiaOcular" to "HEMORRAGIA OCULAR" to "Ocular",
        "OpacidadCornea" to "OPACIDAD DE CÓRNEA" to "Ocular",
        
        "Cancer" to "CANCER" to "General",
        "Nefritis" to "NEFRITIS" to "Urinario",
        "Mortalidad100LechonesLactantes" to "MORTALIDAD DEL 100% EN LECHONES LACTANTES" to "General",
        "NecrosisFocalHigado" to "NECROSIS FOCAL DE HÍGADO" to "Hepático",
        "PeritinitisFibrinosa" to "PERITINITIS FIBRINOSA" to "Digestivo",
        "PeritonitisFibrinosa" to "PERITONITIS FIBRINOSA" to "Digestivo",
        "AmontonamientoCerdos" to "AMONTONAMIENTO DE CERDOS" to "General",
        "Fiebre40a42" to "FIEBRE (40°C-42°C)" to "Sistémico",
        "Fiebre41a42" to "FIEBRE (41°C-42°C)" to "Sistémico",
        "Fiebre39a41" to "FIEBRE (39°C-41°C)" to "Sistémico",
        "Fiebre40" to "FIEBRE (40°C)" to "Sistémico",
        "Fiebre41" to "FIEBRE (41°C)" to "Sistémico",
        "Fiebre43" to "FIEBRE (43°C)" to "Sistémico",
        "Fiebre39a40" to "FIEBRE (39°C-40°C)" to "Sistémico",
        "Fiebre41_5" to "FIEBRE 41.5°C" to "Sistémico",
        "Hipotermia35" to "HIPOTERMIA (<35°C)" to "Sistémico",
        "ConversionAlimenticiaElevada" to "CONVERSIÓN ALIMENTICIA ELEVADA" to "Metabólico"
    )
    
    val directorio = "app/src/main/java/com/example/myapplication/"
    
    sintomasFaltantes.forEach { (nombreClase, nombreSintoma, sistema) ->
        val contenido = """
package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class ${nombreClase}DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_detail_base)

        val titleTextView = findViewById<TextView>(R.id.symptomTitle)
        val descriptionTextView = findViewById<TextView>(R.id.symptomDescription)
        val systemTextView = findViewById<TextView>(R.id.symptomSystem)

        titleTextView.text = "$nombreSintoma"
        descriptionTextView.text = "Descripción detallada del síntoma $nombreSintoma. Este síntoma puede estar asociado con diversas condiciones médicas y requiere evaluación veterinaria para determinar la causa subyacente y establecer el tratamiento adecuado."
        systemTextView.text = "Sistema: $sistema"
    }
}
""".trimIndent()
        
        val archivo = File("$directorio${nombreClase}DetailActivity.kt")
        archivo.writeText(contenido)
        println("Creado: ${archivo.name}")
    }
}

// Ejecutar la función
generarActividadesSintomas() 