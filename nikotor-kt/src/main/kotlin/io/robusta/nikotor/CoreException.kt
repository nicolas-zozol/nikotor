package io.robusta.nikotor

import java.lang.RuntimeException


data class NikotorReason(
    val message: String,
    val status: Int = 500,
    val type: String = ErrorTypes.SERVER_ERROR,
    val value: Any = Unit
)

object ErrorTypes {
    const val VALIDATION_ERROR = "VALIDATION_ERROR"
    const val COMMAND_ERROR = "COMMAND_ERROR"
    val PROJECTION_ERROR = "PROJECTION_ERROR"
    val SERVER_ERROR = "SERVER_ERROR"
}

open class NikotorException(message: String, status: Int, type: String,  value: Any = Unit) : RuntimeException(message) {
    val reason = NikotorReason(message, status, type, value)
}

class NikotorValidationException(message: String, value: Any = Unit) : NikotorException(message, 400, ErrorTypes.VALIDATION_ERROR, value) {
}

class NotFoundException(message: String, value: Any = Unit) : NikotorException(message, 404, ErrorTypes.SERVER_ERROR, value) {
}

