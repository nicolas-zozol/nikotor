package io.robusta.nikotor.user

import io.robusta.nikotor.PersistedEvent


object UserEvents {
    const val USER_REGISTERED = "USER_REGISTERED"
    const val USER_ACTIVATED = "USER_ACTIVATED"
    const val PASSWORD_UPDATED = "PASSWORD_UPDATED"
    const val ASK_PASSWORD_RESET = "ASK_PASSWORD_RESET"
    const val USER_UPDATED = "USER_UPDATED"
    const val USER_REMOVED = "USER_REMOVED"
}



data class UserRegisteredEvent(override val payload:User):Nikotor<User>{
    override val type: String
        get() = UserEvents.USER_REGISTERED
}
