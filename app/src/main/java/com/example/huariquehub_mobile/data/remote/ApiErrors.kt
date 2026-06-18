package com.example.huariquehub_mobile.data.remote

import retrofit2.HttpException
import java.io.IOException

/** Traduce excepciones de red/HTTP a mensajes claros para el usuario. */
fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> when (code()) {
        400 -> "Datos inválidos. Revisa la información ingresada."
        401 -> "Correo o contraseña incorrectos."
        404 -> "No se encontró el recurso solicitado."
        409 -> "El recurso ya existe."
        in 500..599 -> "Error del servidor. Intenta nuevamente."
        else -> "Ocurrió un error (${code()}). Intenta de nuevo."
    }
    is IOException -> "Sin conexión con el servidor. Verifica tu internet."
    else -> "Ocurrió un error inesperado. Intenta de nuevo."
}
