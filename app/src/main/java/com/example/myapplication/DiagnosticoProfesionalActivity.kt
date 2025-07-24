package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.DiagnosticoData
import com.example.myapplication.data.UserData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class DiagnosticoProfesionalActivity : AppCompatActivity() {

    private lateinit var radioGroupVivo: RadioGroup
    private lateinit var containerPreguntasAdicionales: LinearLayout
    private lateinit var btnDiagnostico: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnostico_profesional)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Inicializar vistas
        radioGroupVivo = findViewById(R.id.radioGroupVivo)
        containerPreguntasAdicionales = findViewById(R.id.containerPreguntasAdicionales)
        btnDiagnostico = findViewById(R.id.btnDiagnostico)

        // Cargar datos del usuario
        cargarDatosUsuario()

        // Configurar listener para la pregunta inicial
        radioGroupVivo.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioVivoSi -> {
                    containerPreguntasAdicionales.visibility = View.VISIBLE
                    btnDiagnostico.visibility = View.VISIBLE
                }
                R.id.radioVivoNo -> {
                    containerPreguntasAdicionales.visibility = View.GONE
                    btnDiagnostico.visibility = View.VISIBLE
                    // Mostrar mensaje para casos de muerte
                    mostrarMensajeMuerte()
                }
            }
        }

        // Configurar botón de diagnóstico
        btnDiagnostico.setOnClickListener {
            realizarDiagnostico()
        }
    }

    private fun cargarDatosUsuario() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userData = document.toObject(UserData::class.java)
                    }
                }
                .addOnFailureListener { e ->
                    // Si falla, crear datos básicos
                    userData = UserData(
                        uid = currentUser.uid,
                        fullName = "Usuario",
                        email = currentUser.email ?: "",
                        points = 0,
                        profileImageUrl = "",
                        cardNumber = ""
                    )
                }
        }
    }

    private fun mostrarMensajeMuerte() {
        Toast.makeText(
            this,
            "En caso de muerte del animal, se recomienda realizar necropsia para diagnóstico preciso",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun realizarDiagnostico() {
        // Mostrar indicador de progreso
        btnDiagnostico.isEnabled = false
        btnDiagnostico.text = "PROCESANDO..."

        // Obtener respuestas del cuestionario
        val respuestas = obtenerRespuestas()
        
        // Realizar análisis de síntomas
        val diagnostico = analizarSintomas(respuestas)
        
        // Guardar en Firestore y mostrar resultados
        guardarDiagnostico(respuestas, diagnostico)
    }

    private fun obtenerRespuestas(): MutableMap<String, Any> {
        val respuestas = mutableMapOf<String, Any>()

        // Pregunta 1: ¿Está vivo?
        respuestas["vivo"] = when (radioGroupVivo.checkedRadioButtonId) {
            R.id.radioVivoSi -> "SÍ"
            R.id.radioVivoNo -> "NO"
            else -> "NO SELECCIONADO"
        }

        // Solo continuar si está vivo
        if (respuestas["vivo"] == "SÍ") {
            // Pregunta 2: Edad
            respuestas["edad"] = when (findViewById<RadioGroup>(R.id.radioGroupEdad).checkedRadioButtonId) {
                R.id.radioEdad1 -> "1-4 semanas"
                R.id.radioEdad2 -> "5-10 semanas"
                R.id.radioEdad3 -> "11-16 semanas"
                R.id.radioEdad4 -> "17-22 semanas"
                else -> "NO SELECCIONADO"
            }

            // Pregunta 3: Número de afectados
            respuestas["afectados"] = when (findViewById<RadioGroup>(R.id.radioGroupAfectados).checkedRadioButtonId) {
                R.id.radioAfectadosMas -> "Más del 10%"
                R.id.radioAfectadosMenos -> "Menos del 10%"
                else -> "NO SELECCIONADO"
            }

            // Pregunta 4: Mortalidad
            respuestas["mortalidad"] = when (findViewById<RadioGroup>(R.id.radioGroupMortalidad).checkedRadioButtonId) {
                R.id.radioMortalidadMas -> "Más del 5%"
                R.id.radioMortalidadMenos -> "Menos del 5%"
                else -> "NO SELECCIONADO"
            }

            // Pregunta 5: Área de producción
            respuestas["area"] = when (findViewById<RadioGroup>(R.id.radioGroupArea).checkedRadioButtonId) {
                R.id.radioAreaGestacion -> "Gestación"
                R.id.radioAreaMaternidad -> "Maternidad"
                R.id.radioAreaDestete -> "Destete"
                R.id.radioAreaEngorda -> "Engorda"
                R.id.radioAreaCuarentena -> "Cuarentena"
                else -> "NO SELECCIONADO"
            }

            // Síntomas seleccionados por categoría
            val sintomasNerviosos = mutableListOf<String>()
            val sintomasMusculoesqueleticos = mutableListOf<String>()
            val sintomasDigestivos = mutableListOf<String>()
            val sintomasReproductivos = mutableListOf<String>()
            val sintomasTegumentarios = mutableListOf<String>()
            val sintomasRespiratorios = mutableListOf<String>()

            // Síntomas Nerviosos
            if (findViewById<CheckBox>(R.id.checkDebilidad).isChecked) sintomasNerviosos.add("DEBILIDAD")
            if (findViewById<CheckBox>(R.id.checkConvulsiones).isChecked) sintomasNerviosos.add("CONVULSIONES")
            if (findViewById<CheckBox>(R.id.checkIncordinacion).isChecked) sintomasNerviosos.add("INCOORDINACION")
            if (findViewById<CheckBox>(R.id.checkParalisis).isChecked) sintomasNerviosos.add("PARALISIS")
            if (findViewById<CheckBox>(R.id.checkDecubito).isChecked) sintomasNerviosos.add("DECUBITO LATERAL")
            if (findViewById<CheckBox>(R.id.checkTemblores).isChecked) sintomasNerviosos.add("TEMBLORES")
            if (findViewById<CheckBox>(R.id.checkCabezaLado).isChecked) sintomasNerviosos.add("CABEZA DE LADO")
            if (findViewById<CheckBox>(R.id.checkOpistotono).isChecked) sintomasNerviosos.add("OPISTOTONO")
            if (findViewById<CheckBox>(R.id.checkRigidez).isChecked) sintomasNerviosos.add("RIGIDEZ MUSCULAR")
            if (findViewById<CheckBox>(R.id.checkPedaleo).isChecked) sintomasNerviosos.add("PEDALEO")
            if (findViewById<CheckBox>(R.id.checkDepresion).isChecked) sintomasNerviosos.add("DEPRESION")
            if (findViewById<CheckBox>(R.id.checkPosturasAnormales).isChecked) sintomasNerviosos.add("POSTURAS ANORMALES")
            if (findViewById<CheckBox>(R.id.checkNistagmo).isChecked) sintomasNerviosos.add("NISTAGMO")
            if (findViewById<CheckBox>(R.id.checkCabezaAgachada).isChecked) sintomasNerviosos.add("CABEZA AGACHADA")
            if (findViewById<CheckBox>(R.id.checkLetargo).isChecked) sintomasNerviosos.add("LETARGO")
            if (findViewById<CheckBox>(R.id.checkMarchaCirculos).isChecked) sintomasNerviosos.add("MARCHA EN CIRCULOS")
            if (findViewById<CheckBox>(R.id.checkMarchaReversa).isChecked) sintomasNerviosos.add("MARCHA EN REVERSA")

            // Síntomas Musculoesqueléticos
            if (findViewById<CheckBox>(R.id.checkCojeras).isChecked) sintomasMusculoesqueleticos.add("COJERA")
            if (findViewById<CheckBox>(R.id.checkResistencia).isChecked) sintomasMusculoesqueleticos.add("RESISTENCIA A MOVERSE")
            if (findViewById<CheckBox>(R.id.checkBursitis).isChecked) sintomasMusculoesqueleticos.add("BURSITIS")
            if (findViewById<CheckBox>(R.id.checkInflamacionPierna).isChecked) sintomasMusculoesqueleticos.add("INFLAMACION DE PIERNA")
            if (findViewById<CheckBox>(R.id.checkArticulaciones).isChecked) sintomasMusculoesqueleticos.add("ARTICULACIONES INFLAMADAS")

            // Síntomas Digestivos
            if (findViewById<CheckBox>(R.id.checkAnorexia).isChecked) sintomasDigestivos.add("ANOREXIA")
            if (findViewById<CheckBox>(R.id.checkBajoConsumo).isChecked) sintomasDigestivos.add("BAJO CONSUMO")
            if (findViewById<CheckBox>(R.id.checkPerdidaPeso).isChecked) sintomasDigestivos.add("PERDIDA DE PESO")
            if (findViewById<CheckBox>(R.id.checkCrecimientoLento).isChecked) sintomasDigestivos.add("CRECIMIENTO LENTO")
            if (findViewById<CheckBox>(R.id.checkVomito).isChecked) sintomasDigestivos.add("VOMITO")
            if (findViewById<CheckBox>(R.id.checkDiarreaLiquida).isChecked) sintomasDigestivos.add("DIARREA LIQUIDA")
            if (findViewById<CheckBox>(R.id.checkDiarreaGelatinosa).isChecked) sintomasDigestivos.add("DIARREA GELATINOSA")
            if (findViewById<CheckBox>(R.id.checkDiarreaCremosa).isChecked) sintomasDigestivos.add("DIARREA CREMOSA")
            if (findViewById<CheckBox>(R.id.checkDiarreaVerdosa).isChecked) sintomasDigestivos.add("DIARREA VERDOSA")
            if (findViewById<CheckBox>(R.id.checkDiarreaAmarilla).isChecked) sintomasDigestivos.add("DIARREA AMARILLA")
            if (findViewById<CheckBox>(R.id.checkDiarreaGris).isChecked) sintomasDigestivos.add("DIARREA GRIS")
            if (findViewById<CheckBox>(R.id.checkDiarreaCafe).isChecked) sintomasDigestivos.add("DIARREA CAFE")
            if (findViewById<CheckBox>(R.id.checkDiarreaNegra).isChecked) sintomasDigestivos.add("DIARREA NEGRA")
            if (findViewById<CheckBox>(R.id.checkDiarreaSangre).isChecked) sintomasDigestivos.add("DIARREA CON SANGRE")
            if (findViewById<CheckBox>(R.id.checkDiarreaBlanquecina).isChecked) sintomasDigestivos.add("DIARREA BLANQUECINA")

            // Síntomas Reproductivos
            if (findViewById<CheckBox>(R.id.checkAbortoPrimer).isChecked) sintomasReproductivos.add("ABORTO PRIMER TERCIO")
            if (findViewById<CheckBox>(R.id.checkAbortoSegundo).isChecked) sintomasReproductivos.add("ABORTO SEGUNDO TERCIO")
            if (findViewById<CheckBox>(R.id.checkAbortoTercer).isChecked) sintomasReproductivos.add("ABORTO TERCER TERCIO")
            if (findViewById<CheckBox>(R.id.checkCerdaVacia).isChecked) sintomasReproductivos.add("CERDA VACIA")
            if (findViewById<CheckBox>(R.id.checkAnestro).isChecked) sintomasReproductivos.add("ANESTRO")
            if (findViewById<CheckBox>(R.id.checkCeloSilencioso).isChecked) sintomasReproductivos.add("CELO SILENCIOSO")

            // Síntomas Tegumentarios
            if (findViewById<CheckBox>(R.id.checkFiebre39).isChecked) sintomasTegumentarios.add("FIEBRE 39.3-39.8°C")
            if (findViewById<CheckBox>(R.id.checkFiebre41).isChecked) sintomasTegumentarios.add("FIEBRE 41.5°C")
            if (findViewById<CheckBox>(R.id.checkFiebre40).isChecked) sintomasTegumentarios.add("FIEBRE 40.5-41.5°C")
            if (findViewById<CheckBox>(R.id.checkFiebre43).isChecked) sintomasTegumentarios.add("FIEBRE 43°C")
            if (findViewById<CheckBox>(R.id.checkDeshidratacion).isChecked) sintomasTegumentarios.add("DESHIDRATACION")
            if (findViewById<CheckBox>(R.id.checkConjuntivitis).isChecked) sintomasTegumentarios.add("CONJUNTIVITIS")
            if (findViewById<CheckBox>(R.id.checkCeguera).isChecked) sintomasTegumentarios.add("CEGUERA")
            if (findViewById<CheckBox>(R.id.checkPielPalida).isChecked) sintomasTegumentarios.add("PIEL PALIDA")
            if (findViewById<CheckBox>(R.id.checkPielAmarilla).isChecked) sintomasTegumentarios.add("PIEL AMARILLA")
            if (findViewById<CheckBox>(R.id.checkPielAzul).isChecked) sintomasTegumentarios.add("PIEL AZUL")

            // Síntomas Respiratorios
            if (findViewById<CheckBox>(R.id.checkDisnea).isChecked) sintomasRespiratorios.add("DISNEA")
            if (findViewById<CheckBox>(R.id.checkTaquipnea).isChecked) sintomasRespiratorios.add("TAQUIPNEA")
            if (findViewById<CheckBox>(R.id.checkTosSeca).isChecked) sintomasRespiratorios.add("TOS SECA")
            if (findViewById<CheckBox>(R.id.checkTosProductiva).isChecked) sintomasRespiratorios.add("TOS PRODUCTIVA")
            if (findViewById<CheckBox>(R.id.checkEstornudo).isChecked) sintomasRespiratorios.add("ESTORNUDO")
            if (findViewById<CheckBox>(R.id.checkEscurrimientoNasal).isChecked) sintomasRespiratorios.add("ESCURRIMIENTO NASAL")
            if (findViewById<CheckBox>(R.id.checkMuerteSubita).isChecked) sintomasRespiratorios.add("MUERTE SUBITA")

            respuestas["sintomasNerviosos"] = sintomasNerviosos
            respuestas["sintomasMusculoesqueleticos"] = sintomasMusculoesqueleticos
            respuestas["sintomasDigestivos"] = sintomasDigestivos
            respuestas["sintomasReproductivos"] = sintomasReproductivos
            respuestas["sintomasTegumentarios"] = sintomasTegumentarios
            respuestas["sintomasRespiratorios"] = sintomasRespiratorios
        }

        return respuestas
    }

    private fun analizarSintomas(respuestas: MutableMap<String, Any>): MutableMap<String, Any> {
        val sintomasNerviosos = respuestas["sintomasNerviosos"] as? List<String> ?: emptyList()
        val sintomasDigestivos = respuestas["sintomasDigestivos"] as? List<String> ?: emptyList()
        val sintomasRespiratorios = respuestas["sintomasRespiratorios"] as? List<String> ?: emptyList()
        val sintomasReproductivos = respuestas["sintomasReproductivos"] as? List<String> ?: emptyList()
        val sintomasTegumentarios = respuestas["sintomasTegumentarios"] as? List<String> ?: emptyList()
        val edad = respuestas["edad"] as? String ?: ""
        val area = respuestas["area"] as? String ?: ""

        val diagnostico = StringBuilder()
        diagnostico.append("ANÁLISIS DE DIAGNÓSTICO PROFESIONAL\n\n")

        // Análisis por edad
        diagnostico.append("EDAD: $edad\n")
        diagnostico.append("ÁREA AFECTADA: $area\n\n")

        // Análisis de síntomas por sistema
        val todosLosSintomas = sintomasNerviosos + sintomasDigestivos + sintomasRespiratorios + 
                              sintomasReproductivos + sintomasTegumentarios + 
                              (respuestas["sintomasMusculoesqueleticos"] as? List<String> ?: emptyList())

        if (todosLosSintomas.isNotEmpty()) {
            diagnostico.append("SÍNTOMAS IDENTIFICADOS:\n")
            todosLosSintomas.forEach { sintoma ->
                diagnostico.append("• $sintoma\n")
            }
            diagnostico.append("\n")

            // Análisis de posibles enfermedades basado en síntomas
            val enfermedadesPosibles = analizarEnfermedades(todosLosSintomas)
            if (enfermedadesPosibles.isNotEmpty()) {
                diagnostico.append("POSIBLES ENFERMEDADES:\n")
                enfermedadesPosibles.forEach { enfermedad ->
                    diagnostico.append("• $enfermedad\n")
                }
                diagnostico.append("\n")
            }

            // Recomendaciones
            val recomendaciones = listOf(
                "Realizar examen físico completo",
                "Tomar muestras para análisis de laboratorio",
                "Implementar medidas de bioseguridad",
                "Consultar con veterinario especialista",
                "Considerar necropsia si hay mortalidad"
            )
            diagnostico.append("RECOMENDACIONES:\n")
            recomendaciones.forEach { recomendacion ->
                diagnostico.append("• $recomendacion\n")
            }

            respuestas["diagnosticoGenerado"] = diagnostico.toString()
            respuestas["enfermedadesPosibles"] = enfermedadesPosibles
            respuestas["recomendaciones"] = recomendaciones
        } else {
            diagnostico.append("No se han seleccionado síntomas específicos.\n")
            diagnostico.append("Se recomienda realizar examen físico completo.\n")
            
            respuestas["diagnosticoGenerado"] = diagnostico.toString()
            respuestas["enfermedadesPosibles"] = emptyList<String>()
            respuestas["recomendaciones"] = listOf("Realizar examen físico completo")
        }

        return respuestas
    }

    private fun analizarEnfermedades(sintomas: List<String>): List<String> {
        val enfermedades = mutableListOf<String>()

        // Análisis basado en síntomas comunes (usando nombres normalizados)
        val sintomasNerviosos = sintomas.any { it.contains("CONVULSIONES") || it.contains("PARALISIS") || it.contains("TEMBLOR") }
        val sintomasDigestivos = sintomas.any { it.contains("DIARREA") || it.contains("VOMITO") || it.contains("ANOREXIA") }
        val sintomasRespiratorios = sintomas.any { it.contains("TOS") || it.contains("DISNEA") || it.contains("ESTORNUDO") }
        val sintomasReproductivos = sintomas.any { it.contains("ABORTO") || it.contains("CELO") || it.contains("CERDA VACIA") }
        val fiebre = sintomas.any { it.contains("FIEBRE") }

        // Mapeo de síntomas a posibles enfermedades
        if (sintomasNerviosos && fiebre) {
            enfermedades.add("Estreptococosis")
        }
        if (sintomasRespiratorios && fiebre) {
            enfermedades.add("Pleuroneumonía")
        }
        if (sintomasReproductivos) {
            enfermedades.add("PRRS (Síndrome Reproductivo y Respiratorio Porcino)")
        }
        if (sintomasDigestivos && sintomas.any { it.contains("DIARREA VERDOSA") }) {
            enfermedades.add("Circovirus Porcino")
        }
        if (sintomasDigestivos && sintomas.any { it.contains("DIARREA AMARILLA") }) {
            enfermedades.add("Salmonelosis")
        }
        if (sintomas.any { it.contains("COJERA") } && fiebre) {
            enfermedades.add("Erisipela")
        }
        if (sintomas.any { it.contains("MUERTE SUBITA") }) {
            enfermedades.add("Clostridiosis")
        }

        return enfermedades.distinct()
    }

    private fun guardarDiagnostico(respuestas: MutableMap<String, Any>, diagnostico: MutableMap<String, Any>) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show()
            btnDiagnostico.isEnabled = true
            btnDiagnostico.text = "REALIZAR DIAGNÓSTICO"
            return
        }

        // Crear objeto DiagnosticoData
        val diagnosticoData = DiagnosticoData(
            userId = currentUser.uid,
            userName = userData?.fullName ?: "Usuario",
            userEmail = userData?.email ?: currentUser.email ?: "",
            fechaHora = Timestamp.now(),
            animalVivo = respuestas["vivo"] as? String ?: "",
            edadCerdos = respuestas["edad"] as? String ?: "",
            numeroAfectados = respuestas["afectados"] as? String ?: "",
            incrementoMortalidad = respuestas["mortalidad"] as? String ?: "",
            areaProduccion = respuestas["area"] as? String ?: "",
            sintomasNerviosos = respuestas["sintomasNerviosos"] as? List<String> ?: emptyList(),
            sintomasMusculoesqueleticos = respuestas["sintomasMusculoesqueleticos"] as? List<String> ?: emptyList(),
            sintomasDigestivos = respuestas["sintomasDigestivos"] as? List<String> ?: emptyList(),
            sintomasReproductivos = respuestas["sintomasReproductivos"] as? List<String> ?: emptyList(),
            sintomasTegumentarios = respuestas["sintomasTegumentarios"] as? List<String> ?: emptyList(),
            sintomasRespiratorios = respuestas["sintomasRespiratorios"] as? List<String> ?: emptyList(),
            diagnosticoGenerado = diagnostico["diagnosticoGenerado"] as? String ?: "",
            enfermedadesPosibles = diagnostico["enfermedadesPosibles"] as? List<String> ?: emptyList(),
            recomendaciones = diagnostico["recomendaciones"] as? List<String> ?: emptyList()
        )

        // Guardar en Firestore
        db.collection("diagnosticos")
            .add(diagnosticoData)
            .addOnSuccessListener { documentReference ->
                // Actualizar el ID del documento
                val diagnosticoDataConId = diagnosticoData.copy(id = documentReference.id)
                
                // Navegar al chat en vivo
                val intent = Intent(this, ChatEnVivoActivity::class.java)
                intent.putExtra(ChatEnVivoActivity.EXTRA_DIAGNOSTICO_DATA, diagnosticoDataConId)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar diagnóstico: ${e.message}", Toast.LENGTH_SHORT).show()
                btnDiagnostico.isEnabled = true
                btnDiagnostico.text = "REALIZAR DIAGNÓSTICO"
            }
    }
} 