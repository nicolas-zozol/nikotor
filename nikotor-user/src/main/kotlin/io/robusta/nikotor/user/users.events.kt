package io.robusta.nikotor.user


import io.robusta.nikotor.core.SimpleEvent
import java.time.Instant


object UserEvents {
    const val USER_REGISTERED = "USER_REGISTERED"
    const val USER_ACTIVATED = "USER_ACTIVATED"
    const val PASSWORD_UPDATED = "PASSWORD_UPDATED"
    const val ASK_PASSWORD_RESET = "ASK_PASSWORD_RESET"
    const val USER_UPDATED = "USER_UPDATED"
    const val USER_REMOVED = "USER_REMOVED"
}


class UserRegisteredEvent(payload: User) : SimpleEvent<User>(payload) {
    override val technicalDate = Instant.now().toEpochMilli()
    override val type: String
        get() = UserEvents.USER_REGISTERED
}
