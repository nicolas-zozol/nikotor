package io.robusta.nikotor.user.fixture

import io.robusta.nikotor.InMemoryEventStore
import io.robusta.nikotor.NikotorValidationException
import io.robusta.nikotor.SimpleNikotorEngine
import io.robusta.nikotor.core.NikotorEngine
import io.robusta.nikotor.user.*

/**
 * Fixtures
 */
val token = "1234"
val newPassword = "abcd"


val johnDoe = User("john.doe@example.com")
val janeDoe = User("jane.doe@example.com")

fun getNewJohnDoe(): User {
    val newJohnDoe = User(johnDoe.email)
    newJohnDoe.firstName = "John"
    newJohnDoe.lastName = "Doe"
    return newJohnDoe
}

class FakeEmailSender() : EmailSender {
    override fun sendEmail(target: String, sender: String, subject: String, content: String) {
        sent++
    }

    var sent = 0
}

/**
 * Commands using fixtures
 */
val registerJohn = RegisterUserCommand(RegisterPayload(johnDoe, "abc"));
val registerJane = RegisterUserCommand(RegisterPayload(janeDoe, "abc"));
val askActivation = AskActivationCommand(HasEmailPayload(johnDoe.email))
val activate = ActivateUserCommand(TokenPayload(johnDoe.email, token))
val changePassword = ChangePasswordCommand(HashedPasswordPayload(janeDoe.email, newPassword))
val askPasswordReset = AskPasswordResetCommand(HasEmailPayload(janeDoe.email))
val updateUser = UpdateUserCommand(getNewJohnDoe())
val removeUser = RemoveUserCommand(HasEmailPayload(johnDoe.email))


