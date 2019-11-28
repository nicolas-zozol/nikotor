package io.robusta.nikotor

import java.lang.RuntimeException


data class NikotorReason(
    val message: String,
    val status: Int = 500,
    val type: String = ErrorTypes.SERVER_ERROR,
    val value: Any = Unit
) {

}

object ErrorTypes {
    const val VALIDATION_ERROR = "VALIDATION_ERROR";
    const val COMMAND_ERROR = "COMMAND_ERROR";
    val PROJECTION_ERROR = "PROJECTION_ERROR";
    val SERVER_ERROR = "SERVER_ERROR";
}

class NikotorException(message: String, status: Int, type: String,  value: Any = Unit) : RuntimeException() {
    val reason = NikotorReason(message, status, type, value);
}

