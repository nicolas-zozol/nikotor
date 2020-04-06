package io.robusta.nikotor.user

import io.robusta.nikotor.data.DataSet
import io.robusta.nikotor.data.HasId
import java.time.ZoneOffset
import java.time.ZonedDateTime




data class LoginAttempt(val email: String, val token: String) : HasId {
    override val id: String
        get() = token
    val hour = currentHour()


}



// TODO: count minutes since 2000. Create a config entry in the bundle. Calculate a time difference.
// Not done now, because not sure at all if this method will stands.
fun currentHour(): Int {
    val now = ZonedDateTime.now(ZoneOffset.UTC)
    return now.hour
}
