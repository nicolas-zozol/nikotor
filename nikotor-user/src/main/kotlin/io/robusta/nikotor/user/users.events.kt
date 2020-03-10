package io.robusta.nikotor.user


import io.robusta.nikotor.core.SimpleEvent
import io.robusta.nikotor.data.HasId
import java.util.*



class PayloadId(override val id: String): HasId

typealias Token = String
typealias UserId = PayloadId

fun createRandomId(): Token {
    return UUID.randomUUID().toString()
}

fun createRandomToken(): Token {
    return UUID.randomUUID().toString()
}

data class RegisterPayload(val user: User, val password: String)
data class RegisterEventPayload(val user: User, val token: Token)
data class WithPasswordPayload(override val password: String, override val email: String,
                               override val id: String) : HasPassword, HasEmail, HasId
data class UserUpdatedPayload(val newUser: User, val previous: User, val by: UserId, val date: Date)
data class TokenPayload(override val email: String, override val token: String) : HasEmail, HasToken
data class HasEmailPayload(override val email: String) : HasEmail {}
/**
 * Hashed password !!!! it's up to the rest controller to hash it correctly
 */
data class HashedPasswordPayload(override val email: String, val password: String) : HasEmail
data class LoginHashPayload(override val email: String, val hash: String) : HasEmail

class UserRegisteredEvent(payload: RegisterEventPayload) : SimpleEvent<RegisterEventPayload>(payload)
class UserActivatedEvent(payload: TokenPayload) : SimpleEvent<TokenPayload>(payload)
class LoginEvent(payload: LoginHashPayload) : SimpleEvent<LoginHashPayload>(payload)
class FailedLoginEvent(payload: HasEmail) : SimpleEvent<HasEmail>(payload)
class CompletedLoggedEvent(payload: HasEmail) : SimpleEvent<HasEmail>(payload)
class PasswordUpdatedEvent(payload: HashedPasswordPayload) : SimpleEvent<HashedPasswordPayload>(payload)
class AskPasswordResetEvent(payload: HasEmailPayload) : SimpleEvent<HasEmailPayload>(payload)
class UserUpdatedEvent(payload: User) : SimpleEvent<User>(payload)
class UserRemovedEvent(payload: HasEmailPayload) : SimpleEvent<HasEmailPayload>(payload)

