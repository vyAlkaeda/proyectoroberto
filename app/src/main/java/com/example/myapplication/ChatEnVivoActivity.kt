package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.ChatMessage
import com.example.myapplication.data.DiagnosticoData
import com.example.myapplication.data.UserData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class ChatEnVivoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var etMensaje: EditText
    private lateinit var btnEnviar: Button
    private lateinit var cardDiagnostico: android.view.View
    private lateinit var tvDiagnosticoResumen: TextView
    private lateinit var btnVerDiagnosticoCompleto: Button

    private var diagnosticoData: DiagnosticoData? = null
    private var userData: UserData? = null
    private val messages = mutableListOf<ChatMessage>()

    companion object {
        const val EXTRA_DIAGNOSTICO_DATA = "diagnostico_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_en_vivo)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Inicializar vistas
        setupViews()
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()

        // Cargar datos
        cargarDatosUsuario()
        cargarDiagnostico()
        cargarMensajes()
    }

    private fun setupViews() {
        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        etMensaje = findViewById(R.id.etMensaje)
        btnEnviar = findViewById(R.id.btnEnviar)
        cardDiagnostico = findViewById(R.id.cardDiagnostico)
        tvDiagnosticoResumen = findViewById(R.id.tvDiagnosticoResumen)
        btnVerDiagnosticoCompleto = findViewById(R.id.btnVerDiagnosticoCompleto)
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat en Vivo - Diagnóstico Profesional"
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages, auth.currentUser?.uid ?: "")
        recyclerViewChat.layoutManager = LinearLayoutManager(this)
        recyclerViewChat.adapter = chatAdapter
    }

    private fun setupClickListeners() {
        btnEnviar.setOnClickListener {
            enviarMensaje()
        }

        btnVerDiagnosticoCompleto.setOnClickListener {
            mostrarDiagnosticoCompleto()
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
                    }
                }
        }
    }

    private fun cargarDiagnostico() {
        diagnosticoData = intent.getParcelableExtra(EXTRA_DIAGNOSTICO_DATA)
        if (diagnosticoData != null) {
            mostrarResumenDiagnostico()
            enviarMensajeDiagnostico()
        }
    }

    private fun mostrarResumenDiagnostico() {
        diagnosticoData?.let { diagnostico ->
            val sintomasSeleccionados = mutableListOf<String>()
            
            if (diagnostico.sintomasNerviosos.isNotEmpty()) sintomasSeleccionados.add("Nerviosos: ${diagnostico.sintomasNerviosos.size}")
            if (diagnostico.sintomasDigestivos.isNotEmpty()) sintomasSeleccionados.add("Digestivos: ${diagnostico.sintomasDigestivos.size}")
            if (diagnostico.sintomasRespiratorios.isNotEmpty()) sintomasSeleccionados.add("Respiratorios: ${diagnostico.sintomasRespiratorios.size}")
            if (diagnostico.sintomasReproductivos.isNotEmpty()) sintomasSeleccionados.add("Reproductivos: ${diagnostico.sintomasReproductivos.size}")
            if (diagnostico.sintomasTegumentarios.isNotEmpty()) sintomasSeleccionados.add("Tegumentarios: ${diagnostico.sintomasTegumentarios.size}")
            if (diagnostico.sintomasMusculoesqueleticos.isNotEmpty()) sintomasSeleccionados.add("Musculoesqueléticos: ${diagnostico.sintomasMusculoesqueleticos.size}")

            val resumen = "Edad: ${diagnostico.edadCerdos}, Área: ${diagnostico.areaProduccion}\n" +
                    "Síntomas: ${sintomasSeleccionados.joinToString(", ")}"

            tvDiagnosticoResumen.text = resumen
            cardDiagnostico.visibility = android.view.View.VISIBLE
        }
    }

    private fun enviarMensajeDiagnostico() {
        val currentUser = auth.currentUser
        if (currentUser != null && diagnosticoData != null) {
            val chatMessage = ChatMessage(
                userId = currentUser.uid,
                userName = userData?.fullName ?: "Usuario",
                userEmail = userData?.email ?: currentUser.email ?: "",
                message = "He enviado un diagnóstico profesional",
                timestamp = Timestamp.now(),
                diagnosticoId = diagnosticoData!!.id,
                isDiagnostico = true
            )

            // Guardar en Firestore
            db.collection("chat_messages")
                .add(chatMessage)
                .addOnSuccessListener { documentReference ->
                    chatMessage.copy(id = documentReference.id)
                    chatAdapter.addMessage(chatMessage)
                    scrollToBottom()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al enviar diagnóstico: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun cargarMensajes() {
        db.collection("chat_messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Error al cargar mensajes: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val newMessages = mutableListOf<ChatMessage>()
                    for (document in snapshot.documents) {
                        val message = document.toObject(ChatMessage::class.java)
                        message?.let { newMessages.add(it.copy(id = document.id)) }
                    }
                    chatAdapter.updateMessages(newMessages)
                    scrollToBottom()
                }
            }
    }

    private fun enviarMensaje() {
        val mensaje = etMensaje.text.toString().trim()
        if (TextUtils.isEmpty(mensaje)) {
            Toast.makeText(this, "Por favor escribe un mensaje", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val chatMessage = ChatMessage(
                userId = currentUser.uid,
                userName = userData?.fullName ?: "Usuario",
                userEmail = userData?.email ?: currentUser.email ?: "",
                message = mensaje,
                timestamp = Timestamp.now(),
                diagnosticoId = diagnosticoData?.id ?: "",
                isDiagnostico = false
            )

            // Guardar en Firestore
            db.collection("chat_messages")
                .add(chatMessage)
                .addOnSuccessListener { documentReference ->
                    chatMessage.copy(id = documentReference.id)
                    chatAdapter.addMessage(chatMessage)
                    etMensaje.text.clear()
                    scrollToBottom()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al enviar mensaje: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun mostrarDiagnosticoCompleto() {
        diagnosticoData?.let { diagnostico ->
            val intent = Intent(this, DiagnosticoResultadosActivity::class.java)
            intent.putExtra(DiagnosticoResultadosActivity.EXTRA_DIAGNOSTICO_DATA, diagnostico)
            startActivity(intent)
        }
    }

    private fun scrollToBottom() {
        recyclerViewChat.post {
            recyclerViewChat.smoothScrollToPosition(messages.size)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 