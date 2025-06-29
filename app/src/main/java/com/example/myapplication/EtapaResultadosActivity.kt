package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.DiseaseData
import com.example.myapplication.models.Disease
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myapplication.databinding.ItemDiseaseBinding

class EtapaResultadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etapa_resultados)

        val enfermedadesFiltradas = intent.getStringArrayListExtra("enfermedades_filtradas") ?: arrayListOf()
        val recyclerView = findViewById<RecyclerView>(R.id.etapaResultadosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = EtapaResultadosAdapter(enfermedadesFiltradas) { enfermedad ->
            mostrarSintomasDialogo(enfermedad)
        }
        recyclerView.adapter = adapter

        // Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Resultados por Etapa"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun mostrarSintomasDialogo(nombreEnfermedad: String) {
        val disease = DiseaseData.diseases.find { it.name.equals(nombreEnfermedad, ignoreCase = true) }
        if (disease != null) {
            val sintomas = disease.symptoms.joinToString("\n")
            AlertDialog.Builder(this)
                .setTitle("Síntomas de $nombreEnfermedad")
                .setMessage(sintomas)
                .setPositiveButton("Cerrar", null)
                .show()
        } else {
            Toast.makeText(this, "No se encontraron síntomas para esta enfermedad", Toast.LENGTH_SHORT).show()
        }
    }
}

class EtapaResultadosAdapter(
    private var enfermedades: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<EtapaResultadosAdapter.EtapaResultadosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtapaResultadosViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EtapaResultadosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EtapaResultadosViewHolder, position: Int) {
        holder.bind(enfermedades[position], onItemClick)
    }

    override fun getItemCount() = enfermedades.size

    class EtapaResultadosViewHolder(private val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nombreEnfermedad: String, onItemClick: (String) -> Unit) {
            binding.diseaseNameTextView.text = nombreEnfermedad
            binding.diseaseShortDescTextView.text = "Toca para ver síntomas"
            binding.root.setOnClickListener { onItemClick(nombreEnfermedad) }
        }
    }
} 