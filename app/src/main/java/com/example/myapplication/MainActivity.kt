package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.myapplication.data.UserData
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var bannerAdapter: BannerAdapter
    private var bannerJob: Job? = null

    companion object {
        private const val TAG = "MainActivity"
        private const val EDIT_PROFILE_REQUEST = 100
    }

    // Registrar el callback para el resultado de EditProfileActivity
    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Recargar los datos del usuario
            loadUserData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "Iniciando MainActivity")

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d(TAG, "Layout inflado correctamente")

            try {
                auth = Firebase.auth
                Log.d(TAG, "Firebase Auth inicializado correctamente")
            } catch (e: Exception) {
                Log.e(TAG, "Error al inicializar Firebase Auth: ${e.message}", e)
                Toast.makeText(this, "Error al inicializar la autenticación", Toast.LENGTH_LONG).show()
                return
            }

            setupToolbar()
            setupClickListeners()
            loadUserData()
            setupBanner()
            Log.d(TAG, "MainActivity configurado correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error en onCreate: ${e.message}", e)
            Toast.makeText(this, "Error al iniciar la aplicación", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_faq -> {
                startActivity(Intent(this, FAQActivity::class.java))
                true
            }
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.action_edit_profile -> {
                // Usar el launcher en lugar de startActivity
                editProfileLauncher.launch(Intent(this, EditProfileActivity::class.java))
                true
            }
            R.id.action_logout -> {
                try {
                    auth.signOut()
                    Log.d(TAG, "Sesión cerrada correctamente")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Log.e(TAG, "Error al cerrar sesión: ${e.message}", e)
                    Toast.makeText(this, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userData = document.toObject(UserData::class.java)
                        userData?.let { user ->
                            // Mostrar el nombre del usuario en la barra superior
                            binding.userNameText.text = user.fullName
                            binding.userNameText.visibility = View.VISIBLE

                            // Si hay una URL de imagen de perfil, cargarla
                            user.profileImageUrl?.let { url ->
                                Glide.with(this)
                                    .load(url)
                                    .circleCrop()
                                    .into(binding.profileImage)
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error al cargar datos del usuario: ${e.message}")
                }
        }
    }

    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = "Apps"

            binding.profileImage.setOnClickListener {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            Log.d(TAG, "Toolbar configurada correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error en setupToolbar: ${e.message}", e)
        }
    }

    private fun setupClickListeners() {
        try {
            binding.cardDiagnosticoSintomas.setOnClickListener {
                startActivity(Intent(this, AgeGroupSelectionActivity::class.java))
            }

            binding.cardDiagnosticoNecropsia.setOnClickListener {
                startActivity(Intent(this, AgeGroupSelection2Activity::class.java))
            }

            binding.cardVideos.setOnClickListener {
                Toast.makeText(this, "Videos - En desarrollo", Toast.LENGTH_SHORT).show()
            }

            binding.cardBioseguridad.setOnClickListener {
                startActivity(Intent(this, BioseguridadActivity::class.java))
            }

            binding.cardDiagnosticoProfesional.setOnClickListener {
                startActivity(Intent(this, DiagnosticoProfesionalActivity::class.java))
            }
            Log.d(TAG, "Click listeners configurados correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error en setupClickListeners: ${e.message}", e)
        }
    }

    private fun setupBanner() {
        bannerAdapter = BannerAdapter()
        binding.bannerViewPager.adapter = bannerAdapter

        // Configurar desplazamiento automático
        lifecycleScope.launch {
            // Ejecutar solo cuando la actividad esté en primer plano
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    delay(3000) // Cambiar imagen cada 3 segundos

                    val nextItem = if (bannerAdapter.itemCount == 0) 0
                    else (binding.bannerViewPager.currentItem + 1) % bannerAdapter.itemCount

                    binding.bannerViewPager.setCurrentItem(nextItem, true)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerJob?.cancel()
    }
} 