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
        "Reproductivo",
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