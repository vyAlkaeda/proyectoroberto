package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "Iniciando LoginActivity")
            
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            try {
                auth = Firebase.auth
                Log.d(TAG, "Firebase Auth inicializado correctamente")
            } catch (e: Exception) {
                Log.e(TAG, "Error al inicializar Firebase Auth: ${e.message}", e)
                Toast.makeText(this, "Error al inicializar la autenticación", Toast.LENGTH_LONG).show()
                return
            }

            // Verificar si ya hay una sesión activa
            try {
                val currentUser = auth.currentUser
                Log.d(TAG, "Usuario actual: ${currentUser?.email}")
                if (currentUser != null) {
                    Log.d(TAG, "Usuario ya autenticado, redirigiendo a MainActivity")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al verificar usuario actual: ${e.message}", e)
            }

            setupTextWatchers()
            setupButtons()
        } catch (e: Exception) {
            Log.e(TAG, "Error en onCreate: ${e.message}", e)
            Toast.makeText(this, "Error al iniciar la aplicación", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateLoginButtonState()
            }
        }

        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
    }

    private fun updateLoginButtonState() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()
        binding.loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setupButtons() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Por favor ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show()
                binding.emailEditText.error = "Formato de correo inválido"
                return@setOnClickListener
            }

            Log.d(TAG, "Intentando iniciar sesión con email: $email")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Inicio de sesión exitoso")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        val errorMessage = when (task.exception?.message) {
                            "The password is invalid or the user does not have a password." -> 
                                "Contraseña incorrecta"
                            "There is no user record corresponding to this identifier. The user may have been deleted." ->
                                "No existe una cuenta con este correo"
                            else -> "Error al iniciar sesión: ${task.exception?.message}"
                        }
                        Log.e(TAG, "Error en inicio de sesión: ${task.exception?.message}")
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
} 