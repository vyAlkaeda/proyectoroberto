package com.example.myapplication.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConsultaEspecializada(
    @DocumentId
    val id: String = "",
    val usuarioId: String = "",
    val usuarioNombre: String = "",
    val usuarioEmail: String = "",
    val titulo: String = "",
    val descripcionProblema: String = "",
    val categoria: String = "", // Respiratorio, Digestivo, etc.
    val prioridad: PrioridadConsulta = PrioridadConsulta.MEDIA,
    val estado: EstadoConsulta = EstadoConsulta.PENDIENTE,
    val fechaCreacion: Timestamp = Timestamp.now(),
    val fechaUltimaRespuesta: Timestamp? = null,
    val especialistaAsignado: String = "",
    val especialistaNombre: String = "",
    val imagenes: List<String> = emptyList(), // URLs de imágenes
    val etiquetas: List<String> = emptyList(), // Tags para clasificación
    val esPublica: Boolean = false, // Si otros usuarios pueden ver la consulta
    val numeroRespuestas: Int = 0
) : Parcelable

@Parcelize
data class RespuestaEspecializada(
    @DocumentId
    val id: String = "",
    val consultaId: String = "",
    val autorId: String = "",
    val autorNombre: String = "",
    val autorTipo: TipoUsuario = TipoUsuario.USUARIO,
    val mensaje: String = "",
    val fechaRespuesta: Timestamp = Timestamp.now(),
    val imagenes: List<String> = emptyList(),
    val esRespuestaOficial: Boolean = false, // Respuesta del especialista
    val valoracion: Int = 0, // 1-5 estrellas
    val esUtil: Boolean = false // Marcada como útil por el usuario original
) : Parcelable

@Parcelize
data class EspecialistaVeterinario(
    @DocumentId
    val id: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val especialidades: List<String> = emptyList(), // Sistemas en los que se especializa
    val cedula: String = "",
    val universidad: String = "",
    val experienciaAnios: Int = 0,
    val calificacion: Double = 0.0,
    val numeroConsultasAtendidas: Int = 0,
    val estado: EstadoEspecialista = EstadoEspecialista.DISPONIBLE,
    val horariosAtencion: List<String> = emptyList(),
    val fechaRegistro: Timestamp = Timestamp.now(),
    val perfilImagenUrl: String = "",
    val biografia: String = "",
    val certificaciones: List<String> = emptyList()
) : Parcelable

@Parcelize
data class NotificacionForo(
    @DocumentId
    val id: String = "",
    val usuarioId: String = "",
    val titulo: String = "",
    val mensaje: String = "",
    val tipo: TipoNotificacion = TipoNotificacion.RESPUESTA_CONSULTA,
    val consultaId: String = "",
    val fechaCreacion: Timestamp = Timestamp.now(),
    val leida: Boolean = false,
    val accionUrl: String = "" // Para navegar a la consulta específica
) : Parcelable

enum class PrioridadConsulta {
    BAJA, MEDIA, ALTA, URGENTE
}

enum class EstadoConsulta {
    PENDIENTE, 
    EN_REVISION, 
    RESPONDIDA, 
    CERRADA, 
    REQUIERE_SEGUIMIENTO
}

enum class TipoUsuario {
    USUARIO, 
    ESPECIALISTA, 
    ADMINISTRADOR
}

enum class EstadoEspecialista {
    DISPONIBLE, 
    OCUPADO, 
    DESCONECTADO, 
    VACACIONES
}

enum class TipoNotificacion {
    RESPUESTA_CONSULTA,
    CONSULTA_ASIGNADA,
    CONSULTA_CERRADA,
    SEGUIMIENTO_REQUERIDO,
    NUEVA_CONSULTA_PUBLICA
}

// Clase para estadísticas del foro
data class EstadisticasForo(
    val totalConsultas: Int = 0,
    val consultasPendientes: Int = 0,
    val consultasRespondidas: Int = 0,
    val tiempoPromedioRespuesta: Double = 0.0, // en horas
    val especialistasActivos: Int = 0,
    val satisfaccionPromedio: Double = 0.0
)