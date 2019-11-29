package io.robusta.nikotor.user

import io.robusta.nikotor.*
import java.util.concurrent.CompletableFuture

interface RegisterUserPayload {
    val user: User
    val author: User
}

data class RegisterUserCommand(override val payload: RegisterUserPayload) : ThrowableCommand<RegisterUserPayload>("", payload) {

    override fun runUnit() {
        val email = payload.user.email
        queryUserByEmail(email).get() ?: throw IllegalStateException("email $email is already used")
    }

    override fun validate(): ValidationResult {
        val user = payload.user
        val author = payload.author

        return ValidationResult()
                .check("author", author.email.isNotEmpty(), "Author is not valid")
                .check("email", user.email.isNotEmpty(), "email is not valid")
                .check("password", !user.password.isNullOrEmpty(), "password is not set")
    }

    override fun generateEvent(): NikotorEvent<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}