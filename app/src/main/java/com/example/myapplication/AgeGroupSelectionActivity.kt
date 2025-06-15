package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAgeGroupSelectionBinding

class AgeGroupSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgeGroupSelectionBinding
    private lateinit var checkBoxes: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgeGroupSelectionBinding.inflate(layoutInflater)
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
        checkBoxes = listOf(
            binding.checkGestacion,
            binding.checkMaternidad,
            binding.checkDestete,
            binding.checkEngorda,
            binding.checkCuarentena,
            binding.checkTodas
        )

        checkBoxes.forEach { selectedCheckBox ->
            selectedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Desactivar listeners temporalmente
                    checkBoxes.forEach { it.setOnCheckedChangeListener(null) }

                    // Desmarcar todos los demás
                    checkBoxes.forEach { checkbox ->
                        checkbox.isChecked = checkbox == selectedCheckBox
                    }

                    // Restaurar listeners
                    setupCheckBoxes()

                    // Redirigir según el checkbox marcado
                    when (selectedCheckBox) {
                        binding.checkGestacion -> startActivity(Intent(this, GestacionActivity::class.java))
                        binding.checkMaternidad -> startActivity(Intent(this, MaternidadActivity::class.java))
                        binding.checkDestete -> startActivity(Intent(this, DesteteActivity::class.java))
                        binding.checkEngorda -> startActivity(Intent(this, EngordaActivity::class.java))
                        binding.checkCuarentena -> startActivity(Intent(this, CuarentenaActivity::class.java))
                        binding.checkTodas -> startActivity(Intent(this, SymptomsActivity::class.java))
                    }
                }
            }
        }
    }
}
