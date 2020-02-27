package io.robusta.nikotor.user

import io.robusta.nikotor.*
import io.robusta.nikotor.core.*
import java.util.*


data class RegisterUserCommand(override val payload: User) : ThrowableCommand<User>( payload) {


    override fun validate(): ValidationResult {
        val user = payload

        return ValidationResult()
                .check(user.email.isNotEmpty(), "email is not valid")
                .check(!user.password.isNullOrEmpty(), "password is not set")
    }

    override fun runUnit() {
        val email = payload.email
        queryUserByEmail(email).get() ?: throw IllegalStateException("email $email is already used")

        // if event succeed, then run command SendActivationMailCommand
    }


    override fun generateEvent(): Event<*> {
        return UserRegisteredEvent(payload)
    }

}



class ActivateUserCommand(override val payload: TokenPayload) : ThrowableCommand<TokenPayload>( payload) {
    override fun validate(): ValidationResult {
        return ValidationResult().check( payload.email.isNotEmpty(), "Email is empty")
    }

    override fun runUnit() {
        val email = payload.email
        val user = queryUserByEmail(email).get() ?: throw NikotorValidationException("email $email does not exist")

        ValidationResult()
            .check(!user.activated,"User $email is already activated" )
            .check(user.activationKey == payload.token,"Activation key ${payload.token} is wrong for user $email" )
            .throwIfInvalid()

    }

    override fun generateEvent(): Event<*> {
        return UserActivatedEvent(payload)
    }
}


class ChangePasswordCommand(override val payload: HashedPasswordPayload): ThrowableCommand<HashedPasswordPayload>(payload){
    override fun validate(): ValidationResult {
        return ValidationResult().check(
                payload.password.isNotEmpty(), "Password for ${payload.email} is empty")
    }

    override fun runUnit() {
        val email = payload.email
        val password = payload.password
        val oldPassword = queryHashedPassword(email).get()

        ValidationResult()
            .check(password!=oldPassword, "Password is the same")
            .check(password.isNotEmpty(), "Password for ${payload.email} is empty")
            .throwIfInvalid()

    }

    override fun generateEvent(): Event<*> {
        return PasswordUpdatedEvent(payload)
    }

}



class AskPasswordResetCommand(override val payload: HasEmailPayload):
    Command<HasEmail, TokenPayload> {

    override fun validate(): ValidationResult {
        return ValidationResult().check( payload.email.isNotEmpty(), "Email is empty")
    }

    override fun run():Await<TokenPayload>{
        val token = UUID.randomUUID().toString();
        return awaitNow(TokenPayload(payload.email, token))
    }

    override fun generateEvent(result: TokenPayload): Event<*> {
        return AskPasswordResetEvent(HasEmailPayload(result.email))
    }

}


class UpdateUserCommand(override val payload: User): Command<User, User> {
    override fun run():Await<User> {
        val email = payload.email
        val originalUser = queryUserByEmail(email).get() ?:throw NotFoundException("User $email not found")
        // setting password into the DTO that will be the EventPayload
        payload.password=originalUser.password
        return awaitNow(payload)

    }

    override fun validate(): ValidationResult {
        // There should not be the password in it !
        return ValidationResult()
            .check( payload.email.isNotEmpty(), "Email is empty")
            .check( payload.password?.isEmpty() ?: true, "Password should be empty")
    }

    override fun generateEvent(result: User): Event<*> {
        check(!result.password.isNullOrEmpty())
        return UserUpdatedEvent(result)
    }

}


class RemoveUserCommand(override val payload: HasEmailPayload): ThrowableCommand<HasEmailPayload>(payload){
    override fun runUnit() {
        val email = payload.email
        queryUserByEmail(email).get() ?: throw NikotorValidationException("email $email does not exist")
    }

    override fun validate(): ValidationResult {
        return ValidationResult()
    }

    override fun generateEvent(): Event<*> {
        return UserRemovedEvent(payload)
    }

}























