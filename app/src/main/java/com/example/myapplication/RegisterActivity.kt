package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.MexicoStates
import com.example.myapplication.data.UserData
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    companion object {
        private const val TAG = "RegisterActivity"
        private val GENDERS = listOf("Masculino", "Femenino", "Otro")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Iniciando RegisterActivity")
        
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        auth = Firebase.auth
        db = Firebase.firestore
        
        setupDropdowns()
        setupTextWatchers()
        setupRegisterButton()
        
        Log.d(TAG, "RegisterActivity configurado correctamente")
    }

    private fun navigateToLogin() {
        Log.d(TAG, "Iniciando navegación a LoginActivity")
        try {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
            Log.d(TAG, "Navegación a LoginActivity completada")
        } catch (e: Exception) {
            Log.e(TAG, "Error al navegar a LoginActivity: ${e.message}", e)
        }
    }

    private fun setupDropdowns() {
        try {
            // Configurar dropdown de estados
            val statesAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, MexicoStates.states)
            (binding.cityAutoComplete as? AutoCompleteTextView)?.setAdapter(statesAdapter)

            // Configurar dropdown de género
            val genderAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, GENDERS)
            (binding.genderAutoComplete as? AutoCompleteTextView)?.setAdapter(genderAdapter)
            Log.d(TAG, "Dropdowns configurados correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al configurar dropdowns: ${e.message}", e)
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateFields()
            }
        }

        binding.fullNameEditText.addTextChangedListener(textWatcher)
        binding.cityAutoComplete.addTextChangedListener(textWatcher)
        binding.ageEditText.addTextChangedListener(textWatcher)
        binding.genderAutoComplete.addTextChangedListener(textWatcher)
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
        Log.d(TAG, "TextWatchers configurados correctamente")
    }

    private fun validateFields(): Boolean {
        var isValid = true
        binding.apply {
            // Validar nombre
            if (fullNameEditText.text.toString().trim().isEmpty()) {
                fullNameLayout.error = "Campo requerido"
                isValid = false
            } else {
                fullNameLayout.error = null
            }

            // Validar ciudad
            val city = cityAutoComplete.text.toString().trim()
            if (city.isEmpty() || !MexicoStates.states.contains(city)) {
                cityLayout.error = "Seleccione un estado válido"
                isValid = false
            } else {
                cityLayout.error = null
            }

            // Validar edad
            val age = ageEditText.text.toString().trim()
            if (age.isEmpty()) {
                ageLayout.error = "Campo requerido"
                isValid = false
            } else {
                val ageNum = age.toIntOrNull()
                if (ageNum == null || ageNum < 18 || ageNum > 120) {
                    ageLayout.error = "Edad inválida (18-120)"
                    isValid = false
                } else {
                    ageLayout.error = null
                }
            }

            // Validar género
            val gender = genderAutoComplete.text.toString().trim()
            if (gender.isEmpty() || !GENDERS.contains(gender)) {
                genderLayout.error = "Seleccione un género válido"
                isValid = false
            } else {
                genderLayout.error = null
            }

            // Validar email
            val email = emailEditText.text.toString().trim()
            if (email.isEmpty()) {
                emailLayout.error = "Campo requerido"
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLayout.error = "Email inválido"
                isValid = false
            } else {
                emailLayout.error = null
            }

            // Validar contraseña
            val password = passwordEditText.text.toString()
            if (password.isEmpty()) {
                passwordLayout.error = "Campo requerido"
                isValid = false
            } else if (password.length < 6) {
                passwordLayout.error = "Mínimo 6 caracteres"
                isValid = false
            } else {
                passwordLayout.error = null
            }

            registerButton.isEnabled = isValid
        }
        return isValid
    }

    private fun setupRegisterButton() {
        binding.registerButton.setOnClickListener {
            Log.d(TAG, "Botón de registro presionado")
            
            if (!validateFields()) {
                Log.d(TAG, "Validación de campos fallida")
                Toast.makeText(this, "Por favor, llene todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Deshabilitar el botón y mostrar progreso
            binding.registerButton.isEnabled = false
            binding.registerButton.text = "Registrando..."

            try {
                // Obtener los valores de los campos
                val fullName = binding.fullNameEditText.text.toString().trim()
                val city = binding.cityAutoComplete.text.toString().trim()
                val age = binding.ageEditText.text.toString().trim().toInt()
                val gender = binding.genderAutoComplete.text.toString().trim()
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString()

                Log.d(TAG, "Iniciando registro en Firebase Auth para: $email")

                // Crear usuario en Firebase Auth
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        Log.d(TAG, "Usuario creado exitosamente en Authentication")
                        val user = authResult.user
                        if (user != null) {
                            // Crear objeto con los datos del usuario
                            val userData = hashMapOf(
                                "uid" to user.uid,
                                "fullName" to fullName,
                                "city" to city,
                                "age" to age,
                                "gender" to gender,
                                "email" to email,
                                "points" to 0,
                                "profileImageUrl" to "",
                                "cardNumber" to ""
                            )

                            Log.d(TAG, "Guardando datos en Firestore para uid: ${user.uid}")
                            
                            // Guardar información en Firestore
                            db.collection("users")
                                .document(user.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Datos guardados exitosamente en Firestore")
                                    
                                    // Cerrar sesión después del registro
                                    auth.signOut()
                                    Log.d(TAG, "Sesión cerrada después del registro")

                                    // Mostrar mensaje y navegar
                                    runOnUiThread {
                                        Toast.makeText(this@RegisterActivity, "Registro completo", Toast.LENGTH_SHORT).show()
                                        Log.d(TAG, "Toast mostrado, procediendo a navegar")
                                        navigateToLogin()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Error al guardar en Firestore: ${e.message}", e)
                                    // Si falla el guardado en Firestore, eliminar la cuenta de autenticación
                                    user.delete().addOnCompleteListener { deleteTask ->
                                        runOnUiThread {
                                            binding.registerButton.isEnabled = true
                                            binding.registerButton.text = "Registrarse"
                                            Toast.makeText(this, "Error al guardar información: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                        } else {
                            Log.e(TAG, "Error: Usuario nulo después de registro exitoso")
                            runOnUiThread {
                                binding.registerButton.isEnabled = true
                                binding.registerButton.text = "Registrarse"
                                Toast.makeText(this, "Error en el registro: Usuario no creado", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error en registro de Authentication: ${e.message}", e)
                        runOnUiThread {
                            binding.registerButton.isEnabled = true
                            binding.registerButton.text = "Registrarse"
                            val errorMessage = when (e.message) {
                                "The email address is already in use by another account." ->
                                    "Ya existe una cuenta con este correo"
                                "The email address is badly formatted." ->
                                    "El formato del correo electrónico no es válido"
                                "The given password is invalid. [ Password should be at least 6 characters ]" ->
                                    "La contraseña debe tener al menos 6 caracteres"
                                else -> "Error al registrarse: ${e.message}"
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error en el proceso de registro: ${e.message}", e)
                runOnUiThread {
                    binding.registerButton.isEnabled = true
                    binding.registerButton.text = "Registrarse"
                    Toast.makeText(this, "Error en el registro: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
} 