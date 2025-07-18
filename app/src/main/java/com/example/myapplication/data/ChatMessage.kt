package com.example.myapplication.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatMessage(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val diagnosticoId: String = "", // ID del diagnóstico asociado
    val isDiagnostico: Boolean = false // Si es true, es un diagnóstico, si es false es un comentario
) : Parcelable 