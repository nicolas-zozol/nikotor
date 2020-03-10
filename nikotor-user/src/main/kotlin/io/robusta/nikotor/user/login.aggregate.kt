package io.robusta.nikotor.user

import io.robusta.nikotor.data.DataSet
import io.robusta.nikotor.data.HasId
import java.time.ZoneOffset
import java.time.ZonedDateTime



data class LoginAttemptPayload(val email: String, val hashAttempt: String)

data class LoginAttempt(val email: String, val hashAttempt: String) : HasId {
    override val id: String
        get() = hashAttempt
    val hour = currentHour()


}

val loginAttempts = DataSet<LoginAttempt>()

// TODO: count minutes since 2000. Create a config entry in the bundle. Calculate a time difference.
// Not done now, because not sure at all if this method will stands.
fun currentHour(): Int {
    val now = ZonedDateTime.now(ZoneOffset.UTC)
    return now.hour
}
