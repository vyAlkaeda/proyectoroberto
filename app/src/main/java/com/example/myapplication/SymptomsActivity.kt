package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivitySymptomsBinding

class SymptomsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySymptomsBinding
    private lateinit var symptomsAdapter: SymptomAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private val categories = listOf(
        "Cardíaco",
        "Digestivo",
        "Digestivo, respiratorio",
        "Músculo esquelético",
        "Nervioso",
        "Síntomas Reproductivos",
        "Respiratorio",
        "Tegumento"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerViews()
        loadSymptomsForCategory("Digestivo") // Cargar digestivo por defecto
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Signos y Síntomas"
    }

    private fun setupRecyclerViews() {
        categoryAdapter = CategoryAdapter(categories) { category ->
            loadSymptomsForCategory(category)
        }
        binding.categoriesRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(this@SymptomsActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        symptomsAdapter = SymptomAdapter(emptyList()) { selectedClass ->
            startActivity(Intent(this, selectedClass))
        }
        binding.symptomsRecyclerView.apply {
            adapter = symptomsAdapter
            layoutManager = LinearLayoutManager(this@SymptomsActivity)
        }
    }

    private fun loadSymptomsForCategory(category: String) {
        Log.d("SymptomsActivity", "Cargando síntomas para categoría: $category")
        val symptoms = when (category) {
            "Cardíaco" -> emptyList()
            "Digestivo" -> listOf(
                SymptomItem(
                    "VOMITO BLANQUECINO",
                    "Es la expulsión del contenido del estómago de los lechones, al aumentar la presencia de acido la leche se cuaja, si este tiene una textura espumosa, es señal de que hay un exceso de gas en el estómago de los lechones.",
                    "SÍNTOMA",
                    VomitoBlanquecinoDetailActivity::class.java
                ),
                SymptomItem(
                    "VOMITO CON SANGRE",
                    "Hematemesis: Es la expulsión del contenido del estómago de los cerdos con estrias de sangre o sangre completa.",
                    "SÍNTOMA",
                    VomitoSangreDetailActivity::class.java
                ),
                SymptomItem(
                    "DOLOR ABDOMINAL",
                    "Colico. Cuadro de dolor agudo e intenso, de carácter inesperado, fluctuante que afecta los intestinos y estomago del cerdo.",
                    "SÍNTOMA",
                    DolorAbdominalDetailActivity::class.java
                ),
                SymptomItem(
                    "CALAMBRES ABDOMINALES",
                    "Los calambres son contracciones o espasmos súbitos e involuntarios en el abdomen del cerdo que provoca dolor agudo. Generalmente sacan la lengua cada vez que ocurre un calambre abdominal.",
                    "SÍNTOMA",
                    CalambresAbdominalesDetailActivity::class.java
                ),
                SymptomItem(
                    "DIARREA CREMOSA",
                    "Trastorno digestivo que ocurre cuando las heces son pastosas debido a que componentes del alimento no han sido absorbidos en el intestino delgado porque pasan rápido por el intestino y las grasas no son metabolizadas correctamente. Puede ser ocasionada por problemas en el pancreas, hígado o vesícula biliar.",
                    "SÍNTOMA",
                    DiarreaCremosaDetailActivity::class.java
                ),
                SymptomItem(
                    "DIARREA SANGUINOLENTA",
                    "Las heces liquidas rojizas indican hemorragia al final del tracto digestivo. Pueden tener distintas causas, pero por lo general se debe a que hay una infección en el intestino ocasionado por diversos agentes como bacterias, virus o parásitos.",
                    "SÍNTOMA",
                    DiarreaSanguinolentaDetailActivity::class.java
                ),
                SymptomItem(
                    "DIARREA GRASOSA",
                    "Esteatorrea. Es un síntoma que puede indicar malabsorción de grasas en el tracto gastrointestinal. Esto sucede cuando el alimento ingerido no se descompone ni se absorbe correctamente en el sistema digestivo",
                    "SÍNTOMA",
                    DiarreaGrasosaDetailActivity::class.java
                ),
                SymptomItem(
                    "DIARREA BLANQUECINA",
                    "Trastorno digestivo que ocurre cuando las heces tienen color claro o blanco ocasionado por la poca presencia de bilis. Los nutrientes del alimento no han sido absorbidos en el intestino y no consigue digerir correctamente las grasas.",
                    "SÍNTOMA",
                    DiarreaBlanquecinaDetailActivity::class.java
                ),
                SymptomItem(
                    "OLOR ACIDO DEL EXCREMENTO",
                    "Esto es debido a la fermentación de las bacterias del colon de los hidratos de carbono mal absorbidos en el intestino delgado",
                    "SÍNTOMA",
                    OlorAcidoDetailActivity::class.java
                ),
                SymptomItem(
                    "ENTERITIS CATARRAL",
                    "La enteritis catarral es la inflamación del intestino delgado el cual presenta contenido fluido, turbio y moco abundante; la mucosa puede estar mucosa enrojecida y haber pequeñas hemorragias.",
                    "SÍNTOMA",
                    EnteritisCatarralDetailActivity::class.java
                ),
                SymptomItem(
                    "PANZA ABULTADA",
                    "La distensión abdominal es la hinchazón y el aumento del tamaño del abdomen. Puede tener varias causas.",
                    "SÍNTOMA",
                    PanzaAbultadaDetailActivity::class.java
                ),
                SymptomItem(
                    "RECHAZO DEL ALIMENTO",
                    "Los cerdos evitan o restringen severamente el consumo de alimentos. Anorexia nerviosa.",
                    "SÍNTOMA",
                    RechazoAlimentoDetailActivity::class.java
                )
            )
            "Músculo esquelético" -> listOf(
                SymptomItem(
                    "Cojeras",
                    "Dificultad o incapacidad para caminar correctamente, el cerdo apoya poco o nada una de sus extremidades debido a dolor o lesión.",
                    "SÍNTOMA",
                    CojerasDetailActivity::class.java
                ),
                SymptomItem(
                    "Resistencia a moverse",
                    "El cerdo muestra reticencia o negativa a desplazarse, generalmente por dolor, debilidad o incomodidad en las extremidades.",
                    "SÍNTOMA",
                    ResistenciaMoverseDetailActivity::class.java
                ),
                SymptomItem(
                    "Inflamación de brazo (Bursitis)",
                    "Hinchazón dolorosa en la articulación del codo o brazo del cerdo, causada por inflamación de la bursa (bolsa sinovial).",
                    "SÍNTOMA",
                    BursitisDetailActivity::class.java
                ),
                SymptomItem(
                    "Inflamación de Pierna",
                    "Aumento de volumen, calor y dolor en una o ambas piernas del cerdo, generalmente por infección, traumatismo o artritis.",
                    "SÍNTOMA",
                    InflamacionPiernaDetailActivity::class.java
                ),
                SymptomItem(
                    "Articulaciones inflamadas",
                    "Las articulaciones del cerdo presentan hinchazón, dolor y dificultad de movimiento, comúnmente por artritis o infecciones.",
                    "SÍNTOMA",
                    ArticulacionesInflamadasDetailActivity::class.java
                )
            )
            "Nervioso" -> listOf(
                SymptomItem(
                    "Debilidad (postración)",
                    "Falta de fuerza o energía, el cerdo permanece recostado sin ánimo de moverse.",
                    "SÍNTOMA",
                    DebilidadDetailActivity::class.java),
                SymptomItem(
                    "Incordinación (ataxia)",
                    "Pérdida de coordinación motora, el cerdo camina con movimientos erráticos o torpes.",
                    "SÍNTOMA", IncordinacionDetailActivity::class.java),
                SymptomItem(
                    "Decúbito lateral (echado de lado)",
                    "Postura en la que el cerdo permanece acostado de lado sin poder incorporarse.",
                    "SÍNTOMA", DecubitoLateralDetailActivity::class.java),
                SymptomItem(
                    "Cabeza de lado",
                    "La cabeza permanece inclinada hacia un costado, posible alteración neurológica.",
                    "SÍNTOMA", CabezaDeLadoDetailActivity::class.java),
                SymptomItem(
                    "Cabeza hacia atrás (Opistótono)",
                    "Contracción severa de los músculos de la espalda y cuello, curvando la cabeza hacia atrás.",
                    "SÍNTOMA", OpistotonoDetailActivity::class.java),
                SymptomItem(
                    "Rigidez muscular (paresia)",
                    "Los músculos están tensos, el cerdo muestra dificultad para moverse.",
                    "SÍNTOMA", RigidezMuscularDetailActivity::class.java),
                SymptomItem(
                    "Pedaleo",
                    "Movimiento de las patas como si estuviera corriendo mientras está echado.",
                    "SÍNTOMA", PedaleoDetailActivity::class.java),
                SymptomItem(
                    "Depresión",
                    "Estado de decaimiento general, sin respuesta al entorno.",
                    "SÍNTOMA", DepresionDetailActivity::class.java),
                SymptomItem(
                    "Posturas anormales",
                    "Posiciones corporales extrañas o torcidas, indicativas de daño neurológico.",
                    "SÍNTOMA", PosturasAnormalesDetailActivity::class.java),
                SymptomItem(
                    "Movimiento involuntario de ojos (nistagmo)",
                    "Movimiento rítmico y repetitivo de los ojos, suele indicar problemas en el sistema nervioso central.",
                    "SÍNTOMA", NistagmoDetailActivity::class.java),
                SymptomItem(
                    "Cabeza agachada",
                    "La cabeza permanece baja o colgando, sin intención de levantarla.",
                    "SÍNTOMA", CabezaAgachadaDetailActivity::class.java),
                SymptomItem(
                    "Letargo",
                    "Somnolencia excesiva, el cerdo permanece adormecido por periodos prolongados.",
                    "SÍNTOMA", LetargoDetailActivity::class.java),
                SymptomItem(
                    "Marcha en círculos",
                    "El cerdo camina en círculos sin rumbo aparente, síntoma neurológico común.",
                    "SÍNTOMA", MarchaEnCirculosDetailActivity::class.java),
                SymptomItem(
                    "Marcha en reversa",
                    "El animal camina hacia atrás de manera involuntaria o continua.",
                    "SÍNTOMA", MarchaReversaDetailActivity::class.java),
                SymptomItem(
                    "TEMBLORES",
                    "Contracciones musculares involuntarias que indican alteraciones en el sistema nervioso central del cerdo.",
                    "SÍNTOMA",
                    TembloresDetailActivity::class.java
                ),
                SymptomItem(
                    "DESCOORDINACIÓN",
                    "Pérdida del control motor que provoca movimientos torpes o inestables.",
                    "SÍNTOMA",
                    DescoordinacionDetailActivity::class.java
                ),
                SymptomItem(
                    "CONVULSIONES",
                    "Episodios de actividad neurológica anormal, que pueden ser causados por infecciones o toxicidad.",
                    "SÍNTOMA",
                    ConvulsionesDetailActivity::class.java
                ),
                SymptomItem(
                    "PARÁLISIS",
                    "Inmovilidad total o parcial que puede deberse a lesiones del sistema nervioso o infecciones como *Aujeszky*.",
                    "SÍNTOMA",
                    ParalisisDetailActivity::class.java
                )
            )
            "Síntomas Reproductivos" -> listOf(
                SymptomItem(
                    "Aborto primer tercio",
                    "Pérdida de la gestación durante los primeros 35 días, generalmente por infecciones o fallos en la implantación.",
                    "SÍNTOMA",
                    AbortoPrimerTercioDetailActivity::class.java
                ),
                SymptomItem(
                    "Aborto segundo tercio",
                    "Pérdida de la gestación entre los días 36 y 70, puede deberse a infecciones, estrés o deficiencias nutricionales.",
                    "SÍNTOMA",
                    AbortoSegundoTercioDetailActivity::class.java
                ),
                SymptomItem(
                    "Aborto tercer tercio",
                    "Pérdida de la gestación después del día 70, comúnmente por infecciones virales o bacterianas.",
                    "SÍNTOMA",
                    AbortoTercerTercioDetailActivity::class.java
                ),
                SymptomItem(
                    "Cerda vacía",
                    "Hembra que no queda preñada tras la monta o inseminación, puede deberse a problemas reproductivos o manejo.",
                    "SÍNTOMA",
                    CerdaVaciaDetailActivity::class.java
                ),
                SymptomItem(
                    "Falta de celo (anestro)",
                    "Ausencia de manifestaciones de celo en la cerda, puede ser por problemas hormonales, nutricionales o de manejo.",
                    "SÍNTOMA",
                    FaltaCeloDetailActivity::class.java
                ),
                SymptomItem(
                    "Celo silencioso",
                    "La cerda ovula pero no muestra signos externos de celo, dificultando la detección para la reproducción.",
                    "SÍNTOMA",
                    CeloSilenciosoDetailActivity::class.java
                ),
                SymptomItem(
                    "Parto prematuro",
                    "Nacimiento de lechones antes de los 112 días de gestación, generalmente por infecciones o estrés.",
                    "SÍNTOMA",
                    PartoPrematuroDetailActivity::class.java
                ),
                SymptomItem(
                    "Repetición de celo a 21 días",
                    "La cerda vuelve a presentar celo 21 días después de la inseminación, indicando fallo en la concepción.",
                    "SÍNTOMA",
                    RepeticionCelo21DetailActivity::class.java
                ),
                SymptomItem(
                    "Repetición de celo a 28-35 días",
                    "La cerda repite celo entre los días 28 y 35, puede indicar reabsorción embrionaria o aborto temprano.",
                    "SÍNTOMA",
                    RepeticionCelo28_35DetailActivity::class.java
                ),
                SymptomItem(
                    "Bajo número de nacidos vivos",
                    "Menor cantidad de lechones vivos al parto, puede deberse a problemas genéticos, infecciosos o de manejo.",
                    "SÍNTOMA",
                    BajoNacidosVivosDetailActivity::class.java
                ),
                SymptomItem(
                    "Lechones momificados varios tamaños",
                    "Presencia de lechones momificados de diferentes tamaños en la camada, indica muerte fetal en distintos momentos.",
                    "SÍNTOMA",
                    MomificadosVariosTamanosDetailActivity::class.java
                ),
                SymptomItem(
                    "Lechones momificados grandes",
                    "Lechones momificados de gran tamaño, generalmente por muerte fetal tardía.",
                    "SÍNTOMA",
                    MomificadosGrandesDetailActivity::class.java
                ),
                SymptomItem(
                    "Lechones nacidos muertos",
                    "Lechones que mueren durante el parto o poco antes, puede deberse a distocia, infecciones o hipoxia.",
                    "SÍNTOMA",
                    NacidosMuertosDetailActivity::class.java
                ),
                SymptomItem(
                    "Lechones nacidos débiles",
                    "Lechones con poca vitalidad al nacer, pueden tener bajo peso o problemas de salud.",
                    "SÍNTOMA",
                    NacidosDebilesDetailActivity::class.java
                ),
                SymptomItem(
                    "Lechones nacidos blancos",
                    "Lechones con piel pálida o blanca al nacer, puede indicar anemia o problemas circulatorios.",
                    "SÍNTOMA",
                    NacidosBlancosDetailActivity::class.java
                ),
                SymptomItem(
                    "Descarga vaginal purulenta (endometritis)",
                    "Secreción vaginal espesa y purulenta, generalmente por infección uterina tras el parto.",
                    "SÍNTOMA",
                    EndometritisDetailActivity::class.java
                ),
                SymptomItem(
                    "Descarga vaginal (metritis)",
                    "Secreción vaginal anormal, puede ser acuosa o sanguinolenta, asociada a inflamación del útero.",
                    "SÍNTOMA",
                    MetritisDetailActivity::class.java
                ),
                SymptomItem(
                    "Hemorragia en placenta",
                    "Presencia de sangre en la placenta, puede indicar desprendimiento prematuro o infecciones.",
                    "SÍNTOMA",
                    HemorragiaPlacentaDetailActivity::class.java
                ),
                SymptomItem(
                    "Retención placentaria",
                    "La cerda no expulsa la placenta tras el parto, lo que puede causar infecciones y problemas reproductivos.",
                    "SÍNTOMA",
                    RetencionPlacentariaDetailActivity::class.java
                ),
                SymptomItem(
                    "Nula producción de leche (agalaxia)",
                    "La cerda no produce leche tras el parto, lo que pone en riesgo la supervivencia de los lechones.",
                    "SÍNTOMA",
                    AgalaxiaDetailActivity::class.java
                ),
                SymptomItem(
                    "Poca producción de leche (hipogalaxia)",
                    "Producción insuficiente de leche para alimentar a toda la camada, puede deberse a estrés, mala alimentación o enfermedad.",
                    "SÍNTOMA",
                    HipogalaxiaDetailActivity::class.java
                ),
                SymptomItem(
                    "Inflamación de la ubre (mastitis)",
                    "Hinchazón, enrojecimiento y dolor en la ubre, generalmente por infección bacteriana.",
                    "SÍNTOMA",
                    MastitisDetailActivity::class.java
                ),
                SymptomItem(
                    "Inflamación testicular (orquitis)",
                    "Aumento de tamaño, calor y dolor en los testículos, generalmente por infección.",
                    "SÍNTOMA",
                    OrquitisDetailActivity::class.java
                ),
                SymptomItem(
                    "Inflamación prepucial (postitis)",
                    "Hinchazón y enrojecimiento del prepucio, puede dificultar la micción y la reproducción.",
                    "SÍNTOMA",
                    PostitisDetailActivity::class.java
                ),
                SymptomItem(
                    "Baja líbido del macho",
                    "El macho muestra poco interés en la monta o inseminación, puede deberse a problemas hormonales, estrés o enfermedad.",
                    "SÍNTOMA",
                    BajaLibidoMachoDetailActivity::class.java
                ),
                SymptomItem(
                    "Bajo conteo espermático",
                    "Cantidad insuficiente de espermatozoides en el semen, afecta la fertilidad del macho.",
                    "SÍNTOMA",
                    BajoConteoEspermaticoDetailActivity::class.java
                ),
                SymptomItem(
                    "Conjuntivitis en lechón recién nacido",
                    "Inflamación y enrojecimiento de los ojos en lechones recién nacidos, generalmente por infecciones bacterianas.",
                    "SÍNTOMA",
                    ConjuntivitisLechonDetailActivity::class.java
                ),
                SymptomItem(
                    "Cordón umbilical hemorrágico",
                    "Presencia de sangre en el cordón umbilical al nacer, puede deberse a traumatismos o problemas de coagulación.",
                    "SÍNTOMA",
                    CordonUmbilicalHemorragicoDetailActivity::class.java
                ),
                SymptomItem(
                    "Cordón umbilical edematoso (hinchado)",
                    "El cordón umbilical presenta hinchazón o edema, puede indicar infección o mala cicatrización.",
                    "SÍNTOMA",
                    CordonUmbilicalEdematosoDetailActivity::class.java
                ),
                SymptomItem(
                    "Lechones de patas abiertas",
                    "Lechones que nacen con las patas separadas, dificultando su movilidad y acceso a la ubre.",
                    "SÍNTOMA",
                    PatasAbiertasDetailActivity::class.java
                ),
                SymptomItem(
                    "Inflamación vulvar de la lechona (vulvovaginitis)",
                    "Hinchazón, enrojecimiento y secreción en la vulva de la lechona, generalmente por infección.",
                    "SÍNTOMA",
                    VulvovaginitisDetailActivity::class.java
                ),
                SymptomItem(
                    "Edema escrotal (inflamación del escroto)",
                    "Hinchazón del escroto, puede deberse a infecciones, traumatismos o problemas circulatorios.",
                    "SÍNTOMA",
                    EdemaEscrotalDetailActivity::class.java
                ),
                SymptomItem(
                    "Prolapso uterino",
                    "Salida del útero a través de la vulva tras el parto, requiere atención veterinaria inmediata.",
                    "SÍNTOMA",
                    ProlapsoUterinoDetailActivity::class.java
                ),
                SymptomItem(
                    "Malformaciones congénitas",
                    "Alteraciones estructurales presentes al nacimiento, pueden afectar la viabilidad de los lechones.",
                    "SÍNTOMA",
                    MalformacionesCongenitasDetailActivity::class.java
                ),
                SymptomItem(
                    "Temblor de recién nacido (tremor congénito)",
                    "Movimientos involuntarios o temblores en lechones recién nacidos, generalmente por infecciones virales.",
                    "SÍNTOMA",
                    TremorCongenitoDetailActivity::class.java
                ),
                SymptomItem(
                    "Gestación anormalmente prolongada (>118 días)",
                    "Duración de la gestación mayor a 118 días, puede deberse a alteraciones hormonales o fetales.",
                    "SÍNTOMA",
                    GestacionProlongadaDetailActivity::class.java
                )
            )
            else -> emptyList()
        }
        Log.d("SymptomsActivity", "Número de síntomas cargados: ${symptoms.size}")
        symptomsAdapter.updateSymptoms(symptoms)
    }

    override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return true
    }
} 