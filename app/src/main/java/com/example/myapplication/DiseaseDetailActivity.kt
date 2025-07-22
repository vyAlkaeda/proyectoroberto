package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.EdadFiltradoLogic
import com.example.myapplication.databinding.ActivityDiseaseDetailBinding
import org.json.JSONObject
import java.io.IOException

class DiseaseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiseaseDetailBinding
    private lateinit var symptomsAdapter: DiseaseSymptomAdapter
    private var diseaseName: String = ""
    private var tipoDiagnostico: String = "SINTOMAS"

    companion object {
        const val EXTRA_DISEASE_NAME = "disease_name"
        const val EXTRA_TIPO_DIAGNOSTICO = "tipo_diagnostico"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diseaseName = intent.getStringExtra(EXTRA_DISEASE_NAME) ?: ""
        tipoDiagnostico = intent.getStringExtra(EXTRA_TIPO_DIAGNOSTICO) ?: "SINTOMAS"

        if (diseaseName.isEmpty()) {
            finish()
            return
        }

        aplicarEstiloPorTipoDiagnostico()
        setupToolbar()
        setupRecyclerView()
        setupButtons()
        loadDiseaseData()
    }

    private fun aplicarEstiloPorTipoDiagnostico() {
        if (tipoDiagnostico == "NECROPSIA") {
            binding.toolbar.setBackgroundColor(resources.getColor(R.color.necro_primary))
            binding.btnTreatment.setBackgroundColor(resources.getColor(R.color.necro_primary))
        } else {
            binding.toolbar.setBackgroundColor(resources.getColor(R.color.primary))
            binding.btnTreatment.setBackgroundColor(resources.getColor(R.color.primary))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalles de Enfermedad"
    }

    private fun setupRecyclerView() {
        symptomsAdapter = DiseaseSymptomAdapter(tipoDiagnostico)
        binding.symptomsRecyclerView.apply {
            adapter = symptomsAdapter
            layoutManager = LinearLayoutManager(this@DiseaseDetailActivity)
        }
    }

    private fun setupButtons() {
        binding.btnTreatment.setOnClickListener {
            val intent = Intent(this, TreatmentActivity::class.java)
            intent.putExtra(TreatmentActivity.EXTRA_DISEASE_NAME, diseaseName)
            intent.putExtra(TreatmentActivity.EXTRA_TIPO_DIAGNOSTICO, tipoDiagnostico)
            startActivity(intent)
        }
    }

    private fun loadDiseaseData() {
        try {
            // Cargar datos desde el JSON
            val jsonString = assets.open("enfermedades.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val sistemas = jsonObject.getJSONObject("sistemas")

            var diseaseInfo: DiseaseInfo? = null
            val relatedSymptoms = mutableListOf<String>()

            // Buscar la enfermedad en todos los sistemas
            sistemas.keys().forEach { sistemaKey ->
                val sistema = sistemas.getJSONObject(sistemaKey)
                val sintomas = sistema.getJSONArray("sintomas")

                for (i in 0 until sintomas.length()) {
                    val sintoma = sintomas.getJSONObject(i)
                    val enfermedades = sintoma.getJSONArray("enfermedades")
                    
                    for (j in 0 until enfermedades.length()) {
                        val enfermedad = enfermedades.getString(j)
                        if (enfermedad.equals(diseaseName, ignoreCase = true)) {
                            if (diseaseInfo == null) {
                                diseaseInfo = DiseaseInfo(
                                    name = diseaseName,
                                    description = getDescriptionForDisease(diseaseName),
                                    system = sistemaKey
                                )
                            }
                            relatedSymptoms.add(sintoma.getString("nombre"))
                        }
                    }
                }
            }

            // Mostrar información de la enfermedad
            if (diseaseInfo != null) {
                binding.diseaseNameText.text = diseaseInfo.name
                binding.diseaseDescriptionText.text = diseaseInfo.description
                binding.diseaseSystemText.text = "Sistema: ${diseaseInfo.system}"
                
                // Mostrar síntomas relacionados
                if (relatedSymptoms.isNotEmpty()) {
                    val uniqueSymptoms = relatedSymptoms.distinct()
                    symptomsAdapter.updateSymptoms(uniqueSymptoms)
                    binding.symptomsSection.visibility = View.VISIBLE
                } else {
                    binding.symptomsSection.visibility = View.GONE
                }
            } else {
                // Si no se encuentra la enfermedad, mostrar información básica
                binding.diseaseNameText.text = diseaseName
                binding.diseaseDescriptionText.text = "No se encontró descripción específica para esta enfermedad."
                binding.diseaseSystemText.text = ""
                binding.symptomsSection.visibility = View.GONE
            }

        } catch (e: IOException) {
            Log.e("DiseaseDetailActivity", "Error al cargar datos de enfermedad", e)
            binding.diseaseNameText.text = diseaseName
            binding.diseaseDescriptionText.text = "Error al cargar información de la enfermedad."
            binding.diseaseSystemText.text = ""
            binding.symptomsSection.visibility = View.GONE
        }
    }

    private fun getDescriptionForDisease(diseaseName: String): String {
        return when (diseaseName.uppercase()) {
            "MICOPLASMA" -> "Enfermedad respiratoria crónica causada por Mycoplasma hyopneumoniae. Provoca neumonía enzoótica en cerdos, caracterizada por tos seca persistente y consolidación pulmonar."
            "PLEURONEUMONIA" -> "Infección respiratoria grave que afecta los pulmones y la pleura. Causa neumonía severa y puede provocar adherencias pleurales."
            "ENFERMEDAD DE GLÄSSER" -> "Enfermedad sistémica causada por Haemophilus parasuis. Afecta múltiples órganos y puede causar artritis, pericarditis y neumonía."
            "PASTEURELOSIS" -> "Infección causada por Pasteurella multocida. Puede manifestarse como neumonía, rinitis atrófica o septicemia."
            "ESTREPTOCOCOSIS" -> "Infección causada por Streptococcus suis. Puede provocar meningitis, artritis, neumonía y septicemia."
            "INFLUENZA PORCINA" -> "Enfermedad viral respiratoria aguda causada por el virus de la influenza A. Provoca fiebre alta, tos y dificultad respiratoria."
            "ESTRONGILOIDES" -> "Infección parasitaria causada por Strongyloides ransomi. Afecta principalmente a lechones jóvenes causando diarrea y neumonía."
            "TRUEPERELA PYOGENES" -> "Infección bacteriana oportunista que puede causar abscesos en múltiples órganos incluyendo pulmones."
            "PESTE PORCINA CLÁSICA" -> "Enfermedad viral altamente contagiosa que afecta a cerdos domésticos y salvajes. Causa fiebre, trastornos neurológicos y alta mortalidad."
            "SALMONELOSIS" -> "Infección bacteriana causada por Salmonella spp. Provoca enterocolitis, septicemia y puede afectar múltiples órganos."
            "COLIBACILOSIS" -> "Infección causada por Escherichia coli patógena. Común en lechones, causa diarrea severa y deshidratación."
            "DISENTERIA PORCINA" -> "Enfermedad entérica causada por Brachyspira hyodysenteriae. Provoca diarrea sanguinolenta y mucosa."
            "ILEITIS PROLIFERATIVA" -> "Enfermedad intestinal causada por Lawsonia intracellularis. Provoca proliferación de la mucosa intestinal y diarrea."
            else -> "Enfermedad que afecta a los cerdos. Para obtener información específica sobre el tratamiento, consulte con un veterinario especializado."
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    data class DiseaseInfo(
        val name: String,
        val description: String,
        val system: String
    )
}