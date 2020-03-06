package io.robusta.nikotor.user

import io.robusta.nikotor.*
import io.robusta.nikotor.core.*
import java.util.*


data class RegisterUserCommand(override val payload: RegisterPayload) : RunnableCommand<RegisterPayload, Token>(payload) {

    override suspend fun run(): Token {
        val email = payload.user.email
        if (queryUserByEmail(email) != null) {
            throw IllegalStateException("email $email is already used")
        }
        return createRandomToken()
    }


    override fun validate(): ValidationResult {

        return ValidationResult()
                .check(payload.user.email.isNotEmpty(), "email is not valid")
                .check(payload.password.isNotEmpty(), "password is not set")
    }

    override fun generateEvent(result: Token): UserRegisteredEvent {
        return UserRegisteredEvent(RegisterEventPayload(payload.user, result))
    }

}

data class LoginCommand(override val payload: WithPasswordPayload) : RunnableCommand<WithPasswordPayload, Boolean>(payload) {
    override suspend fun run(): Boolean {
        return privateQueryCheckPassword(payload.email, hashPassword(payload.password))
    }

    override fun validate(): ValidationResult {
        return ValidationResult()
                .check(payload.email.isNotEmpty(), "Sent email is not valid")
                .check(payload.password.isNotEmpty(), "Sent password is not valid")
    }

    override fun generateEvent(result: Boolean): Event<*> {
        return UserLoggedEvent(payload)
    }

}

/*
class AskActivationCommand(override val payload: HasEmailPayload) :
        Command<HasEmail, TokenPayload> {

    override fun validate(): ValidationResult {
        return ValidationResult().check(payload.email.isNotEmpty(), "Email is empty")
    }

    override suspend fun run(): TokenPayload {
        val token = UUID.randomUUID().toString();
        return TokenPayload(payload.email, token)
    }

    override fun generateEvents(result: TokenPayload): Events {
        return listOf(AskPasswordResetEvent(HasEmailPayload(result.email)))
    }

}
*/
class ActivateUserCommand(override val payload: TokenPayload) : ThrowableCommand<TokenPayload>(payload) {
    override fun validate(): ValidationResult {
        return ValidationResult().check(payload.email.isNotEmpty(), "Email is empty")
    }

    override fun runUnit() {
        val email = payload.email
        val user = queryUserByEmail(email) ?: throw NikotorValidationException("email $email does not exist")

        val activationToken = queryActivationTokenByEmail(payload.email)
        ValidationResult()
                .check(!user.activated, "User $email is already activated")
                .check(activationToken == payload.token, "Activation key ${payload.token} is wrong for user $email")
                .throwIfInvalid()

    }

    override fun generateEvent(): Event<*> {
        return UserActivatedEvent(payload)
    }
}


class ChangePasswordCommand(override val payload: HashedPasswordPayload) : RunnableCommand<HashedPasswordPayload, String>(payload) {

    override fun validate(): ValidationResult {
        return ValidationResult().check(
                payload.password.isNotEmpty(), "Password for ${payload.email} is empty")
    }

    override suspend fun run(): String {
        return (hashPassword(payload.password))
    }

    override fun generateEvent(result: String): Event<*> {
        return PasswordUpdatedEvent(payload)
    }

}


class AskPasswordResetCommand(override val payload: HasEmailPayload) :
        Command<HasEmail, TokenPayload> {

    override fun validate(): ValidationResult {
        return ValidationResult().check(payload.email.isNotEmpty(), "Email is empty")
    }

    override suspend fun run(): TokenPayload {
        val token = UUID.randomUUID().toString();
        return TokenPayload(payload.email, token)
    }

    override fun generateEvents(result: TokenPayload): Events {
        return listOf(AskPasswordResetEvent(HasEmailPayload(result.email)))
    }

}


class UpdateUserCommand(override val payload: User) : Command<User, User> {
    override suspend fun run(): User {
        val email = payload.email
        val originalUser = queryUserByEmail(email) ?: throw NotFoundException("User $email not found")
        // setting password into the DTO that will be the EventPayload
        return payload

    }

    override fun validate(): ValidationResult {
        // There should not be the password in it !
        return ValidationResult()
                .check(payload.email.isNotEmpty(), "Email is empty")
    }

    override fun generateEvents(result: User): Events {
        return listOf(UserUpdatedEvent(result))
    }

}


class RemoveUserCommand(override val payload: HasEmailPayload) : ThrowableCommand<HasEmailPayload>(payload) {
    override fun runUnit() {
        val email = payload.email
        queryUserByEmail(email) ?: throw NikotorValidationException("email $email does not exist")
    }

    override fun validate(): ValidationResult {
        return ValidationResult()
    }

    override fun generateEvent(): Event<*> {
        return UserRemovedEvent(payload)
    }

}























