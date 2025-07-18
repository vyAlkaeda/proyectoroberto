package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val messages: MutableList<ChatMessage>,
    private val currentUserId: String
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2
        private const val VIEW_TYPE_DIAGNOSTICO = 3
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return when {
            message.isDiagnostico -> VIEW_TYPE_DIAGNOSTICO
            message.userId == currentUserId -> VIEW_TYPE_MY_MESSAGE
            else -> VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun updateMessages(newMessages: List<ChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layoutMensajeUsuario: View = itemView.findViewById(R.id.layoutMensajeUsuario)
        private val layoutMensajeOtro: View = itemView.findViewById(R.id.layoutMensajeOtro)
        private val layoutMensajeDiagnostico: View = itemView.findViewById(R.id.layoutMensajeDiagnostico)

        // Views para mensaje del usuario
        private val tvMensajeUsuario: TextView = itemView.findViewById(R.id.tvMensajeUsuario)
        private val tvTimestampUsuario: TextView = itemView.findViewById(R.id.tvTimestampUsuario)

        // Views para mensaje de otro
        private val tvNombreOtro: TextView = itemView.findViewById(R.id.tvNombreOtro)
        private val tvMensajeOtro: TextView = itemView.findViewById(R.id.tvMensajeOtro)
        private val tvTimestampOtro: TextView = itemView.findViewById(R.id.tvTimestampOtro)

        // Views para mensaje de diagnóstico
        private val tvNombreDiagnostico: TextView = itemView.findViewById(R.id.tvNombreDiagnostico)
        private val tvMensajeDiagnostico: TextView = itemView.findViewById(R.id.tvMensajeDiagnostico)
        private val tvTimestampDiagnostico: TextView = itemView.findViewById(R.id.tvTimestampDiagnostico)

        fun bind(message: ChatMessage) {
            // Ocultar todos los layouts primero
            layoutMensajeUsuario.visibility = View.GONE
            layoutMensajeOtro.visibility = View.GONE
            layoutMensajeDiagnostico.visibility = View.GONE

            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timestamp = dateFormat.format(message.timestamp.toDate())

            when {
                message.isDiagnostico -> {
                    layoutMensajeDiagnostico.visibility = View.VISIBLE
                    tvNombreDiagnostico.text = "Diagnóstico de ${message.userName}"
                    tvMensajeDiagnostico.text = "He enviado un diagnóstico profesional"
                    tvTimestampDiagnostico.text = timestamp
                }
                message.userId == currentUserId -> {
                    layoutMensajeUsuario.visibility = View.VISIBLE
                    tvMensajeUsuario.text = message.message
                    tvTimestampUsuario.text = timestamp
                }
                else -> {
                    layoutMensajeOtro.visibility = View.VISIBLE
                    tvNombreOtro.text = message.userName
                    tvMensajeOtro.text = message.message
                    tvTimestampOtro.text = timestamp
                }
            }
        }
    }
} 