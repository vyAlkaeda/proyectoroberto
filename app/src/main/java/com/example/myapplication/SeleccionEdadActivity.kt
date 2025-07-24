package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.EdadClasificacion
import com.example.myapplication.data.EdadFiltradoLogic
import com.example.myapplication.databinding.ActivitySeleccionEdadBinding
import com.example.myapplication.databinding.ItemEdadClasificacionBinding
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat

class SeleccionEdadActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeleccionEdadBinding
    private lateinit var adapter: EdadClasificacionAdapter
    private var clasificacionesEdad: List<EdadClasificacion> = emptyList()
    private var tipoDiagnostico: String = "SINTOMAS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionEdadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tipoDiagnostico = intent.getStringExtra("TIPO_DIAGNOSTICO") ?: "SINTOMAS"

        // Configuración de colores según el tipo de diagnóstico
        // (Implementación futura para personalización de temas)

        aplicarEstiloPorTipoDiagnostico()
        setupToolbar()
        setupRecyclerView()
        configurarContenido()
        cargarClasificacionesEdad()
    }

    private fun aplicarEstiloPorTipoDiagnostico() {
        if (tipoDiagnostico == "NECROPSIA") {
            binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.necro_primary))
            // Configurar color del texto del toolbar como blanco
            binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
            binding.tvTitulo.setTextColor(ContextCompat.getColor(this, R.color.necro_primary_dark))
            binding.tvSubtitulo.setTextColor(ContextCompat.getColor(this, R.color.necro_primary_dark))
            binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.necro_primary_light))
        } else {
            binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            // Configurar color del texto del toolbar como blanco
            binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
            binding.tvTitulo.setTextColor(ContextCompat.getColor(this, R.color.primary_dark))
            binding.tvSubtitulo.setTextColor(ContextCompat.getColor(this, R.color.primary_dark))
            binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        val titulo = when (tipoDiagnostico) {
            "NECROPSIA" -> "Necropsia por Edad"
            else -> "Diagnóstico por Síntomas"
        }
        supportActionBar?.title = titulo
        
        // Configurar color del icono de navegación como blanco
        binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))
    }

    private fun setupRecyclerView() {
        adapter = EdadClasificacionAdapter(tipoDiagnostico) { edadSeleccionada ->
            // Navegar a la pantalla de sistemas y síntomas
            val intent = Intent(this, SistemasYSintomasPorEdadActivity::class.java)
            intent.putExtra("EDAD_SELECCIONADA", edadSeleccionada.etapa)
            intent.putExtra("TIPO_DIAGNOSTICO", tipoDiagnostico)
            startActivity(intent)
        }

        binding.recyclerViewEdades.apply {
            layoutManager = LinearLayoutManager(this@SeleccionEdadActivity)
            adapter = this@SeleccionEdadActivity.adapter
        }
    }

    private fun configurarContenido() {
        when (tipoDiagnostico) {
            "NECROPSIA" -> {
                binding.tvTitulo.text = "Necropsia por Edad"
                binding.tvSubtitulo.text = "Elige la etapa del animal para ver lesiones específicas o 'Todas' para ver todas las lesiones disponibles"
            }
            else -> {
                binding.tvTitulo.text = "Diagnóstico por Síntomas"
                binding.tvSubtitulo.text = "Elige la etapa del animal para ver síntomas específicos o 'Todas' para ver todos los síntomas disponibles"
            }
        }
    }

    private fun cargarClasificacionesEdad() {
        clasificacionesEdad = EdadFiltradoLogic.cargarClasificacionEdades(this)
        adapter.submitList(clasificacionesEdad)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

// Adapter para mostrar las clasificaciones de edad
class EdadClasificacionAdapter(
    private val tipoDiagnostico: String,
    private val onEdadClick: (EdadClasificacion) -> Unit
) : RecyclerView.Adapter<EdadClasificacionAdapter.EdadViewHolder>() {

    private var clasificaciones: List<EdadClasificacion> = emptyList()

    fun submitList(newList: List<EdadClasificacion>) {
        clasificaciones = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EdadViewHolder {
        val binding = ItemEdadClasificacionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EdadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EdadViewHolder, position: Int) {
        holder.bind(clasificaciones[position])
    }

    override fun getItemCount(): Int = clasificaciones.size

    inner class EdadViewHolder(
        private val binding: ItemEdadClasificacionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clasificacion: EdadClasificacion) {
            binding.tvEtapa.text = clasificacion.etapa
            binding.tvDescripcion.text = clasificacion.descripcion
            
            // Asignar icono según la etapa
            val iconRes = when (clasificacion.etapa) {
                "Todas" -> R.drawable.ic_all_ages
                "Lechones lactantes" -> R.drawable.ic_baby
                "Lechones destetados" -> R.drawable.ic_young
                "Crecimiento/Engorda" -> R.drawable.ic_growth
                "Adultos/Reproductores" -> R.drawable.ic_adult
                else -> R.drawable.ic_necropsy
            }
            binding.ivEtapaIcon.setImageResource(iconRes)
            
            // Cambiar el color del borde del círculo según el tipo de diagnóstico
            val color = if (tipoDiagnostico == "NECROPSIA") ContextCompat.getColor(binding.root.context, R.color.necro_primary) else ContextCompat.getColor(binding.root.context, R.color.primary)
            val background = binding.ivEtapaIcon.parent as? ViewGroup
            background?.background?.let {
                if (it is GradientDrawable) {
                    it.setStroke(2, color)
                }
            }
            binding.root.setOnClickListener {
                onEdadClick(clasificacion)
            }
        }
    }
} 