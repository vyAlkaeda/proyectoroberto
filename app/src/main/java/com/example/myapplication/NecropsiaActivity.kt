package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityNecropsiaBinding

class NecropsiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNecropsiaBinding
    private lateinit var necropsiaAdapter: NecropsiaAdapter
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
        binding = ActivityNecropsiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerViews()
        loadNecropsiaForCategory("Digestivo") // Cargar digestivo por defecto
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Necropsia"
    }

    private fun setupRecyclerViews() {
        categoryAdapter = CategoryAdapter(categories) { category ->
            loadNecropsiaForCategory(category)
        }
        binding.categoriesRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(this@NecropsiaActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        necropsiaAdapter = NecropsiaAdapter(emptyList()) { selectedClass ->
            startActivity(Intent(this, selectedClass))
        }
        binding.necropsiaRecyclerView.apply {
            adapter = necropsiaAdapter
            layoutManager = LinearLayoutManager(this@NecropsiaActivity)
        }
    }

    private fun loadNecropsiaForCategory(category: String) {
        val necropsia = when (category) {
            "Cardíaco" -> listOf(
                NecropsiaItem(
                    "EPICARDITIS FIBRINO PURULENTA",
                    "Inflamación del epicardio, la membrana que cubre el corazón con mayor presencia de liquido y fibrina.",
                    "LESIÓN",
                    EpicarditisDetailActivity::class.java
                )
            )
            "Digestivo" -> listOf(
                NecropsiaItem(
                    "LINFADENITIS MESENTERICA",
                    "Inflamación de los ganglios linfáticos del mesenterio, generalmente asociado a un proceso infeccioso de origen viral o bacteriano",
                    "LESIÓN",
                    LinfadenitisDetailActivity::class.java
                ),
                NecropsiaItem(
                    "EDEMA DE LINFONODULOS BRONQUIALES",
                    "Inflamación de los ganglios o linfonodulos presentes en los bronquios.",
                    "LESIÓN",
                    EdemaBronquialesDetailActivity::class.java
                ),
                NecropsiaItem(
                    "INFARTO ESPLENICO",
                    "Muerte de tejido (necrosis) en el bazo por obstrucción del flujo sanguíneo; se puede complicar por pancreatitis.",
                    "LESIÓN",
                    InfartoEsplenicoDetailActivity::class.java
                ),
                NecropsiaItem(
                    "INFARTOS DE VESICULA BILIAR",
                    "Gangrena vesicular, es una hemorragia dentro de la vesícula biliar que se produce por la erosión de la mucosa y la inflamación de la pared vesicular",
                    "LESIÓN",
                    InfartosVesiculaDetailActivity::class.java
                ),
                NecropsiaItem(
                    "PETEQUIA ESPLENICA",
                    "Son las manchas rojas o moradas parecidas a salpicadura de pintura que aparecen en el bazo a causa un sangrado anormal de los vasos sanguíneos.",
                    "LESIÓN",
                    PetequiaEsplenicaDetailActivity::class.java
                ),
                NecropsiaItem(
                    "PANCREATITIS",
                    "Inflamacion del pancreas, puede ser cronica o aguda.",
                    "LESIÓN",
                    PancreatitisDetailActivity::class.java
                ),
                NecropsiaItem(
                    "EDEMA DE MESOCOLON",
                    "Acumulacion de liquidos en los tejidos blandos del intestino. Puede presentarse como una gelatina transparente o amarillenta alrededor del intestino.",
                    "LESIÓN",
                    EdemaMesocolonDetailActivity::class.java
                ),
                NecropsiaItem(
                    "EDEMA DE LINFONODULOS ILEOCECALES",
                    "Inflamacion de los ganglios o linfonodulos que se ubican en el mesenterio y que siguen el trayecto del íleon hacia el ciego.",
                    "LESIÓN",
                    EdemaIleocecalesDetailActivity::class.java
                ),
                NecropsiaItem(
                    "EXUDADO SANGUINOLENTO DE INTESTINO DELGADO",
                    "El exudado es líquido que se filtra desde los vasos sanguíneos del intestino delgado hacia los tejidos cercanos. Este líquido está compuesto de células, proteínas y materiales sólidos. El exudado puede supurar a partir de incisiones o de zonas de infección o inflamación.",
                    "LESIÓN",
                    ExudadoDelgadoDetailActivity::class.java
                ),
                NecropsiaItem(
                    "EXUDADO SANGUINOLENTO DE INTESTINO GRUESO",
                    "El exudado es líquido que se filtra desde los vasos sanguíneos del intestino grueso hacia los tejidos cercanos. Este líquido está compuesto de células, proteínas y materiales sólidos. El exudado puede supurar a partir de incisiones o de zonas de infección o inflamación.",
                    "LESIÓN",
                    ExudadoGruesoDetailActivity::class.java
                ),
                NecropsiaItem(
                    "ULCERACION FOCAL DE INTESTINO DELGADO",
                    "Formacion de llagas que provoca la pérdida de la integridad de la mucosa del intestino delgado en un punto particular o area especifica",
                    "LESIÓN",
                    UlceracionFocalDetailActivity::class.java
                ),
                NecropsiaItem(
                    "ENTERITIS FIBRINONECROTICA",
                    "Es la inflamación del intestino delgado con presencia de materia amarilla en el lumen intestinal con manchas blancas o negras.",
                    "LESIÓN",
                    EnteritisFibrinonecroticoDetailActivity::class.java
                ),
                NecropsiaItem(
                    "HEMORRAGIA HEPATICA",
                    "Es el sangrado espontáneo del hígado, puede ser mortal.",
                    "LESIÓN",
                    HemorragiaHepaticaDetailActivity::class.java
                ),
                NecropsiaItem(
                    "MANCHAS DE LECHE EN HIGADO",
                    "Son áreas blanquecinas en el hígado distribuidas de forma multifocal que infiltran el parénquima hepático. Generalmente ocasionadas por la migracion de larvas de parasitos.",
                    "LESIÓN",
                    ManchasLecheDetailActivity::class.java
                ),
                NecropsiaItem(
                    "OCLUSION INTESTINAL",
                    "Es el bloqueo del intestino delgado o grueso que impide que los alimentos y las heces pasen a través de él. Puede ser parcial o total",
                    "LESIÓN",
                    OclusionIntestinalDetailActivity::class.java
                ),
                NecropsiaItem(
                    "ULCERAS ORALES",
                    "Son lesiones inflamatorias frecuentes de la mucosa oral, redondas u ovaladas, rodeadas por un halo eritematoso de fondo amarillo grisáceo. Se les conoce como aftas.",
                    "LESIÓN",
                    UlcerasOralesDetailActivity::class.java
                ),
                NecropsiaItem(
                    "NECROSIS PANCREATICA",
                    "Es una inflamación brusca del páncreas provocada por la activación dentro de él de las enzimas que produce para la digestión ocasionando la muerte del tejido por el insuficiente suministro de sangre. El páncreas se destruye",
                    "LESIÓN",
                    NecrosisPancreaticaDetailActivity::class.java
                )
            )
            else -> emptyList()
        }
        Log.d("NecropsiaActivity", "Número de lesiones cargadas: ${necropsia.size}")
        necropsiaAdapter.updateNecropsia(necropsia)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 