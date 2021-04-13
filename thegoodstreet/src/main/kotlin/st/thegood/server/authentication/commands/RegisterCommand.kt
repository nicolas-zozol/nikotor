package st.thegood.server.authentication.commands

import io.robusta.nikotor.core.Event
import io.robusta.nikotor.core.RunnableCommand
import io.robusta.nikotor.core.SimpleEvent
import io.robusta.nikotor.core.ValidationResult


data class RegisterPayload(val email:String)

enum class RegisterResult(result: Boolean){
    EMAIL_REGISTERED(false),
    OK(true)
}

data class RegisterEvent(override val payload:RegisterPayload):SimpleEvent<RegisterPayload>(payload){}

data class RegisterUserCommand(override val payload: RegisterPayload) : RunnableCommand<RegisterPayload, RegisterResult>(payload) {
    override fun validate(): ValidationResult {
        return ValidationResult()
    }

    override suspend fun run(): RegisterResult {
        // TODO: check that the email does not exist
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun generateEvent(result: RegisterResult): Event<*> {
        return
    }


}