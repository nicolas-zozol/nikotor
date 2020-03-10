package io.robusta.nikotor.user

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

val dao = UserDatasetDao()

fun queryUserByEmail(email: String): User? {
    return runBlocking {
        dao.findByEmail(email)
    }
}

fun queryLoginAttempt(email: String, hashAttempt: String): User? {
    val attempt = loginAttempts.find(hashAttempt) ?: return null

    if (currentHour() - attempt.hour > 1) {
        // TODO: log time problem
        throw IllegalStateException("Delay has expired")
    }

    if (email != attempt.email) {
        throw IllegalArgumentException("Email for the attempt is different from the attempts saved with the same hash")
    }

    return queryUserByEmail(email)
}


fun privateQueryCheckPassword(email: String, hashedPassword: String): Boolean {
    return runBlocking {
        dao.checkPassword(email, hashedPassword)
    }
}


fun queryActivationTokenByEmail(email: String): String? {
    return runBlocking {
        dao.findActivationToken(email)
    }
}


/**
 * Associated DAO
 */
interface UserDao {

    suspend fun findByEmail(email: String): User? {
        return usersDatabase.find(email)
    }

    suspend fun findActivationToken(email: String): String? {
        return activationTokenDatabase.find(email)?.token
    }

    suspend fun checkPassword(email: String, hashedPassword: String): Boolean {
        return accountSet.find(email)?.hashedPassword == hashedPassword
    }

}

class UserDatasetDao : UserDao {}

