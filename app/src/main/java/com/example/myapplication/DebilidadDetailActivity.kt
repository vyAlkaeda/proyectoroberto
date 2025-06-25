package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySymptomDetailBaseBinding

class DebilidadDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySymptomDetailBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "DIAGNÓSTICO DIFERENCIAL"

        // Mostrar solo la enfermedad principal con estilo médico
        binding.sectionTitle1.text = "DIAGNÓSTICO DIFERENCIAL"
        binding.sectionContent1.text = "ESTREPTOCOCOSIS"

        // Redirigir al detalle al hacer click
        binding.sectionContent1.setOnClickListener {
            val intent = Intent(this, EstreptococosisDetailActivity::class.java)
            startActivity(intent)
        }
    }
}