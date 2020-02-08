package io.robusta.nikotor.user

import io.robusta.nikotor.core.Event
import io.robusta.nikotor.core.PersistedEvent
import io.robusta.nikotor.PersistedNikEvent
import java.time.Instant


object UserEvents {
    const val USER_REGISTERED = "USER_REGISTERED"
    const val USER_ACTIVATED = "USER_ACTIVATED"
    const val PASSWORD_UPDATED = "PASSWORD_UPDATED"
    const val ASK_PASSWORD_RESET = "ASK_PASSWORD_RESET"
    const val USER_UPDATED = "USER_UPDATED"
    const val USER_REMOVED = "USER_REMOVED"
}


open class UserRegisteredEvent(override val payload: User) : Event<User> {
    override val technicalDate = Instant.now().toEpochMilli()
    override val type: String
        get() = UserEvents.USER_REGISTERED
}

val john = User("john@doe.com")
data class PersistedUserRegisteredEvent(override val payload: User, override val sequenceId: Long):UserRegisteredEvent(payload),
    PersistedEvent<User>
