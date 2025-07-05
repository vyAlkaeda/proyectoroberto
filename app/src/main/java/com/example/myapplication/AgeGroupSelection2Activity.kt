package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAgeGroupSelection2Binding

class AgeGroupSelection2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityAgeGroupSelection2Binding
    private lateinit var checkBoxes: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgeGroupSelection2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCheckBoxes()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Selecciona un grupo de Edad"

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupCheckBoxes() {
        // Inicializar la lista de checkboxes
        checkBoxes = listOf(
            binding.checkGestacion,
            binding.checkMaternidad,
            binding.checkDestete,
            binding.checkEngorda,
            binding.checkCuarentena,
            binding.checkTodas
        )

        checkBoxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Desmarcar los demás checkboxes
                    checkBoxes.forEach { other ->
                        if (other != checkbox) other.isChecked = false
                    }
                    // Obtener el texto del grupo de edad seleccionado
                    val edadSeleccionada = when (checkbox) {
                        binding.checkGestacion -> "Gestación"
                        binding.checkMaternidad -> "Maternidad"
                        binding.checkDestete -> "Destete"
                        binding.checkEngorda -> "Engorda"
                        binding.checkCuarentena -> "Cuarentena"
                        binding.checkTodas -> "Todas las edades"
                        else -> "Todas las edades"
                    }
                    // Obtener el nombre de la actividad destino
                    val destino = intent.getStringExtra("DESTINO_ACTIVITY")
                    if (destino != null) {
                        val destinoClass = Class.forName(destino)
                        val intentDestino = Intent(this@AgeGroupSelection2Activity, destinoClass)
                        intentDestino.putExtra("EDAD_SELECCIONADA", edadSeleccionada)
                        startActivity(intentDestino)
                    }
                }
            }
        }
    }
}