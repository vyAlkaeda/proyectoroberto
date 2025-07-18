package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.myapplication.data.ChatMessage
import com.google.firebase.Timestamp

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    init {
        // Mensaje de bienvenida
        _messages.value = listOf(
            ChatMessage(
                userId = "system",
                userName = "Sistema",
                userEmail = "",
                message = "¡Bienvenido al chat en vivo! ¿En qué puedo ayudarte hoy?",
                timestamp = Timestamp.now(),
                diagnosticoId = "",
                isDiagnostico = false
            )
        )
    }

    fun sendMessage(text: String) {
        val userMessage = ChatMessage(
            userId = "user",
            userName = "Usuario",
            userEmail = "",
            message = text,
            timestamp = Timestamp.now(),
            diagnosticoId = "",
            isDiagnostico = false
        )
        _messages.value = listOf(userMessage) + _messages.value

        // Simular respuesta del sistema después de un mensaje del usuario
        val systemResponse = ChatMessage(
            userId = "system",
            userName = "Sistema",
            userEmail = "",
            message = "Gracias por tu mensaje. Un profesional te responderá pronto.",
            timestamp = Timestamp.now(),
            diagnosticoId = "",
            isDiagnostico = false
        )
        _messages.value = listOf(systemResponse) + _messages.value
    }
} 