package io.robusta.nikotor.user

import io.robusta.nikotor.*
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


    override fun generateEvent(): NikotorEvent<*> {
        return SimpleEvent(UserEvents.USER_REGISTERED, payload)
    }

}

data class TokenPayload (val email: String,val token: String){}


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

    override fun generateEvent(): NikotorEvent<*> {
        return SimpleEvent(UserEvents.USER_ACTIVATED, payload)
    }
}

interface PasswordPayload{
    val email : String
    /**
     * Hashed password !!!!
     */
    val password:String
}
class ChangePasswordCommand(override val payload: PasswordPayload): ThrowableCommand<PasswordPayload>(payload){
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

    override fun generateEvent(): NikotorEvent<*> {
        return SimpleEvent(UserEvents.PASSWORD_UPDATED, payload)
    }

}

interface EmailPayload{
    val email:String
}

class AskPasswordResetCommand(override val payload: EmailPayload): Command<EmailPayload, TokenPayload>{

    override fun validate(): ValidationResult {
        return ValidationResult().check( payload.email.isNotEmpty(), "Email is empty")
    }

    override fun run():Await<TokenPayload>{
        val token = UUID.randomUUID().toString();
        return awaitNow(TokenPayload(payload.email, token))
    }

    override fun generateEvent(result: TokenPayload): NikotorEvent<*> {
        return SimpleEvent(UserEvents.ASK_PASSWORD_RESET, result)
    }

}



























