package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityBioseguridadBinding

class BioseguridadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBioseguridadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBioseguridadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Bioseguridad"

        binding.cardMedidasBioseguridad.setOnClickListener {
            startActivity(Intent(this, MedidasBioseguridadActivity::class.java))
        }

        binding.cardTrasladoEnfermos.setOnClickListener {
            startActivity(Intent(this, TrasladoEnfermosActivity::class.java))
        }

        binding.cardEvitarEstres.setOnClickListener {
            startActivity(Intent(this, EvitarCausasEstresActivity::class.java))
        }

        /*

        binding.cardNoMovilizar.setOnClickListener {
            // TODO: Implementar vista de detalle para no movilizar cerdos
        }

        binding.cardNoComercializar.setOnClickListener {
            // TODO: Implementar vista de detalle para no comercializar cadáveres
        }
        */
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 