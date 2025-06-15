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

        // Configurar el comportamiento del checkbox "TODAS"
        binding.checkTodas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Si se marca "TODAS", desmarcar las demás y navegar a SymptomsActivity
                checkBoxes.forEach { checkbox ->
                    if (checkbox != binding.checkTodas) {
                        checkbox.isChecked = false
                    }
                }
                // Navegar a SymptomsActivity
                startActivity(Intent(this, NecropsiaActivity::class.java))
            }
        }

        // Configurar el comportamiento de los demás checkboxes
        checkBoxes.forEach { checkbox ->
            if (checkbox != binding.checkTodas) {
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Si se marca cualquier otra opción, desmarcar "TODAS"
                        binding.checkTodas.isChecked = false
                    }
                }
            }
        }
    }
}