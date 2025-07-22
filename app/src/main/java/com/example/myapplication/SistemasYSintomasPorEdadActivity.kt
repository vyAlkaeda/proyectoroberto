package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.EdadFiltradoLogic
import com.example.myapplication.data.SintomaPorEdad
import com.example.myapplication.data.SistemaManager
import com.example.myapplication.databinding.ActivitySistemasYSintomasPorEdadBinding
import com.example.myapplication.databinding.ItemSistemaBinding
import com.example.myapplication.databinding.ItemSintomaPorSistemaBinding

class SistemasYSintomasPorEdadActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySistemasYSintomasPorEdadBinding
    private lateinit var sistemasAdapter: SistemasAdapter
    private lateinit var sintomasAdapter: SintomasPorSistemaAdapter
    private var edadSeleccionada: String = ""
    private var tipoDiagnostico: String = "SINTOMAS"
    private var sintomasFiltrados: List<SintomaPorEdad> = emptyList()
    private var sintomasAgrupados: Map<String, List<SintomaPorEdad>> = emptyMap()
    private var sistemasDisponibles: List<String> = emptyList()
    private var sistemaSeleccionado: String = ""
    private var sintomasSeleccionadosGlobales = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySistemasYSintomasPorEdadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edadSeleccionada = intent.getStringExtra("EDAD_SELECCIONADA") ?: ""
        tipoDiagnostico = intent.getStringExtra("TIPO_DIAGNOSTICO") ?: "SINTOMAS"

        aplicarEstiloPorTipoDiagnostico()
        setupToolbar()
        setupRecyclerViews()
        configurarTitulos()
        configurarBotonConsultar()
        cargarDatos()
    }

    private fun aplicarEstiloPorTipoDiagnostico() {
        if (tipoDiagnostico == "NECROPSIA") {
            // Cambiar color de la toolbar
            binding.toolbar.setBackgroundColor(resources.getColor(R.color.necro_primary))
            // Cambiar color del botón consultar
            binding.btnConsultar.setBackgroundResource(R.drawable.button_consult)
            binding.btnConsultar.isSelected = true
            binding.btnConsultar.setTextColor(resources.getColor(R.color.white))
            // Cambiar color de títulos
            binding.tvSistemasTitulo.setTextColor(resources.getColor(R.color.necro_primary_dark))
            binding.tvSintomasTitulo.setTextColor(resources.getColor(R.color.necro_primary_dark))
        } else {
            binding.toolbar.setBackgroundColor(resources.getColor(R.color.primary))
            binding.btnConsultar.setBackgroundResource(R.drawable.button_consult)
            binding.btnConsultar.isSelected = false
            binding.btnConsultar.setTextColor(resources.getColor(R.color.white))
            binding.tvSistemasTitulo.setTextColor(resources.getColor(R.color.primary_dark))
            binding.tvSintomasTitulo.setTextColor(resources.getColor(R.color.primary_dark))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        val titulo = when (tipoDiagnostico) {
            "NECROPSIA" -> if (edadSeleccionada == "Todas") "Necropsia - Todas las Edades" else "Necropsia - $edadSeleccionada"
            else -> if (edadSeleccionada == "Todas") "Síntomas - Todas las Edades" else "Síntomas - $edadSeleccionada"
        }
        supportActionBar?.title = titulo
    }

    private fun setupRecyclerViews() {
        // Adapter para sistemas
        sistemasAdapter = SistemasAdapter(tipoDiagnostico) { sistema ->
            sistemaSeleccionado = sistema
            mostrarSintomasDelSistema(sistema)
        }
        binding.recyclerViewSistemas.apply {
            layoutManager = LinearLayoutManager(this@SistemasYSintomasPorEdadActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = sistemasAdapter
        }

        // Adapter para síntomas
        sintomasAdapter = SintomasPorSistemaAdapter(tipoDiagnostico, sintomasSeleccionadosGlobales) { sintomasSeleccionados ->
            // Actualizar la lista global directamente
            sintomasSeleccionadosGlobales.clear()
            sintomasSeleccionadosGlobales.addAll(sintomasSeleccionados)
            actualizarVisibilidadBotonConsultar()
        }
        binding.recyclerViewSintomas.apply {
            layoutManager = LinearLayoutManager(this@SistemasYSintomasPorEdadActivity)
            adapter = sintomasAdapter
        }
    }

    private fun configurarTitulos() {
        when (tipoDiagnostico) {
            "NECROPSIA" -> {
                if (edadSeleccionada == "Todas") {
                    binding.tvSistemasTitulo.text = "Sistemas con lesiones disponibles (todas las edades):"
                    binding.tvSintomasTitulo.text = "Lesiones del sistema seleccionado (todas las edades):"
                } else {
                    binding.tvSistemasTitulo.text = "Sistemas con lesiones disponibles:"
                    binding.tvSintomasTitulo.text = "Lesiones del sistema seleccionado:"
                }
            }
            else -> {
                if (edadSeleccionada == "Todas") {
                    binding.tvSistemasTitulo.text = "Sistemas con síntomas disponibles (todas las edades):"
                    binding.tvSintomasTitulo.text = "Síntomas del sistema seleccionado (todas las edades):"
                } else {
                    binding.tvSistemasTitulo.text = "Sistemas con síntomas disponibles:"
                    binding.tvSintomasTitulo.text = "Síntomas del sistema seleccionado:"
                }
            }
        }
    }

    private fun cargarDatos() {
        // Cargar todos los síntomas
        val todosLosSintomas = EdadFiltradoLogic.cargarSintomasCompletos(this)
        
        // Filtrar por edad seleccionada y tipo (Síntoma o Lesión)
        val tipoFiltro = when (tipoDiagnostico) {
            "NECROPSIA" -> "Lesión"
            else -> "Síntoma"
        }
        
        sintomasFiltrados = EdadFiltradoLogic.filtrarSintomasPorEdadYTipo(todosLosSintomas, edadSeleccionada, tipoFiltro)
        
        // Reclasificar síntomas usando el nuevo sistema mejorado
        val sintomasReclasificados = sintomasFiltrados.map { sintoma ->
            sintoma.copy(sistema = SistemaManager.determinarSistemaAvanzado(sintoma.sintoma))
        }
        
        // Agrupar por sistema usando la nueva clasificación
        sintomasAgrupados = sintomasReclasificados.groupBy { it.sistema }
        
        // Obtener sistemas disponibles en orden recomendado
        val sistemasConSintomas = sintomasAgrupados.keys.toSet()
        sistemasDisponibles = SistemaManager.obtenerOrdenRecomendado().filter { 
            sistemasConSintomas.contains(it) 
        }
        
        // Actualizar adapters
        sistemasAdapter.submitList(sistemasDisponibles)
        
        // Mostrar el primer sistema por defecto
        if (sistemasDisponibles.isNotEmpty()) {
            sistemaSeleccionado = sistemasDisponibles[0]
            mostrarSintomasDelSistema(sistemaSeleccionado)
        }
    }

    private fun mostrarSintomasDelSistema(sistema: String) {
        val sintomasDelSistema = sintomasAgrupados[sistema] ?: emptyList()
        sintomasAdapter.submitList(sintomasDelSistema)
        
        // Actualizar título del sistema seleccionado
        val tituloSistema = if (edadSeleccionada == "Todas") {
            "Sistema: $sistema (todas las edades)"
        } else {
            "Sistema: $sistema"
        }
        binding.tvSistemaSeleccionado.text = tituloSistema
    }

    private fun configurarBotonConsultar() {
        binding.btnConsultar.setOnClickListener {
            if (sintomasSeleccionadosGlobales.isNotEmpty()) {
                consultarEnfermedades()
            }
        }
    }
    
    private fun actualizarVisibilidadBotonConsultar() {
        binding.btnConsultar.visibility = if (sintomasSeleccionadosGlobales.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    
    private fun consultarEnfermedades() {
        // Crear un mapa de enfermedades con su frecuencia
        val enfermedadFrecuencia = mutableMapOf<String, Int>()
        sintomasFiltrados.forEach { sintoma ->
            if (sintomasSeleccionadosGlobales.contains(sintoma.sintoma)) {
                sintoma.enfermedades.forEach { enfermedad ->
                    enfermedadFrecuencia[enfermedad] = enfermedadFrecuencia.getOrDefault(enfermedad, 0) + 1
                }
            }
        }

        // Filtrar enfermedades con 1 o más coincidencias
        val enfermedadesFiltradas = enfermedadFrecuencia.filter { it.value >= 1 }

        // Ordenar enfermedades por frecuencia (más frecuentes primero)
        val enfermedadesOrdenadas = enfermedadesFiltradas.entries
            .sortedByDescending { it.value }
            .map { it.key }

        // Mostrar resultados en un diálogo o navegar a una nueva actividad
        mostrarResultadosDiagnostico(
            enfermedadesOrdenadas,
            sintomasSeleccionadosGlobales.toList(),
            HashMap(enfermedadesFiltradas)
        )
    }

    private fun mostrarResultadosDiagnostico(
        enfermedades: List<String>,
        sintomasSeleccionados: List<String>,
        enfermedadesConContador: HashMap<String, Int>? = null
    ) {
        val intent = Intent(this, FoundDiseasesActivity::class.java)
        intent.putExtra("ENFERMEDADES", ArrayList(enfermedades))
        intent.putExtra("SINTOMAS_SELECCIONADOS", ArrayList(sintomasSeleccionados))
        intent.putExtra("TIPO_DIAGNOSTICO", tipoDiagnostico)
        if (enfermedadesConContador != null) {
            intent.putExtra("ENFERMEDADES_CON_CONTADOR", enfermedadesConContador)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

// Adapter para sistemas
class SistemasAdapter(
    private val tipoDiagnostico: String,
    private val onSistemaClick: (String) -> Unit
) : RecyclerView.Adapter<SistemasAdapter.SistemaViewHolder>() {

    private var sistemas: List<String> = emptyList()

    fun submitList(newList: List<String>) {
        sistemas = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SistemaViewHolder {
        val binding = ItemSistemaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SistemaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SistemaViewHolder, position: Int) {
        holder.bind(sistemas[position])
    }

    override fun getItemCount(): Int = sistemas.size

    inner class SistemaViewHolder(
        private val binding: ItemSistemaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sistema: String) {
            binding.tvSistema.text = sistema
            // Cambiar color de fondo según tipo de diagnóstico
            if (tipoDiagnostico == "NECROPSIA") {
                binding.root.setBackgroundResource(R.drawable.chip_necro)
                binding.tvSistema.setTextColor(binding.root.context.getColor(R.color.necro_primary_dark))
            } else {
                binding.root.setBackgroundResource(R.drawable.chip_sintoma)
                binding.tvSistema.setTextColor(binding.root.context.getColor(R.color.primary_dark))
            }
            binding.root.setOnClickListener {
                onSistemaClick(sistema)
            }
        }
    }
}

// Adapter para síntomas por sistema
class SintomasPorSistemaAdapter(
    private val tipoDiagnostico: String,
    private val sintomasSeleccionadosGlobales: MutableSet<String>,
    private val onSintomasSeleccionados: (List<String>) -> Unit
) : RecyclerView.Adapter<SintomasPorSistemaAdapter.SintomaViewHolder>() {

    private var sintomas: List<SintomaPorEdad> = emptyList()

    fun submitList(newList: List<SintomaPorEdad>) {
        sintomas = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SintomaViewHolder {
        val binding = ItemSintomaPorSistemaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SintomaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SintomaViewHolder, position: Int) {
        holder.bind(sintomas[position])
    }

    override fun getItemCount(): Int = sintomas.size

    inner class SintomaViewHolder(
        private val binding: ItemSintomaPorSistemaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sintoma: SintomaPorEdad) {
            binding.tvSintoma.text = sintoma.sintoma
            binding.tvDescripcion.text = sintoma.descripcion // Esto ya lo muestra, asegúrate que el TextView esté visible y bien estilizado
            binding.tvTipo.text = sintoma.tipo

            binding.checkBoxSintoma.isChecked = sintomasSeleccionadosGlobales.contains(sintoma.sintoma)

            // Cambiar color del checkbox según tipo de diagnóstico
            if (tipoDiagnostico == "NECROPSIA") {
                binding.checkBoxSintoma.buttonTintList = binding.root.context.getColorStateList(R.color.necro_primary)
            } else {
                binding.checkBoxSintoma.buttonTintList = binding.root.context.getColorStateList(R.color.primary)
            }

            binding.checkBoxSintoma.setOnClickListener {
                if (binding.checkBoxSintoma.isChecked) {
                    sintomasSeleccionadosGlobales.add(sintoma.sintoma)
                } else {
                    sintomasSeleccionadosGlobales.remove(sintoma.sintoma)
                }
                onSintomasSeleccionados(sintomasSeleccionadosGlobales.toList())
            }

            binding.root.setOnClickListener {
                val nuevoEstado = !binding.checkBoxSintoma.isChecked
                binding.checkBoxSintoma.isChecked = nuevoEstado
                if (nuevoEstado) {
                    sintomasSeleccionadosGlobales.add(sintoma.sintoma)
                } else {
                    sintomasSeleccionadosGlobales.remove(sintoma.sintoma)
                }
                onSintomasSeleccionados(sintomasSeleccionadosGlobales.toList())
            }
        }
    }
} 