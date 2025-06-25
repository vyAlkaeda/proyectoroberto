package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityEstreptococosisDetailBinding

class EstreptococosisDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEstreptococosisDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleText.text = "ESTREPTOCOCOSIS"
        binding.descriptionText.text = "Enfermedad infecciosa causada por Streptococcus suis, uno de los patógenos más importantes en porcinos. Se caracteriza por septicemia, meningitis, artritis y endocarditis, principalmente en lechones posdestete."
        binding.symptomsText.text = "SÍNTOMAS CLÍNICOS:\n\n" +
            "• FIEBRE (40°C-42°C)\n" +
            "• ANOREXIA\n" +
            "• DEPRESIÓN\n" +
            "• POSTRACIÓN\n" +
            "• ATAXIA\n" +
            "• COJERA\n" +
            "• ARTRITIS\n" +
            "• OPISTÓTONO\n" +
            "• CABEZA AGACHADA\n" +
            "• CONVULSIONES\n" +
            "• DISNEA\n" +
            "• CEGUERA\n" +
            "• SORDERA\n" +
            "• PEDALEO\n" +
            "• NISTAGMO\n" +
            "• SEPTICEMIA\n" +
            "• RIGIDEZ MUSCULAR\n" +
            "• MARCHA EN CÍRCULOS\n" +
            "• PELO HIRSUTO\n" +
            "• TEMBLORES\n" +
            "• DECÚBITO LATERAL\n\n" +
            "LESIONES PATOLÓGICAS:\n\n" +
            "• ARTRITIS PURULENTA\n" +
            "• BRONCONEUMONÍA PURULENTA\n" +
            "• MIOCARDITIS HEMORRÁGICA\n" +
            "• POLISEROSITIS\n" +
            "• ENDOCARDITIS VALVULAR\n" +
            "• PERICARDITIS\n" +
            "• MENINGITIS\n" +
            "• NEUMONÍA INTERSTICIAL"

        // Agregar listener para el botón de recomendaciones
        binding.recommendationsButton.setOnClickListener {
            val intent = Intent(this, RecomendacionesEstreptococosisActivity::class.java)
            startActivity(intent)
        }
    }
} 