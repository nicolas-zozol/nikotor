package io.robusta.nikotor.user

import io.robusta.nikotor.data.HasId
import org.mindrot.jbcrypt.BCrypt
import java.security.MessageDigest
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

// same kind of object for Timer

open class Hasher {

    private fun hashBcryptPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(10))
    }

    fun hashPassword(password: String): String {
        return hashBcryptPassword(password)
    }

    fun checkPassword(candidatePassword: ClearPassword, storedPassword: HashedPassword): Boolean {
        return BCrypt.checkpw(candidatePassword, storedPassword)
    }

    private fun sha512(input: String) = hashString("SHA-512", input)

    private fun sha256(input: String) = hashString("SHA-256", input)

    private fun sha1(input: String) = hashString("SHA-1", input)

    /**
     * Supported algorithms on Android:
     *
     * Algorithm	Supported API Levels
     * MD5          1+
     * SHA-1	    1+
     * SHA-224	    1-8,22+
     * SHA-256	    1+
     * SHA-384	    1+
     * SHA-512	    1+
     */
    @Suppress("LocalVariableName")
    private fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }
}