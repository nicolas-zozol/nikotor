package io.robusta.nikotor.user


import io.robusta.nikotor.core.PayloadId
import io.robusta.nikotor.core.SimpleEvent
import java.util.*




typealias UserId = PayloadId

class PasswordUpdatedPayload(val password:String, val id:String)
class UserUpdatedPayload(val newUser:User, val previous:User, by:UserId, date:Date)
data class TokenPayload (override val email: String,val token: String):HasEmail{}
data class HasEmailPayload (override val email: String):HasEmail{}
/**
 * Hashed password !!!! it's up to the rest controller to hash it correctly
 */
data class HashedPasswordPayload(override val email : String,val password:String):HasEmail


class UserRegisteredEvent(payload: User) : SimpleEvent<User>(payload)
class UserActivatedEvent(payload: TokenPayload) : SimpleEvent<TokenPayload>(payload)
class PasswordUpdatedEvent(payload: HashedPasswordPayload) : SimpleEvent<HashedPasswordPayload>(payload)
class AskPasswordResetEvent(payload: HasEmailPayload) : SimpleEvent<HasEmailPayload>(payload)
class UserUpdatedEvent(payload: User) : SimpleEvent<User>(payload)
class UserRemovedEvent(payload: HasEmailPayload) : SimpleEvent<HasEmailPayload>(payload)
