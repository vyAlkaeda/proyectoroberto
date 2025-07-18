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
            if (findViewById<CheckBox>(R.id.checkDebilidad).isChecked) sintomasNerviosos.add("Debilidad")
            if (findViewById<CheckBox>(R.id.checkConvulsiones).isChecked) sintomasNerviosos.add("Convulsiones")
            if (findViewById<CheckBox>(R.id.checkIncordinacion).isChecked) sintomasNerviosos.add("Incordinación")
            if (findViewById<CheckBox>(R.id.checkParalisis).isChecked) sintomasNerviosos.add("Parálisis")
            if (findViewById<CheckBox>(R.id.checkDecubito).isChecked) sintomasNerviosos.add("Decúbito lateral")
            if (findViewById<CheckBox>(R.id.checkTemblores).isChecked) sintomasNerviosos.add("Temblores")
            if (findViewById<CheckBox>(R.id.checkCabezaLado).isChecked) sintomasNerviosos.add("Cabeza de lado")
            if (findViewById<CheckBox>(R.id.checkOpistotono).isChecked) sintomasNerviosos.add("Opistótono")
            if (findViewById<CheckBox>(R.id.checkRigidez).isChecked) sintomasNerviosos.add("Rigidez muscular")
            if (findViewById<CheckBox>(R.id.checkPedaleo).isChecked) sintomasNerviosos.add("Pedaleo")
            if (findViewById<CheckBox>(R.id.checkDepresion).isChecked) sintomasNerviosos.add("Depresión")
            if (findViewById<CheckBox>(R.id.checkPosturasAnormales).isChecked) sintomasNerviosos.add("Posturas anormales")
            if (findViewById<CheckBox>(R.id.checkNistagmo).isChecked) sintomasNerviosos.add("Nistagmo")
            if (findViewById<CheckBox>(R.id.checkCabezaAgachada).isChecked) sintomasNerviosos.add("Cabeza agachada")
            if (findViewById<CheckBox>(R.id.checkLetargo).isChecked) sintomasNerviosos.add("Letargo")
            if (findViewById<CheckBox>(R.id.checkMarchaCirculos).isChecked) sintomasNerviosos.add("Marcha en círculos")
            if (findViewById<CheckBox>(R.id.checkMarchaReversa).isChecked) sintomasNerviosos.add("Marcha en reversa")

            // Síntomas Musculoesqueléticos
            if (findViewById<CheckBox>(R.id.checkCojeras).isChecked) sintomasMusculoesqueleticos.add("Cojeras")
            if (findViewById<CheckBox>(R.id.checkResistencia).isChecked) sintomasMusculoesqueleticos.add("Resistencia a moverse")
            if (findViewById<CheckBox>(R.id.checkBursitis).isChecked) sintomasMusculoesqueleticos.add("Bursitis")
            if (findViewById<CheckBox>(R.id.checkInflamacionPierna).isChecked) sintomasMusculoesqueleticos.add("Inflamación de pierna")
            if (findViewById<CheckBox>(R.id.checkArticulaciones).isChecked) sintomasMusculoesqueleticos.add("Articulaciones inflamadas")

            // Síntomas Digestivos
            if (findViewById<CheckBox>(R.id.checkAnorexia).isChecked) sintomasDigestivos.add("Anorexia")
            if (findViewById<CheckBox>(R.id.checkBajoConsumo).isChecked) sintomasDigestivos.add("Bajo consumo")
            if (findViewById<CheckBox>(R.id.checkPerdidaPeso).isChecked) sintomasDigestivos.add("Pérdida de peso")
            if (findViewById<CheckBox>(R.id.checkCrecimientoLento).isChecked) sintomasDigestivos.add("Crecimiento lento")
            if (findViewById<CheckBox>(R.id.checkVomito).isChecked) sintomasDigestivos.add("Vómito")
            if (findViewById<CheckBox>(R.id.checkDiarreaLiquida).isChecked) sintomasDigestivos.add("Diarrea líquida")
            if (findViewById<CheckBox>(R.id.checkDiarreaGelatinosa).isChecked) sintomasDigestivos.add("Diarrea gelatinosa")
            if (findViewById<CheckBox>(R.id.checkDiarreaCremosa).isChecked) sintomasDigestivos.add("Diarrea cremosa")
            if (findViewById<CheckBox>(R.id.checkDiarreaVerdosa).isChecked) sintomasDigestivos.add("Diarrea verdosa")
            if (findViewById<CheckBox>(R.id.checkDiarreaAmarilla).isChecked) sintomasDigestivos.add("Diarrea amarilla")
            if (findViewById<CheckBox>(R.id.checkDiarreaGris).isChecked) sintomasDigestivos.add("Diarrea gris")
            if (findViewById<CheckBox>(R.id.checkDiarreaCafe).isChecked) sintomasDigestivos.add("Diarrea café")
            if (findViewById<CheckBox>(R.id.checkDiarreaNegra).isChecked) sintomasDigestivos.add("Diarrea negra")
            if (findViewById<CheckBox>(R.id.checkDiarreaSangre).isChecked) sintomasDigestivos.add("Diarrea con sangre")
            if (findViewById<CheckBox>(R.id.checkDiarreaBlanquecina).isChecked) sintomasDigestivos.add("Diarrea blanquecina")

            // Síntomas Reproductivos
            if (findViewById<CheckBox>(R.id.checkAbortoPrimer).isChecked) sintomasReproductivos.add("Aborto primer tercio")
            if (findViewById<CheckBox>(R.id.checkAbortoSegundo).isChecked) sintomasReproductivos.add("Aborto segundo tercio")
            if (findViewById<CheckBox>(R.id.checkAbortoTercer).isChecked) sintomasReproductivos.add("Aborto tercer tercio")
            if (findViewById<CheckBox>(R.id.checkCerdaVacia).isChecked) sintomasReproductivos.add("Cerda vacía")
            if (findViewById<CheckBox>(R.id.checkAnestro).isChecked) sintomasReproductivos.add("Anestro")
            if (findViewById<CheckBox>(R.id.checkCeloSilencioso).isChecked) sintomasReproductivos.add("Celo silencioso")

            // Síntomas Tegumentarios
            if (findViewById<CheckBox>(R.id.checkFiebre39).isChecked) sintomasTegumentarios.add("Fiebre 39.3-39.8°C")
            if (findViewById<CheckBox>(R.id.checkFiebre41).isChecked) sintomasTegumentarios.add("Fiebre 41.5°C")
            if (findViewById<CheckBox>(R.id.checkFiebre40).isChecked) sintomasTegumentarios.add("Fiebre 40.5-41.5°C")
            if (findViewById<CheckBox>(R.id.checkFiebre43).isChecked) sintomasTegumentarios.add("Fiebre 43°C")
            if (findViewById<CheckBox>(R.id.checkDeshidratacion).isChecked) sintomasTegumentarios.add("Deshidratación")
            if (findViewById<CheckBox>(R.id.checkConjuntivitis).isChecked) sintomasTegumentarios.add("Conjuntivitis")
            if (findViewById<CheckBox>(R.id.checkCeguera).isChecked) sintomasTegumentarios.add("Ceguera")
            if (findViewById<CheckBox>(R.id.checkPielPalida).isChecked) sintomasTegumentarios.add("Piel pálida")
            if (findViewById<CheckBox>(R.id.checkPielAmarilla).isChecked) sintomasTegumentarios.add("Piel amarilla")
            if (findViewById<CheckBox>(R.id.checkPielAzul).isChecked) sintomasTegumentarios.add("Piel azul")

            // Síntomas Respiratorios
            if (findViewById<CheckBox>(R.id.checkDisnea).isChecked) sintomasRespiratorios.add("Disnea")
            if (findViewById<CheckBox>(R.id.checkTaquipnea).isChecked) sintomasRespiratorios.add("Taquipnea")
            if (findViewById<CheckBox>(R.id.checkTosSeca).isChecked) sintomasRespiratorios.add("Tos seca")
            if (findViewById<CheckBox>(R.id.checkTosProductiva).isChecked) sintomasRespiratorios.add("Tos productiva")
            if (findViewById<CheckBox>(R.id.checkEstornudo).isChecked) sintomasRespiratorios.add("Estornudo")
            if (findViewById<CheckBox>(R.id.checkEscurrimientoNasal).isChecked) sintomasRespiratorios.add("Escurrimiento nasal")
            if (findViewById<CheckBox>(R.id.checkMuerteSubita).isChecked) sintomasRespiratorios.add("Muerte súbita")

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

        // Análisis basado en síntomas comunes
        val sintomasNerviosos = sintomas.any { it.contains("Convulsión") || it.contains("Parálisis") || it.contains("Temblor") }
        val sintomasDigestivos = sintomas.any { it.contains("Diarrea") || it.contains("Vómito") || it.contains("Anorexia") }
        val sintomasRespiratorios = sintomas.any { it.contains("Tos") || it.contains("Disnea") || it.contains("Estornudo") }
        val sintomasReproductivos = sintomas.any { it.contains("Aborto") || it.contains("Celo") || it.contains("Cerda vacía") }
        val fiebre = sintomas.any { it.contains("Fiebre") }

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
        if (sintomasDigestivos && sintomas.any { it.contains("Diarrea verdosa") }) {
            enfermedades.add("Circovirus Porcino")
        }
        if (sintomasDigestivos && sintomas.any { it.contains("Diarrea amarilla") }) {
            enfermedades.add("Salmonelosis")
        }
        if (sintomas.any { it.contains("Cojeras") } && fiebre) {
            enfermedades.add("Erisipela")
        }
        if (sintomas.any { it.contains("Muerte súbita") }) {
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