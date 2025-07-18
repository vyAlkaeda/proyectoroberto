package com.example.myapplication.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserManager {
    companion object {
        private const val TAG = "UserManager"
        private val auth = FirebaseAuth.getInstance()
        private val db = FirebaseFirestore.getInstance()
        
        /**
         * Obtiene los datos del usuario actual desde Firestore
         */
        suspend fun getCurrentUserData(): UserData? {
            return try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val document = db.collection("users").document(currentUser.uid).get().await()
                    if (document.exists()) {
                        document.toObject(UserData::class.java)
                    } else {
                        null
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener datos del usuario: ${e.message}")
                null
            }
        }
        
        /**
         * Verifica si el usuario actual tiene datos en Firestore
         */
        suspend fun hasUserData(): Boolean {
            return try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val document = db.collection("users").document(currentUser.uid).get().await()
                    document.exists()
                } else {
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al verificar datos del usuario: ${e.message}")
                false
            }
        }
        
        /**
         * Crea datos básicos para un usuario si no existen
         */
        suspend fun createBasicUserData(): Boolean {
            return try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val userData = UserData(
                        uid = currentUser.uid,
                        fullName = "Usuario",
                        email = currentUser.email ?: "",
                        points = 0,
                        profileImageUrl = "",
                        cardNumber = ""
                    )
                    
                    db.collection("users").document(currentUser.uid).set(userData).await()
                    Log.d(TAG, "Datos básicos del usuario creados")
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al crear datos básicos del usuario: ${e.message}")
                false
            }
        }
        
        /**
         * Actualiza los datos del usuario en Firestore
         */
        suspend fun updateUserData(userData: UserData): Boolean {
            return try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    db.collection("users").document(currentUser.uid).set(userData).await()
                    Log.d(TAG, "Datos del usuario actualizados")
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al actualizar datos del usuario: ${e.message}")
                false
            }
        }
        
        /**
         * Obtiene el ID del usuario actual
         */
        fun getCurrentUserId(): String? {
            return auth.currentUser?.uid
        }
        
        /**
         * Verifica si hay un usuario autenticado
         */
        fun isUserLoggedIn(): Boolean {
            return auth.currentUser != null
        }
    }
} 