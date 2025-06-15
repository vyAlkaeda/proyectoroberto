package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityEvitarCausasEstresBinding

class EvitarCausasEstresActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEvitarCausasEstresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvitarCausasEstresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Evitar Causas de Estres"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}