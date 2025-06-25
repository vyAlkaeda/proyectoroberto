package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class DiagnosticoProfesionalActivity : AppCompatActivity() {
    private lateinit var checkBox1: CheckBox
    private lateinit var checkBox2: CheckBox
    private lateinit var checkBox3: CheckBox
    private lateinit var checkBox4: CheckBox
    private lateinit var radioGroupAfectados: RadioGroup
    private lateinit var radioGroupMortalidad: RadioGroup
    private lateinit var checkBoxGestacion: CheckBox
    private lateinit var checkBoxMaternidad: CheckBox
    private lateinit var checkBoxDestete: CheckBox
    private lateinit var checkBoxEngorda: CheckBox
    private lateinit var checkBoxCuarentena: CheckBox
    private lateinit var buttonContinuar: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnostico_profesional)

        // Inicializar vistas
        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        checkBox1 = findViewById(R.id.checkBox1)
        checkBox2 = findViewById(R.id.checkBox2)
        checkBox3 = findViewById(R.id.checkBox3)
        checkBox4 = findViewById(R.id.checkBox4)
        radioGroupAfectados = findViewById(R.id.radioGroupAfectados)
        radioGroupMortalidad = findViewById(R.id.radioGroupMortalidad)
        checkBoxGestacion = findViewById(R.id.checkBoxGestacion)
        checkBoxMaternidad = findViewById(R.id.checkBoxMaternidad)
        checkBoxDestete = findViewById(R.id.checkBoxDestete)
        checkBoxEngorda = findViewById(R.id.checkBoxEngorda)
        checkBoxCuarentena = findViewById(R.id.checkBoxCuarentena)
        buttonContinuar = findViewById(R.id.buttonContinuar)
    }

    private fun setupListeners() {
        buttonContinuar.setOnClickListener {
            val intent = Intent(this, ForoDiagnosticoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateAnswers(): Boolean {
        // Validar que al menos una opción esté seleccionada en cada pregunta
        val edadSeleccionada = checkBox1.isChecked || checkBox2.isChecked || 
                             checkBox3.isChecked || checkBox4.isChecked
        
        val afectadosSeleccionado = radioGroupAfectados.checkedRadioButtonId != -1
        val mortalidadSeleccionada = radioGroupMortalidad.checkedRadioButtonId != -1
        
        val areaSeleccionada = checkBoxGestacion.isChecked || checkBoxMaternidad.isChecked || 
                              checkBoxDestete.isChecked || checkBoxEngorda.isChecked || 
                              checkBoxCuarentena.isChecked

        if (!edadSeleccionada) {
            Toast.makeText(this, "Por favor seleccione la edad de los cerdos", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!afectadosSeleccionado) {
            Toast.makeText(this, "Por favor seleccione el número de cerdos afectados", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!mortalidadSeleccionada) {
            Toast.makeText(this, "Por favor seleccione el incremento de mortalidad", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!areaSeleccionada) {
            Toast.makeText(this, "Por favor seleccione al menos un área de producción afectada", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun processAnswers() {
        // Recolectar respuestas
        val edadesSeleccionadas = mutableListOf<String>()
        if (checkBox1.isChecked) edadesSeleccionadas.add("1-4 semanas")
        if (checkBox2.isChecked) edadesSeleccionadas.add("5-10 semanas")
        if (checkBox3.isChecked) edadesSeleccionadas.add("11-16 semanas")
        if (checkBox4.isChecked) edadesSeleccionadas.add("17-22 semanas")

        val afectados = when (radioGroupAfectados.checkedRadioButtonId) {
            R.id.radioButtonMas10 -> "MAS DEL 10%"
            R.id.radioButtonMenos10 -> "MENOS DEL 10%"
            else -> ""
        }

        val mortalidad = when (radioGroupMortalidad.checkedRadioButtonId) {
            R.id.radioButtonMas5 -> "MAS DEL 5%"
            R.id.radioButtonMenos5 -> "MENOS DEL 5%"
            else -> ""
        }

        val areasSeleccionadas = mutableListOf<String>()
        if (checkBoxGestacion.isChecked) areasSeleccionadas.add("GESTACION")
        if (checkBoxMaternidad.isChecked) areasSeleccionadas.add("MATERNIDAD")
        if (checkBoxDestete.isChecked) areasSeleccionadas.add("DESTETE")
        if (checkBoxEngorda.isChecked) areasSeleccionadas.add("ENGORDA")
        if (checkBoxCuarentena.isChecked) areasSeleccionadas.add("CUARENTENA")

        // Crear el mensaje del foro
        val mensaje = """
            Resultados del Diagnóstico:
            
            Edad de los cerdos: ${edadesSeleccionadas.joinToString(", ")}
            Número de cerdos afectados: $afectados
            Incremento de mortalidad: $mortalidad
            Áreas de producción afectadas: ${areasSeleccionadas.joinToString(", ")}
        """.trimIndent()

        // Guardar en Firestore
        val question = ForumQuestion(
            title = "Nuevo Diagnóstico",
            content = mensaje,
            timestamp = Timestamp.now(),
            author = "Usuario" // Aquí podrías obtener el usuario actual si tienes autenticación
        )

        db.collection("forum_questions")
            .add(question)
            .addOnSuccessListener {
                Toast.makeText(this, "Diagnóstico publicado en el foro", Toast.LENGTH_SHORT).show()
                // Navegar a la actividad del foro de diagnósticos
                val intent = Intent(this, ForoDiagnosticoActivity::class.java)
                startActivity(intent)
                finish() // Cerrar la actividad actual
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al publicar el diagnóstico", Toast.LENGTH_SHORT).show()
            }
    }
} 