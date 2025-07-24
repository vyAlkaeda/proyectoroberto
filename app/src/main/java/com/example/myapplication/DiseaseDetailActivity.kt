package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.DiseaseData
import com.example.myapplication.data.EnfermedadRepository
import com.example.myapplication.databinding.ActivityDiseaseDetailBinding
import com.example.myapplication.databinding.ItemSintomaAsociadoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiseaseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiseaseDetailBinding
    private lateinit var sintomasAdapter: SintomasAsociadosAdapter
    private var enfermedadNombre: String = ""
    private var enfermedadData: DiseaseData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el nombre de la enfermedad desde el intent
        enfermedadNombre = intent.getStringExtra("ENFERMEDAD_NOMBRE") ?: ""
        
        setupToolbar()
        setupRecyclerView()
        cargarDetalleEnfermedad()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = enfermedadNombre
    }

    private fun setupRecyclerView() {
        sintomasAdapter = SintomasAsociadosAdapter()
        binding.recyclerViewSintomas.apply {
            layoutManager = LinearLayoutManager(this@DiseaseDetailActivity)
            adapter = sintomasAdapter
        }
    }

    private fun cargarDetalleEnfermedad() {
        if (enfermedadNombre.isEmpty()) {
            mostrarError("No se especificó la enfermedad")
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.contentLayout.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = EnfermedadRepository(this@DiseaseDetailActivity)
                val enfermedad = repository.obtenerEnfermedadPorNombre(enfermedadNombre)
                
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    if (enfermedad != null) {
                        enfermedadData = enfermedad
                        mostrarDetalleEnfermedad(enfermedad)
                    } else {
                        mostrarError("No se encontró información para: $enfermedadNombre")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    mostrarError("Error al cargar la información: ${e.message}")
                }
            }
        }
    }

    private fun mostrarDetalleEnfermedad(enfermedad: DiseaseData) {
        binding.contentLayout.visibility = View.VISIBLE
        
        // Información básica
        binding.tvEnfermedadNombre.text = enfermedad.nombre
        binding.tvEnfermedadDescripcion.text = enfermedad.descripcion ?: "Sin descripción disponible"
        
        // Síntomas asociados
        if (enfermedad.sintomas.isNotEmpty()) {
            binding.tvSintomasTitulo.visibility = View.VISIBLE
            binding.recyclerViewSintomas.visibility = View.VISIBLE
            sintomasAdapter.submitList(enfermedad.sintomas)
        } else {
            binding.tvSintomasTitulo.visibility = View.GONE
            binding.recyclerViewSintomas.visibility = View.GONE
        }

        // Configurar botón de remedio
        binding.btnRemedio.setOnClickListener {
            mostrarDialogoRemedio()
        }
    }

    private fun mostrarDialogoRemedio() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Remedio - $enfermedadNombre")
            .setMessage("Esta funcionalidad estará disponible próximamente. Aquí se mostrarán las opciones de tratamiento y remedios para la enfermedad seleccionada.")
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(R.drawable.ic_help)
            .create()
        
        dialog.show()
    }

    private fun mostrarError(mensaje: String) {
        binding.errorLayout.visibility = View.VISIBLE
        binding.tvError.text = mensaje
        binding.btnRetry.setOnClickListener {
            binding.errorLayout.visibility = View.GONE
            cargarDetalleEnfermedad()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

// Adapter para mostrar síntomas asociados
class SintomasAsociadosAdapter : ListAdapter<String, SintomasAsociadosAdapter.ViewHolder>(SintomasDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSintomaAsociadoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemSintomaAsociadoBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(sintoma: String) {
            binding.tvSintoma.text = sintoma
        }
    }
}

class SintomasDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
} 