package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.*
import com.example.myapplication.databinding.ActivityForoEspecializadoBinding
import com.example.myapplication.databinding.ItemConsultaEspecializadaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.text.format.DateUtils
import android.widget.Toast
import androidx.core.content.ContextCompat

class ForoEspecializadoActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityForoEspecializadoBinding
    private lateinit var consultasAdapter: ConsultasEspecializadasAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var userData: UserData? = null
    private var consultasActuales: List<ConsultaEspecializada> = emptyList()
    private var filtroCategoria: String = "Todas"
    private var filtroEstado: EstadoConsulta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForoEspecializadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setupToolbar()
        setupRecyclerView()
        setupFAB()
        cargarDatosUsuario()
        cargarConsultas()
        setupFiltros()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Consulta Especializada"
            subtitle = "Atención veterinaria profesional"
            setDisplayHomeAsUpEnabled(true)
        }
        
        // Configurar colores del toolbar
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))
    }

    private fun setupRecyclerView() {
        consultasAdapter = ConsultasEspecializadasAdapter(
            onConsultaClick = { consulta ->
                abrirDetalleConsulta(consulta)
            },
            onEspecialistaClick = { especialista ->
                verPerfilEspecialista(especialista)
            }
        )
        
        binding.recyclerViewConsultas.apply {
            layoutManager = LinearLayoutManager(this@ForoEspecializadoActivity)
            adapter = consultasAdapter
        }
    }

    private fun setupFAB() {
        binding.fabNuevaConsulta.setOnClickListener {
            crearNuevaConsulta()
        }
    }

    private fun setupFiltros() {
        // Configurar spinner de categorías
        val categorias = listOf("Todas") + SistemaManager.obtenerSistemasOrganizados().map { it.nombre }
        // Aquí configurarías el spinner con las categorías
        
        // Configurar chips de estado
        binding.chipPendientes.setOnClickListener {
            filtroEstado = EstadoConsulta.PENDIENTE
            aplicarFiltros()
        }
        
        binding.chipRespondidas.setOnClickListener {
            filtroEstado = EstadoConsulta.RESPONDIDA
            aplicarFiltros()
        }
        
        binding.chipTodas.setOnClickListener {
            filtroEstado = null
            aplicarFiltros()
        }
    }

    private fun cargarDatosUsuario() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userData = document.toObject(UserData::class.java)
                        actualizarUI()
                    }
                }
        }
    }

    private fun cargarConsultas() {
        val currentUser = auth.currentUser ?: return
        
        // Cargar consultas del usuario actual y consultas públicas
        db.collection("consultasEspecializadas")
            .where(
                com.google.firebase.firestore.Filter.or(
                    com.google.firebase.firestore.Filter.equalTo("usuarioId", currentUser.uid),
                    com.google.firebase.firestore.Filter.equalTo("esPublica", true)
                )
            )
            .orderBy("fechaCreacion", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error al cargar consultas: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    consultasActuales = snapshots.toObjects(ConsultaEspecializada::class.java)
                    aplicarFiltros()
                    actualizarEstadisticas()
                }
            }
    }

    private fun aplicarFiltros() {
        var consultasFiltradas = consultasActuales
        
        // Filtrar por categoría
        if (filtroCategoria != "Todas") {
            consultasFiltradas = consultasFiltradas.filter { it.categoria == filtroCategoria }
        }
        
        // Filtrar por estado
        filtroEstado?.let { estado ->
            consultasFiltradas = consultasFiltradas.filter { it.estado == estado }
        }
        
        consultasAdapter.submitList(consultasFiltradas)
        
        // Mostrar mensaje si no hay consultas
        if (consultasFiltradas.isEmpty()) {
            binding.layoutVacio.visibility = View.VISIBLE
            binding.recyclerViewConsultas.visibility = View.GONE
        } else {
            binding.layoutVacio.visibility = View.GONE
            binding.recyclerViewConsultas.visibility = View.VISIBLE
        }
    }

    private fun actualizarEstadisticas() {
        val pendientes = consultasActuales.count { it.estado == EstadoConsulta.PENDIENTE }
        val respondidas = consultasActuales.count { it.estado == EstadoConsulta.RESPONDIDA }
        
        binding.tvEstadisticas.text = "Consultas: ${consultasActuales.size} | Pendientes: $pendientes | Respondidas: $respondidas"
    }

    private fun crearNuevaConsulta() {
        val intent = Intent(this, NuevaConsultaEspecializadaActivity::class.java)
        startActivity(intent)
    }

    private fun abrirDetalleConsulta(consulta: ConsultaEspecializada) {
        val intent = Intent(this, DetalleConsultaEspecializadaActivity::class.java)
        intent.putExtra("CONSULTA_DATA", consulta)
        startActivity(intent)
    }

    private fun verPerfilEspecialista(especialista: String) {
        // Navegar al perfil del especialista
        val intent = Intent(this, PerfilEspecialistaActivity::class.java)
        intent.putExtra("ESPECIALISTA_ID", especialista)
        startActivity(intent)
    }

    private fun actualizarUI() {
        userData?.let { user ->
            binding.tvBienvenida.text = "¡Hola ${user.fullName}!"
            binding.tvDescripcion.text = "Consulta con veterinarios especializados en porcicultura"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_foro_especializado, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_mis_consultas -> {
                mostrarSoloMisConsultas()
                true
            }
            R.id.action_especialistas -> {
                verListaEspecialistas()
                true
            }
            R.id.action_notificaciones -> {
                verNotificaciones()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarSoloMisConsultas() {
        val currentUser = auth.currentUser ?: return
        val misConsultas = consultasActuales.filter { it.usuarioId == currentUser.uid }
        consultasAdapter.submitList(misConsultas)
    }

    private fun verListaEspecialistas() {
        val intent = Intent(this, ListaEspecialistasActivity::class.java)
        startActivity(intent)
    }

    private fun verNotificaciones() {
        val intent = Intent(this, NotificacionesForoActivity::class.java)
        startActivity(intent)
    }
}

// Adapter para las consultas especializadas
class ConsultasEspecializadasAdapter(
    private val onConsultaClick: (ConsultaEspecializada) -> Unit,
    private val onEspecialistaClick: (String) -> Unit
) : RecyclerView.Adapter<ConsultasEspecializadasAdapter.ConsultaViewHolder>() {

    private var consultas: List<ConsultaEspecializada> = emptyList()

    fun submitList(newList: List<ConsultaEspecializada>) {
        consultas = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultaViewHolder {
        val binding = ItemConsultaEspecializadaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ConsultaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultaViewHolder, position: Int) {
        holder.bind(consultas[position])
    }

    override fun getItemCount(): Int = consultas.size

    inner class ConsultaViewHolder(
        private val binding: ItemConsultaEspecializadaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(consulta: ConsultaEspecializada) {
            binding.apply {
                tvTitulo.text = consulta.titulo
                tvDescripcion.text = consulta.descripcionProblema
                tvCategoria.text = consulta.categoria
                tvUsuario.text = consulta.usuarioNombre
                tvFecha.text = DateUtils.getRelativeTimeSpanString(
                    consulta.fechaCreacion.toDate().time,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
                )
                tvRespuestas.text = "${consulta.numeroRespuestas} respuestas"

                // Configurar estado visual
                when (consulta.estado) {
                    EstadoConsulta.PENDIENTE -> {
                        chipEstado.text = "Pendiente"
                        chipEstado.setChipBackgroundColorResource(android.R.color.holo_orange_light)
                    }
                    EstadoConsulta.EN_REVISION -> {
                        chipEstado.text = "En revisión"
                        chipEstado.setChipBackgroundColorResource(android.R.color.holo_blue_light)
                    }
                    EstadoConsulta.RESPONDIDA -> {
                        chipEstado.text = "Respondida"
                        chipEstado.setChipBackgroundColorResource(android.R.color.holo_green_light)
                    }
                    EstadoConsulta.CERRADA -> {
                        chipEstado.text = "Cerrada"
                        chipEstado.setChipBackgroundColorResource(android.R.color.darker_gray)
                    }
                    EstadoConsulta.REQUIERE_SEGUIMIENTO -> {
                        chipEstado.text = "Seguimiento"
                        chipEstado.setChipBackgroundColorResource(android.R.color.holo_red_light)
                    }
                }

                // Configurar prioridad
                when (consulta.prioridad) {
                    PrioridadConsulta.URGENTE -> {
                        ivPrioridad.visibility = View.VISIBLE
                        ivPrioridad.setColorFilter(ContextCompat.getColor(binding.root.context, android.R.color.holo_red_dark))
                    }
                    PrioridadConsulta.ALTA -> {
                        ivPrioridad.visibility = View.VISIBLE
                        ivPrioridad.setColorFilter(ContextCompat.getColor(binding.root.context, android.R.color.holo_orange_dark))
                    }
                    else -> {
                        ivPrioridad.visibility = View.GONE
                    }
                }

                // Mostrar especialista asignado
                if (consulta.especialistaNombre.isNotEmpty()) {
                    tvEspecialista.text = "Atendido por: ${consulta.especialistaNombre}"
                    tvEspecialista.visibility = View.VISIBLE
                    tvEspecialista.setOnClickListener { onEspecialistaClick(consulta.especialistaAsignado) }
                } else {
                    tvEspecialista.visibility = View.GONE
                }

                root.setOnClickListener { onConsultaClick(consulta) }
            }
        }
    }
}