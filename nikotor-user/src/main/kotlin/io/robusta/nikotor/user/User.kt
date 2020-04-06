package io.robusta.nikotor.user

import io.robusta.nikotor.data.HasId
import io.robusta.nikotor.data.SimpleEntity
import java.time.Instant
import java.util.*
import org.mindrot.jbcrypt.BCrypt
import java.security.MessageDigest


interface HasEmail {
    val email: String
}

interface HasPassword {
    val password: String
}

interface HasToken {
    val token: String
}

interface HasUser {
    val user: User
}

// Don't use this. Use concept of Decorator, ie Modification
interface Auditable {

}

abstract class AbstractAuditingEntity : SimpleEntity(), Auditable, HasId {

    var createdBy: String? = null
    var createdDate = Instant.now()
    var lastModifiedBy: String? = null
    var lastModifiedDate = Instant.now()
}


/**
 * User email is both database key and login
 */
class User(override val email: String) : AbstractAuditingEntity(), HasEmail {

    override val id: String
        get() {
            return this.email
        }

    var firstName: String? = null

    var lastName: String? = null

    var activated = false

    var langKey: String? = null

    var imageUrl: String? = null

    var activationKey: String? = null

    var resetKey: String? = null

    var resetDate: Instant? = null

    var authorities: Set<Authority> = HashSet<Authority>()

    override fun equals(other: Any?): Boolean {

        return when {
            this === other -> true
            other !is User -> false
            else -> email == other.email
        }
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

    override fun toString(): String {
        return email
    }


}

class ActivationTokenRecord(val user: User) : HasId {
    override val id: String
        get() = user.email
    val token: String = UUID.randomUUID().toString()

}

class Account(override val email: String, val hashedPassword: HashedPassword) : HasEmail, HasId {
    override val id: String
        get() = email
}

class Visit(override val email: String, val date: Date) : HasEmail, HasId {
    override val id = createRandomId()
}

data class Authority(val name: String)




fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt(12))
}

object HashUtils {
    fun sha512(input: String) = hashString("SHA-512", input)

    fun sha256(input: String) = hashString("SHA-256", input)

    fun sha1(input: String) = hashString("SHA-1", input)

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