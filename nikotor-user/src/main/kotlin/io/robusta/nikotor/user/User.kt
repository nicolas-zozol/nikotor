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
open class User(override val email: String) : AbstractAuditingEntity(), HasEmail {

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

class ActivationTokenRecord(override val user: User) : HasId, HasUser {
    override val id: String
        get() = user.email
    val token: String = UUID.randomUUID().toString()

}

internal class Account(override val user: User, val hashedPassword: HashedPassword) : HasUser, HasId {
    override val id: String
        get() = user.email
}

internal class Visit(override val user: User, val date: Date) : HasUser, HasId {
    override val id = createRandomId()
}

data class Authority(val name: String)

