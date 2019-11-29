package io.robusta.nikotor.user

import io.robusta.nikotor.*

interface UserPayload {
    val user: User
    val author: User
}


data class RegisterUserCommand(override val payload: User) : ThrowableCommand<User>( payload) {

    override fun runUnit() {
        val email = payload.email
        queryUserByEmail(email).get() ?: throw IllegalStateException("email $email is already used")
    }

    override fun validate(): ValidationResult {
        val user = payload

        return ValidationResult()
                .check("email", user.email.isNotEmpty(), "email is not valid")
                .check("password", !user.password.isNullOrEmpty(), "password is not set")
    }

    override fun generateEvent(): NikotorEvent<*> {
        return NikEvent(UserEvents.USER_REGISTERED, payload)
    }

}

interface ActivationPayload {
    val email: String
    val activationKey: String
}

class ActivateUserCommand(override val payload: ActivationPayload) : ThrowableCommand<ActivationPayload>( payload) {
    override fun validate(): ValidationResult {
        return ValidationResult().check("email", payload.email.isNotEmpty(), "Email is empty")
    }

    override fun runUnit() {
        val email = payload.email
        val user = queryUserByEmail(email).get() ?: throw NikotorValidationException("email $email is already used")
        !user.activated || throw NikotorValidationException("User $email is already activated")
        user.activationKey == payload.activationKey
                || throw NikotorValidationException("Activation key ${payload.activationKey} is wrong for user $email")


    }

    override fun generateEvent(): NikotorEvent<*> {
        return NikEvent(UserEvents.USER_ACTIVATED, payload)
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
                "password", payload.password.isNotEmpty(), "Password for ${payload.email} is empty")
    }

    override fun runUnit() {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun generateEvent(): NikotorEvent<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}





























