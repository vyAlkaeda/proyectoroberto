package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.data.MexicoStates
import com.example.myapplication.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var currentPhotoUrl: String? = null

    companion object {
        private const val TAG = "EditProfileActivity"
        private val GENDERS = listOf("Masculino", "Femenino", "Otro")
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { uploadImage(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDropdowns()
        loadUserData()
        setupButtons()
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Editar Perfil"
        }
    }

    private fun setupDropdowns() {
        // Configurar dropdown de estados
        val statesAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, MexicoStates.states)
        (binding.cityAutoComplete as? AutoCompleteTextView)?.setAdapter(statesAdapter)

        // Configurar dropdown de género
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, GENDERS)
        (binding.genderAutoComplete as? AutoCompleteTextView)?.setAdapter(genderAdapter)
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        binding.apply {
                            fullNameEditText.setText(document.getString("fullName"))
                            cityAutoComplete.setText(document.getString("city"))
                            ageEditText.setText(document.getLong("age")?.toString())
                            genderAutoComplete.setText(document.getString("gender"))
                            cardNumberEditText.setText(document.getString("cardNumber"))
                            
                            // Cargar imagen de perfil
                            val profileUrl = document.getString("profileImageUrl")
                            currentPhotoUrl = profileUrl
                            if (!profileUrl.isNullOrEmpty()) {
                                Glide.with(this@EditProfileActivity)
                                    .load(profileUrl)
                                    .circleCrop()
                                    .into(profileImage)
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error al cargar datos del usuario: ${e.message}")
                    Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setupButtons() {
        binding.changePhotoButton.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun uploadImage(uri: Uri) {
        val currentUser = auth.currentUser ?: return
        val storageRef = storage.reference.child("profile_images/${currentUser.uid}")

        binding.saveButton.isEnabled = false
        binding.changePhotoButton.isEnabled = false

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    updateProfileImageUrl(downloadUri.toString())
                    Glide.with(this)
                        .load(downloadUri)
                        .circleCrop()
                        .into(binding.profileImage)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al subir imagen: ${e.message}")
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                binding.saveButton.isEnabled = true
                binding.changePhotoButton.isEnabled = true
            }
    }

    private fun updateProfileImageUrl(url: String) {
        val currentUser = auth.currentUser ?: return
        currentPhotoUrl = url
        
        db.collection("users").document(currentUser.uid)
            .update("profileImageUrl", url)
            .addOnSuccessListener {
                binding.saveButton.isEnabled = true
                binding.changePhotoButton.isEnabled = true
                Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al actualizar URL de imagen: ${e.message}")
                binding.saveButton.isEnabled = true
                binding.changePhotoButton.isEnabled = true
            }
    }

    private fun saveUserData() {
        val currentUser = auth.currentUser ?: return
        
        // Validar campos
        val fullName = binding.fullNameEditText.text.toString().trim()
        val city = binding.cityAutoComplete.text.toString().trim()
        val ageText = binding.ageEditText.text.toString().trim()
        val gender = binding.genderAutoComplete.text.toString().trim()
        val cardNumber = binding.cardNumberEditText.text.toString().trim()

        if (fullName.isEmpty() || city.isEmpty() || ageText.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageText.toIntOrNull()
        if (age == null || age < 18 || age > 120) {
            binding.ageLayout.error = "Edad inválida (18-120)"
            return
        }

        if (!MexicoStates.states.contains(city)) {
            binding.cityLayout.error = "Seleccione un estado válido"
            return
        }

        if (!GENDERS.contains(gender)) {
            binding.genderLayout.error = "Seleccione un género válido"
            return
        }

        binding.saveButton.isEnabled = false

        val userData = hashMapOf(
            "fullName" to fullName,
            "city" to city,
            "age" to age,
            "gender" to gender,
            "cardNumber" to cardNumber
        )

        db.collection("users").document(currentUser.uid)
            .update(userData as Map<String, Any>)
            .addOnSuccessListener {
                // Enviar resultado a MainActivity
                setResult(RESULT_OK)
                Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al actualizar datos: ${e.message}")
                Toast.makeText(this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show()
                binding.saveButton.isEnabled = true
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 