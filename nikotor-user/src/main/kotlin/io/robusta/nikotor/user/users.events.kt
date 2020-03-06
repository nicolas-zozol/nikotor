package io.robusta.nikotor.user


import io.robusta.nikotor.core.PayloadId
import io.robusta.nikotor.core.SimpleEvent
import java.util.*


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
data class WithPasswordPayload(override val password: String, override val email: String):HasPassword, HasEmail
data class UserUpdatedPayload(val newUser: User, val previous: User, val by: UserId, val date: Date)
data class TokenPayload(override val email: String, override val token: String) : HasEmail, HasToken
data class HasEmailPayload(override val email: String) : HasEmail {}
/**
 * Hashed password !!!! it's up to the rest controller to hash it correctly
 */
data class HashedPasswordPayload(override val email: String, val password: String) : HasEmail


class UserRegisteredEvent(payload: RegisterEventPayload) : SimpleEvent<RegisterEventPayload>(payload)
class UserActivatedEvent(payload: TokenPayload) : SimpleEvent<TokenPayload>(payload)
class UserLoggedEvent(payload: HasEmail) : SimpleEvent<HasEmail>(payload)
class PasswordUpdatedEvent(payload: HashedPasswordPayload) : SimpleEvent<HashedPasswordPayload>(payload)
class AskPasswordResetEvent(payload: HasEmailPayload) : SimpleEvent<HasEmailPayload>(payload)
class UserUpdatedEvent(payload: User) : SimpleEvent<User>(payload)
class UserRemovedEvent(payload: HasEmailPayload) : SimpleEvent<HasEmailPayload>(payload)
