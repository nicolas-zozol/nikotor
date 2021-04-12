package io.robusta.nikotor.user.fixture

import io.robusta.nikotor.user.*


//val johnDoe = User()
//val janeDoe = User("jane.doe@example.com")

internal const val johnDoeEmail = "john.doe@example.com";
internal const val janeDoeEmail = "jane.doe@example.com";
internal const val johnDoePassword = "abc";

internal fun getNewJohnDoe(): User {
    val newJohnDoe = User(johnDoeEmail)
    newJohnDoe.firstName = "John"
    newJohnDoe.lastName = "Doe"
    return newJohnDoe
}

internal fun getNewJaneDoe(): User {
    val newJohnDoe = User(janeDoeEmail)
    newJohnDoe.firstName = "Jane"
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
internal fun registerJohn() = RegisterUserCommand(RegisterPayload(getNewJohnDoe(), johnDoePassword));

internal fun registerJane() = RegisterUserCommand(RegisterPayload(getNewJaneDoe(), johnDoePassword));
//val askActivation = AskActivationCommand(HasEmailPayload(johnDoe.email))
//val activate = ActivateUserCommand(TokenPayload(johnDoe.email, token))

internal fun askJanePasswordReset() = AskPasswordResetCommand(HasEmailPayload(janeDoeEmail))
internal fun updateJohn() = UpdateUserCommand(getNewJohnDoe())
internal fun removeJohn() = RemoveUserCommand(HasEmailPayload(johnDoeEmail))

internal fun loginJohn() = LoginCommand(LoginAttemptPayload(johnDoeEmail, johnDoePassword, createRandomToken()));


