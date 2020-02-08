package io.robusta.nikotor.user

import java.time.Instant
import java.util.*

interface HasEmail{
    val email:String
}

// Don't use this. Use concept of Decorator, ie Modification
interface Auditable{

}

abstract class AbstractAuditingEntity:Auditable {

    var createdBy: String? = null
    var createdDate = Instant.now()
    var lastModifiedBy: String? = null
    var lastModifiedDate = Instant.now()
}


/**
 * User email is both database key and login
 */
class User(override val email: String) : AbstractAuditingEntity(), HasEmail {


    var password: String? = null
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


data class Authority(val name: String)
