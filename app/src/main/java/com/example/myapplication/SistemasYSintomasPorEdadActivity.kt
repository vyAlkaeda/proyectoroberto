package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.EdadFiltradoLogic
import com.example.myapplication.data.SintomaPorEdad
import com.example.myapplication.databinding.ActivitySistemasYSintomasPorEdadBinding
import com.example.myapplication.databinding.ItemSistemaBinding
import com.example.myapplication.databinding.ItemSintomaPorSistemaBinding

// Clase para representar separadores de sistemas
data class SistemaSeparador(val nombreSistema: String)

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
            // Configurar color del texto del toolbar como blanco
            binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
            // Cambiar color del botón consultar
            binding.btnConsultar.setBackgroundResource(R.drawable.button_consult)
            binding.btnConsultar.isSelected = true
            binding.btnConsultar.setTextColor(resources.getColor(R.color.white))
            // Cambiar color de títulos
            binding.tvSistemasTitulo.setTextColor(resources.getColor(R.color.necro_primary_dark))
            binding.tvSintomasTitulo.setTextColor(resources.getColor(R.color.necro_primary_dark))
        } else {
            binding.toolbar.setBackgroundColor(resources.getColor(R.color.primary))
            // Configurar color del texto del toolbar como blanco
            binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
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
        
        // Configurar color del icono de navegación como blanco
        binding.toolbar.navigationIcon?.setTint(resources.getColor(R.color.white))
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
        
        // Determinar tipo de filtro (SINTOMA/SIGNOS o LESION)
        val tipoFiltro = when (tipoDiagnostico) {
            "NECROPSIA" -> "LESION"
            else -> "SINTOMA" // Para síntomas incluir también SIGNOS NEUROLOGICOS y SIGNOS CLINICOS
        }
        
        // Log para depuración
        android.util.Log.d("SistemasYSintomas", "Edad seleccionada: $edadSeleccionada")
        android.util.Log.d("SistemasYSintomas", "Tipo filtro: $tipoFiltro")
        
        if (edadSeleccionada == "Todas") {
            // Para "Todas": solo filtrar por tipo, no por edad
            sintomasFiltrados = EdadFiltradoLogic.filtrarSintomasPorTipo(todosLosSintomas, tipoFiltro)
            android.util.Log.d("SistemasYSintomas", "Mostrando TODOS los síntomas (sin filtro de edad)")
        } else {
            // Para edad específica: filtrar por edad y tipo
            sintomasFiltrados = EdadFiltradoLogic.filtrarSintomasPorEdadYTipo(todosLosSintomas, edadSeleccionada, tipoFiltro)
            android.util.Log.d("SistemasYSintomas", "Filtrando por edad específica: $edadSeleccionada")
        }
        
        android.util.Log.d("SistemasYSintomas", "Total síntomas filtrados: ${sintomasFiltrados.size}")
        
        // Agrupar por sistema
        sintomasAgrupados = EdadFiltradoLogic.agruparSintomasPorSistema(sintomasFiltrados)
        
        // Obtener sistemas disponibles según la edad seleccionada
        val sistemasDisponiblesParaEdad = if (edadSeleccionada == "Todas") {
            // Para "Todas": mostrar todos los sistemas
            EdadFiltradoLogic.obtenerTodosLosSistemas(this)
        } else {
            // Para edad específica: mostrar solo sistemas que tengan síntomas para esa edad
            EdadFiltradoLogic.obtenerSistemasDisponibles(sintomasFiltrados)
        }
        
        sistemasDisponibles = listOf("Todas") + sistemasDisponiblesParaEdad
        
        // Log para depuración
        android.util.Log.d("SistemasYSintomas", "Sistemas disponibles: $sistemasDisponibles")
        
        // Actualizar adapters - filtrar "Todas" de la lista horizontal de sistemas
        val sistemasSinTodas = sistemasDisponibles.filter { it != "Todas" }
        sistemasAdapter.submitList(sistemasSinTodas)
        
        // Mostrar "Todas" por defecto
        sistemaSeleccionado = "Todas"
        mostrarSintomasDelSistema(sistemaSeleccionado)
    }

    private fun mostrarSintomasDelSistema(sistema: String) {
        if (sistema == "Todas") {
            // Mostrar todos los sistemas con sus síntomas organizados
            mostrarTodosLosSistemas()
        } else {
            // Mostrar síntomas de un sistema específico
            val sintomasDelSistema = sintomasAgrupados[sistema] ?: emptyList()
            
            // Log para depuración
            android.util.Log.d("SistemasYSintomas", "Sistema seleccionado: $sistema")
            android.util.Log.d("SistemasYSintomas", "Síntomas a mostrar: ${sintomasDelSistema.size}")
            
            sintomasAdapter.submitList(sintomasDelSistema)
            
            // Actualizar título del sistema seleccionado
            val tituloSistema = if (edadSeleccionada == "Todas") {
                "Sistema: $sistema (todas las edades)"
            } else {
                "Sistema: $sistema"
            }
            binding.tvSistemaSeleccionado.text = tituloSistema
        }
    }

    private fun mostrarTodosLosSistemas() {
        // Crear una lista que incluya separadores de sistemas y sus síntomas
        val todosLosSistemasConSintomas = mutableListOf<Any>()
        
        // Agregar un separador para cada sistema que tenga síntomas
        sistemasDisponibles.filter { it != "Todas" }.forEach { sistema ->
            val sintomasDelSistema = sintomasAgrupados[sistema] ?: emptyList()
            if (sintomasDelSistema.isNotEmpty()) {
                // Agregar separador del sistema
                todosLosSistemasConSintomas.add(SistemaSeparador(sistema))
                // Agregar todos los síntomas del sistema
                todosLosSistemasConSintomas.addAll(sintomasDelSistema)
            }
        }
        
        // Log para depuración
        android.util.Log.d("SistemasYSintomas", "Mostrando sistemas con síntomas disponibles")
        android.util.Log.d("SistemasYSintomas", "Total elementos (separadores + síntomas): ${todosLosSistemasConSintomas.size}")
        
        sintomasAdapter.submitList(todosLosSistemasConSintomas)
        
        // Actualizar título
        val tituloSistema = if (edadSeleccionada == "Todas") {
            "Todos los síntomas (todas las edades)"
        } else {
            "Síntomas disponibles - $edadSeleccionada"
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
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var sintomas: List<Any> = emptyList() // Changed to Any to accommodate separators

    fun submitList(newList: List<Any>) {
        sintomas = newList
        android.util.Log.d("SintomasAdapter", "Actualizando lista de síntomas: ${sintomas.size} síntomas")
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (sintomas[position] is SistemaSeparador) {
            VIEW_TYPE_SEPARADOR
        } else {
            VIEW_TYPE_SINTOMA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SEPARADOR -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_sistema_separador, parent, false
                )
                SeparadorViewHolder(view)
            }
            else -> { // VIEW_TYPE_SINTOMA
                val binding = ItemSintomaPorSistemaBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SintomaViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SeparadorViewHolder -> {
                holder.bind(sintomas[position] as SistemaSeparador)
            }
            is SintomaViewHolder -> {
                holder.bind(sintomas[position] as SintomaPorEdad)
            }
        }
    }

    override fun getItemCount(): Int = sintomas.size

    inner class SeparadorViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(sistema: SistemaSeparador) {
            val tvSistemaSeparador = itemView.findViewById<TextView>(R.id.tvSistemaSeparador)
            tvSistemaSeparador.text = sistema.nombreSistema
        }
    }

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

    companion object {
        private const val VIEW_TYPE_SEPARADOR = 0
        private const val VIEW_TYPE_SINTOMA = 1
    }
} 