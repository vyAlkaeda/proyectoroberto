package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myapplication.data.UserData
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.DocumentSnapshot

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    companion object {
        private const val TAG = "ProfileActivity"
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri ->
            val user = auth.currentUser
            if (user != null) {
                val imageRef = storage.reference.child("profile_images/${user.uid}")
                imageRef.putFile(imageUri)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            // Actualizar la URL de la imagen en Firestore
                            db.collection("users").document(user.uid)
                                .update("profileImageUrl", downloadUri.toString())
                                .addOnSuccessListener {
                                    // Cargar la nueva imagen
                                    loadProfileImage(downloadUri.toString())
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al subir la imagen: ${e.message}")
                        Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadUserData()
        setupPhotoButton()
        setupDeleteAccountButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Mostrar el email del usuario actual mientras se cargan los datos
            binding.emailText.text = currentUser.email ?: "No disponible"
            
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userData = document.toObject(UserData::class.java)
                        userData?.let { user ->
                            binding.fullNameText.text = user.fullName.ifEmpty { "No especificado" }
                            binding.emailText.text = user.email.ifEmpty { currentUser.email ?: "No disponible" }
                            binding.ageText.text = if (user.age > 0) "Edad: ${user.age} años" else "Edad: No especificada"
                            binding.genderText.text = "Género: ${user.gender.ifEmpty { "No especificado" }}"
                            binding.cityText.text = "Estado: ${user.city.ifEmpty { "No especificado" }}"
                            binding.pointsText.text = "Puntos acumulados: ${user.points}"
                            binding.cardNumberText.text = user.cardNumber?.let { "Tarjeta: $it" } ?: "Tarjeta: No registrada"
                            
                            // Cargar imagen de perfil si existe
                            user.profileImageUrl?.let { url ->
                                loadProfileImage(url)
                            }
                        }
                    } else {
                        // Si el documento no existe, mostrar valores por defecto
                        setDefaultValues(currentUser.email ?: "No disponible")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error al cargar datos del usuario: ${e.message}")
                    Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
                    // En caso de error, mostrar valores por defecto
                    setDefaultValues(currentUser.email ?: "No disponible")
                }
        }
    }

    private fun setDefaultValues(email: String) {
        binding.fullNameText.text = "No especificado"
        binding.emailText.text = email
        binding.ageText.text = "Edad: No especificada"
        binding.genderText.text = "Género: No especificado"
        binding.cityText.text = "Estado: No especificado"
        binding.pointsText.text = "Puntos acumulados: 0"
        binding.cardNumberText.text = "Tarjeta: No registrada"
    }

    private fun loadProfileImage(url: String) {
        Glide.with(this)
            .load(url)
            .circleCrop()
            .into(binding.profileImage)
    }

    private fun setupPhotoButton() {
        binding.changePhotoButton.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun setupDeleteAccountButton() {
        binding.deleteAccountButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val currentUser = auth.currentUser
        if (currentUser == null || currentUser.email == null) {
            Toast.makeText(this, "Error: No se encontró información de la cuenta", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Eliminar cuenta")
            .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
            .setPositiveButton("ELIMINAR") { dialog, _ ->
                dialog.dismiss()
                deleteUserAccount(currentUser.email!!)
            }
            .setNegativeButton("CANCELAR") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteUserAccount(userEmail: String) {
        // Mostrar diálogo de progreso
        val progressDialog = AlertDialog.Builder(this)
            .setMessage("Eliminando cuenta...")
            .setCancelable(false)
            .create()
        progressDialog.show()

        // Obtener el usuario actual
        val currentUser = auth.currentUser
        if (currentUser == null) {
            progressDialog.dismiss()
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Usar lifecycleScope para manejar las operaciones asíncronas
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // 1. Primero eliminar la cuenta de autenticación
                withContext(Dispatchers.IO) {
                    currentUser.delete().await()
                }

                // 2. Intentar eliminar datos de Firestore si existen
                withContext(Dispatchers.IO) {
                    try {
                        // Buscar y eliminar documento del usuario
                        val userQuery = db.collection("users")
                            .whereEqualTo("email", userEmail)
                            .get()
                            .await()

                        // Verificar si se encontraron documentos
                        when {
                            userQuery.documents.isNotEmpty() -> {
                                val userId = userQuery.documents.first().id
                                
                                // Eliminar documento del usuario
                                db.collection("users").document(userId).delete().await()

                                // Eliminar mensajes del chat
                                db.collection("chats")
                                    .whereEqualTo("senderId", userId)
                                    .get()
                                    .await()
                                    .documents
                                    .forEach { it.reference.delete().await() }

                                // Eliminar imagen de perfil
                                try {
                                    storage.reference.child("profile_images/$userId").delete().await()
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error al eliminar imagen de perfil: ${e.message}")
                                }

                                // Eliminar otros datos relacionados
                                listOf("diagnostics", "points").forEach { collection ->
                                    db.collection(collection)
                                        .whereEqualTo("userId", userId)
                                        .get()
                                        .await()
                                        .documents
                                        .forEach { it.reference.delete().await() }
                                }
                            }
                            else -> {
                                Log.d(TAG, "No se encontraron datos adicionales para eliminar")
                            }
                        }
                    } catch (e: Exception) {
                        // Si hay error al eliminar datos de Firestore, lo registramos pero no detenemos el proceso
                        Log.e(TAG, "Error al eliminar datos de Firestore: ${e.message}")
                    }
                }

                // 3. Limpiar datos locales y cerrar sesión
                auth.signOut()
                clearAppData()

                // 4. Cerrar el diálogo y mostrar mensaje de éxito
                progressDialog.dismiss()
                Toast.makeText(this@ProfileActivity, "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show()

                // 5. Redirigir al login
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()

            } catch (e: Exception) {
                // Manejar cualquier error
                progressDialog.dismiss()
                Log.e(TAG, "Error al eliminar la cuenta: ${e.message}")
                Toast.makeText(
                    this@ProfileActivity,
                    "Error al eliminar la cuenta: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun clearAppData() {
        try {
            // Limpiar caché
            cacheDir.deleteRecursively()
            
            // Limpiar archivos
            filesDir.deleteRecursively()
            
            // Limpiar shared preferences
            getSharedPreferences(packageName + "_preferences", MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error al limpiar datos locales: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
} 