package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Date = Date()
)

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    init {
        // Mensaje de bienvenida
        _messages.value = listOf(
            ChatMessage(
                text = "¡Bienvenido al chat en vivo! ¿En qué puedo ayudarte hoy?",
                isFromUser = false
            )
        )
    }

    fun sendMessage(text: String) {
        val userMessage = ChatMessage(
            text = text,
            isFromUser = true
        )
        _messages.value = listOf(userMessage) + _messages.value

        // Simular respuesta del sistema después de un mensaje del usuario
        val systemResponse = ChatMessage(
            text = "Gracias por tu mensaje. Un profesional te responderá pronto.",
            isFromUser = false
        )
        _messages.value = listOf(systemResponse) + _messages.value
    }
} 