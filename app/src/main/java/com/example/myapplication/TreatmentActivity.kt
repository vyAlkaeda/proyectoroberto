package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityTreatmentBinding

class TreatmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTreatmentBinding
    private var diseaseName: String = ""
    private var tipoDiagnostico: String = "SINTOMAS"

    companion object {
        const val EXTRA_DISEASE_NAME = "disease_name"
        const val EXTRA_TIPO_DIAGNOSTICO = "tipo_diagnostico"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreatmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diseaseName = intent.getStringExtra(EXTRA_DISEASE_NAME) ?: ""
        tipoDiagnostico = intent.getStringExtra(EXTRA_TIPO_DIAGNOSTICO) ?: "SINTOMAS"

        if (diseaseName.isEmpty()) {
            finish()
            return
        }

        aplicarEstiloPorTipoDiagnostico()
        setupToolbar()
        setupButtons()
        loadTreatmentData()
    }

    private fun aplicarEstiloPorTipoDiagnostico() {
        if (tipoDiagnostico == "NECROPSIA") {
            binding.toolbar.setBackgroundColor(resources.getColor(R.color.necro_primary))
            binding.btnContactVet.setBackgroundColor(resources.getColor(R.color.necro_primary))
            binding.treatmentTitleText.setTextColor(resources.getColor(R.color.necro_primary_dark))
            binding.preventionTitleText.setTextColor(resources.getColor(R.color.necro_primary_dark))
            binding.warningTitleText.setTextColor(resources.getColor(R.color.necro_primary_dark))
        } else {
            binding.toolbar.setBackgroundColor(resources.getColor(R.color.primary))
            binding.btnContactVet.setBackgroundColor(resources.getColor(R.color.primary))
            binding.treatmentTitleText.setTextColor(resources.getColor(R.color.primary_dark))
            binding.preventionTitleText.setTextColor(resources.getColor(R.color.primary_dark))
            binding.warningTitleText.setTextColor(resources.getColor(R.color.primary_dark))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Tratamiento"
    }

    private fun setupButtons() {
        binding.btnContactVet.setOnClickListener {
            // Abrir aplicación de contactos o marcador telefónico
            val intent = Intent(Intent.ACTION_DIAL)
            startActivity(intent)
        }

        binding.btnBackToMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun loadTreatmentData() {
        binding.diseaseNameText.text = diseaseName
        
        val treatmentInfo = getTreatmentInfo(diseaseName)
        binding.treatmentDescriptionText.text = treatmentInfo.treatment
        binding.preventionDescriptionText.text = treatmentInfo.prevention
        binding.warningDescriptionText.text = treatmentInfo.warning
    }

    private fun getTreatmentInfo(diseaseName: String): TreatmentInfo {
        return when (diseaseName.uppercase()) {
            "MICOPLASMA" -> TreatmentInfo(
                treatment = """
                    • Antibioterapia con tilmicosina, tilosina o lincomicina bajo prescripción veterinaria
                    • Mejorar la ventilación y reducir el hacinamiento
                    • Medicación en agua o alimento según indicación veterinaria
                    • Aislamiento de animales afectados
                    • Tratamiento de soporte con antiinflamatorios si es necesario
                """.trimIndent(),
                prevention = """
                    • Vacunación preventiva según programa veterinario
                    • Mantener buena ventilación en las instalaciones
                    • Evitar el hacinamiento
                    • Cuarentena para animales nuevos
                    • Desinfección regular de instalaciones
                    • Control de temperatura y humedad
                """.trimIndent(),
                warning = "El Micoplasma es altamente contagioso. Es fundamental consultar con un veterinario para el diagnóstico correcto y tratamiento apropiado. No automedicar."
            )
            
            "PLEURONEUMONIA" -> TreatmentInfo(
                treatment = """
                    • Antibioterapia inmediata con penicilina, ampicilina o fluoroquinolonas
                    • Tratamiento antiinflamatorio
                    • Terapia de soporte con fluidos si hay deshidratación
                    • Aislamiento inmediato de animales afectados
                    • Monitoreo constante de signos vitales
                """.trimIndent(),
                prevention = """
                    • Vacunación según calendario veterinario
                    • Mantener instalaciones limpias y bien ventiladas
                    • Evitar estrés y hacinamiento
                    • Cuarentena estricta para animales nuevos
                    • Desinfección regular con productos apropiados
                """.trimIndent(),
                warning = "Enfermedad grave que puede ser fatal. Requiere atención veterinaria inmediata. El tratamiento tardío puede resultar en muerte del animal."
            )
            
            "ENFERMEDAD DE GLÄSSER" -> TreatmentInfo(
                treatment = """
                    • Antibioterapia con amoxicilina, ceftiofur o fluoroquinolonas
                    • Tratamiento antiinflamatorio para reducir la inflamación articular
                    • Terapia de soporte con analgésicos
                    • Aislamiento de animales afectados
                    • Monitoreo de función respiratoria y articular
                """.trimIndent(),
                prevention = """
                    • Vacunación específica contra Haemophilus parasuis
                    • Reducir factores de estrés
                    • Mantener buena calidad del aire
                    • Desinfección regular de instalaciones
                    • Cuarentena para animales nuevos
                """.trimIndent(),
                warning = "Enfermedad sistémica grave que afecta múltiples órganos. Requiere diagnóstico veterinario y tratamiento específico inmediato."
            )
            
            "SALMONELOSIS" -> TreatmentInfo(
                treatment = """
                    • Antibioterapia específica según antibiograma
                    • Rehidratación oral o parenteral
                    • Probióticos para restaurar flora intestinal
                    • Electrolitos para corregir desequilibrios
                    • Aislamiento estricto de animales afectados
                """.trimIndent(),
                prevention = """
                    • Control estricto de la higiene en alimentos y agua
                    • Desinfección regular de instalaciones
                    • Control de roedores e insectos
                    • Cuarentena para animales nuevos
                    • Vacunación preventiva en granjas de alto riesgo
                """.trimIndent(),
                warning = "Zoonosis - puede transmitirse a humanos. Usar equipo de protección. Notificar a autoridades sanitarias si es necesario."
            )
            
            "COLIBACILOSIS" -> TreatmentInfo(
                treatment = """
                    • Antibioterapia según sensibilidad (colistina, neomicina, apramicina)
                    • Rehidratación inmediata con electrolitos
                    • Probióticos para restaurar flora intestinal
                    • Separación de lechones afectados
                    • Mejora de condiciones de higiene
                """.trimIndent(),
                prevention = """
                    • Manejo higiénico del parto
                    • Desinfección del ombligo al nacer
                    • Calostro adecuado en las primeras horas
                    • Mantener instalaciones limpias y secas
                    • Vacunación de madres gestantes
                """.trimIndent(),
                warning = "Muy común en lechones recién nacidos. La deshidratación puede ser fatal. Requiere tratamiento veterinario inmediato."
            )
            
            "DISENTERIA PORCINA" -> TreatmentInfo(
                treatment = """
                    • Antibioterapia con tiamulina, valnemulina o lincomicina
                    • Rehidratación con electrolitos
                    • Modificación de la dieta (reducir fibra)
                    • Aislamiento de animales afectados
                    • Limpieza y desinfección intensiva
                """.trimIndent(),
                prevention = """
                    • Control estricto de la introducción de animales nuevos
                    • Desinfección regular con productos específicos
                    • Control de vectores (roedores, aves)
                    • Manejo adecuado de purines
                    • Vacunación en granjas endémicas
                """.trimIndent(),
                warning = "Enfermedad altamente contagiosa. Puede causar grandes pérdidas económicas. Requiere medidas de bioseguridad estrictas."
            )
            
            "PESTE PORCINA CLÁSICA" -> TreatmentInfo(
                treatment = """
                    • NO EXISTE TRATAMIENTO ESPECÍFICO
                    • Medidas de soporte sintomático únicamente
                    • Aislamiento inmediato y estricto
                    • Notificación obligatoria a autoridades sanitarias
                    • Seguir protocolos oficiales de control
                """.trimIndent(),
                prevention = """
                    • Vacunación según normativas locales
                    • Bioseguridad estricta en granjas
                    • Control de fauna silvestre
                    • Cuarentena estricta para animales nuevos
                    • Desinfección rigurosa de vehículos y personas
                """.trimIndent(),
                warning = "ENFERMEDAD DE NOTIFICACIÓN OBLIGATORIA. Contactar inmediatamente al servicio veterinario oficial. Puede requerir sacrificio sanitario."
            )
            
            else -> TreatmentInfo(
                treatment = """
                    • Consultar inmediatamente con un veterinario especializado
                    • Aislamiento de animales afectados
                    • Mejorar condiciones de manejo e higiene
                    • Seguir estrictamente las indicaciones veterinarias
                    • Monitoreo constante del estado de los animales
                """.trimIndent(),
                prevention = """
                    • Mantener un programa de vacunación actualizado
                    • Implementar medidas de bioseguridad
                    • Desinfección regular de instalaciones
                    • Control de la calidad del agua y alimento
                    • Cuarentena para animales nuevos
                """.trimIndent(),
                warning = "Para un diagnóstico preciso y tratamiento adecuado, es esencial consultar con un veterinario especializado en porcinos."
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    data class TreatmentInfo(
        val treatment: String,
        val prevention: String,
        val warning: String
    )
}