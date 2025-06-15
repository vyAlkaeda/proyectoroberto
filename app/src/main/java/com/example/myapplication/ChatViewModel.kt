package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.util.*

data class ChatMessage(
    val text: String,
    val timestamp: Date = Date(),
    val isFromUser: Boolean = true
)

class ChatViewModel : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    init {
        // Agregar algunos mensajes de ejemplo
        _messages.add(
            ChatMessage(
                text = "¡Bienvenidos al foro de discusión! Aquí pueden compartir sus experiencias y hacer preguntas.",
                isFromUser = false
            )
        )
    }

    fun sendMessage(text: String) {
        _messages.add(0, ChatMessage(text = text))
    }
} 