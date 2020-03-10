package io.robusta.nikotor.user.fixture

import io.robusta.nikotor.user.*


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
//val askActivation = AskActivationCommand(HasEmailPayload(johnDoe.email))
//val activate = ActivateUserCommand(TokenPayload(johnDoe.email, token))

val askJanePasswordReset = AskPasswordResetCommand(HasEmailPayload(janeDoe.email))
val updateJohn = UpdateUserCommand(getNewJohnDoe())
val removeJohn = RemoveUserCommand(HasEmailPayload(johnDoe.email))


