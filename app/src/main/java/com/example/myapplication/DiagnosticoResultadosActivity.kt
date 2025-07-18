package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.DiagnosticoData
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DiagnosticoResultadosActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DIAGNOSTICO_DATA = "diagnostico_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnostico_resultados)

        // Obtener los datos del diagnóstico
        val diagnosticoData = intent.getParcelableExtra<DiagnosticoData>(EXTRA_DIAGNOSTICO_DATA)
        
        if (diagnosticoData != null) {
            mostrarResultados(diagnosticoData)
        } else {
            Toast.makeText(this, "Error al cargar los resultados", Toast.LENGTH_SHORT).show()
            finish()
        }

        setupButtons()
    }

    private fun mostrarResultados(diagnosticoData: DiagnosticoData) {
        // Información del usuario
        findViewById<android.widget.TextView>(R.id.tvUserName).text = "Usuario: ${diagnosticoData.userName}"
        findViewById<android.widget.TextView>(R.id.tvUserEmail).text = "Email: ${diagnosticoData.userEmail}"
        
        // Formatear fecha y hora
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val fechaHora = dateFormat.format(diagnosticoData.fechaHora.toDate())
        findViewById<android.widget.TextView>(R.id.tvFechaHora).text = "Fecha: $fechaHora"

        // Información epidemiológica
        findViewById<android.widget.TextView>(R.id.tvAnimalVivo).text = "Estado del animal: ${diagnosticoData.animalVivo}"
        findViewById<android.widget.TextView>(R.id.tvEdadCerdos).text = "Edad: ${diagnosticoData.edadCerdos}"
        findViewById<android.widget.TextView>(R.id.tvNumeroAfectados).text = "Número afectados: ${diagnosticoData.numeroAfectados}"
        findViewById<android.widget.TextView>(R.id.tvIncrementoMortalidad).text = "Incremento mortalidad: ${diagnosticoData.incrementoMortalidad}"
        findViewById<android.widget.TextView>(R.id.tvAreaProduccion).text = "Área producción: ${diagnosticoData.areaProduccion}"

        // Síntomas identificados
        findViewById<android.widget.TextView>(R.id.tvSintomasNerviosos).text = 
            "Síntomas Nerviosos: ${if (diagnosticoData.sintomasNerviosos.isNotEmpty()) diagnosticoData.sintomasNerviosos.joinToString(", ") else "Ninguno"}"
        
        findViewById<android.widget.TextView>(R.id.tvSintomasMusculoesqueleticos).text = 
            "Síntomas Musculoesqueléticos: ${if (diagnosticoData.sintomasMusculoesqueleticos.isNotEmpty()) diagnosticoData.sintomasMusculoesqueleticos.joinToString(", ") else "Ninguno"}"
        
        findViewById<android.widget.TextView>(R.id.tvSintomasDigestivos).text = 
            "Síntomas Digestivos: ${if (diagnosticoData.sintomasDigestivos.isNotEmpty()) diagnosticoData.sintomasDigestivos.joinToString(", ") else "Ninguno"}"
        
        findViewById<android.widget.TextView>(R.id.tvSintomasReproductivos).text = 
            "Síntomas Reproductivos: ${if (diagnosticoData.sintomasReproductivos.isNotEmpty()) diagnosticoData.sintomasReproductivos.joinToString(", ") else "Ninguno"}"
        
        findViewById<android.widget.TextView>(R.id.tvSintomasTegumentarios).text = 
            "Síntomas Tegumentarios: ${if (diagnosticoData.sintomasTegumentarios.isNotEmpty()) diagnosticoData.sintomasTegumentarios.joinToString(", ") else "Ninguno"}"
        
        findViewById<android.widget.TextView>(R.id.tvSintomasRespiratorios).text = 
            "Síntomas Respiratorios: ${if (diagnosticoData.sintomasRespiratorios.isNotEmpty()) diagnosticoData.sintomasRespiratorios.joinToString(", ") else "Ninguno"}"

        // Diagnóstico generado
        findViewById<android.widget.TextView>(R.id.tvEnfermedadesPosibles).text = 
            "Enfermedades Posibles: ${if (diagnosticoData.enfermedadesPosibles.isNotEmpty()) diagnosticoData.enfermedadesPosibles.joinToString(", ") else "No se identificaron enfermedades específicas"}"
        
        findViewById<android.widget.TextView>(R.id.tvRecomendaciones).text = 
            "Recomendaciones: ${if (diagnosticoData.recomendaciones.isNotEmpty()) diagnosticoData.recomendaciones.joinToString("\n• ") else "Realizar examen físico completo y consultar con veterinario especialista"}"
    }

    private fun setupButtons() {
        // Botón compartir
        findViewById<android.widget.Button>(R.id.btnCompartir).setOnClickListener {
            compartirDiagnostico()
        }

        // Botón nuevo diagnóstico
        findViewById<android.widget.Button>(R.id.btnNuevoDiagnostico).setOnClickListener {
            val intent = Intent(this, DiagnosticoProfesionalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Botón volver al menú principal
        findViewById<android.widget.Button>(R.id.btnVolver).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun compartirDiagnostico() {
        val diagnosticoData = intent.getParcelableExtra<DiagnosticoData>(EXTRA_DIAGNOSTICO_DATA)
        if (diagnosticoData != null) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val fechaHora = dateFormat.format(diagnosticoData.fechaHora.toDate())
            
            val textoCompartir = """
                DIAGNÓSTICO PROFESIONAL PORCINO
                
                Usuario: ${diagnosticoData.userName}
                Email: ${diagnosticoData.userEmail}
                Fecha: $fechaHora
                
                INFORMACIÓN EPIDEMIOLÓGICA:
                Estado del animal: ${diagnosticoData.animalVivo}
                Edad: ${diagnosticoData.edadCerdos}
                Número afectados: ${diagnosticoData.numeroAfectados}
                Incremento mortalidad: ${diagnosticoData.incrementoMortalidad}
                Área producción: ${diagnosticoData.areaProduccion}
                
                SÍNTOMAS IDENTIFICADOS:
                Nerviosos: ${if (diagnosticoData.sintomasNerviosos.isNotEmpty()) diagnosticoData.sintomasNerviosos.joinToString(", ") else "Ninguno"}
                Musculoesqueléticos: ${if (diagnosticoData.sintomasMusculoesqueleticos.isNotEmpty()) diagnosticoData.sintomasMusculoesqueleticos.joinToString(", ") else "Ninguno"}
                Digestivos: ${if (diagnosticoData.sintomasDigestivos.isNotEmpty()) diagnosticoData.sintomasDigestivos.joinToString(", ") else "Ninguno"}
                Reproductivos: ${if (diagnosticoData.sintomasReproductivos.isNotEmpty()) diagnosticoData.sintomasReproductivos.joinToString(", ") else "Ninguno"}
                Tegumentarios: ${if (diagnosticoData.sintomasTegumentarios.isNotEmpty()) diagnosticoData.sintomasTegumentarios.joinToString(", ") else "Ninguno"}
                Respiratorios: ${if (diagnosticoData.sintomasRespiratorios.isNotEmpty()) diagnosticoData.sintomasRespiratorios.joinToString(", ") else "Ninguno"}
                
                DIAGNÓSTICO:
                Enfermedades Posibles: ${if (diagnosticoData.enfermedadesPosibles.isNotEmpty()) diagnosticoData.enfermedadesPosibles.joinToString(", ") else "No se identificaron enfermedades específicas"}
                
                Recomendaciones: ${if (diagnosticoData.recomendaciones.isNotEmpty()) diagnosticoData.recomendaciones.joinToString("\n• ") else "Realizar examen físico completo y consultar con veterinario especialista"}
                
                Generado por: App Diagnóstico Profesional Porcino
            """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Diagnóstico Profesional Porcino")
                putExtra(Intent.EXTRA_TEXT, textoCompartir)
            }
            startActivity(Intent.createChooser(intent, "Compartir diagnóstico"))
        }
    }
} 