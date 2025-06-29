package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAgeGroupSelectionBinding
import com.example.myapplication.data.EtapaProductiva
import com.example.myapplication.data.enfermedadesPorEtapa
import com.example.myapplication.data.DiseaseData

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

                    // Filtrar enfermedades según etapa
                    val etapa = when (selectedCheckBox) {
                        binding.checkGestacion -> EtapaProductiva.GESTACION
                        binding.checkMaternidad -> EtapaProductiva.MATERNIDAD
                        binding.checkDestete -> EtapaProductiva.DESTETE
                        binding.checkEngorda -> EtapaProductiva.ENGORDA
                        binding.checkCuarentena -> EtapaProductiva.CUARENTENA
                        else -> null
                    }
                    if (etapa != null) {
                        val enfermedadesFiltradas = enfermedadesPorEtapa.filterValues { it.contains(etapa) }.keys.toList()
                        // Obtener síntomas únicos de las enfermedades asociadas a la etapa
                        val sintomasFiltrados = DiseaseData.diseases
                            .filter { enfermedadesFiltradas.contains(it.name) }
                            .flatMap { it.symptoms }
                            .distinct()
                        val intent = Intent(this, SymptomsActivity::class.java)
                        intent.putStringArrayListExtra("filtered_symptoms", ArrayList(sintomasFiltrados))
                        startActivity(intent)
                    } else if (selectedCheckBox == binding.checkTodas) {
                        startActivity(Intent(this, SymptomsActivity::class.java))
                    }
                }
            }
        }
    }
}
