package io.robusta.nikotor.user

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.CompletableFuture

val dao = UserDatasetDao()

fun queryUserByEmail(email: String): User? {
    return runBlocking {
        dao.findByEmail(email)
    }
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
 * Associated DAO with default implementation using in memory dataset
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

    suspend fun queryLoginAttempt(email: String, hashAttempt: String): User? {
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

}

class UserDatasetDao : UserDao {}

class UserDaoAsync(private val dao: UserDao) {

    fun findByEmail(email: String): CompletableFuture<Optional<User>> {
        return GlobalScope.future {
            Optional.ofNullable(dao.findByEmail(email))
        }
    }

    fun findActivationToken(email: String): CompletableFuture<Optional<String>> {
        return GlobalScope.future {
            Optional.ofNullable(dao.findActivationToken(email))
        }
    }

    fun checkPassword(email: String, hashedPassword: String): CompletableFuture<Boolean> {
        return GlobalScope.future {
            dao.checkPassword(email, hashedPassword)
        }
    }

    fun queryLoginAttempt(email: String, hashAttempt: String): CompletableFuture<Optional<User>> {
        return GlobalScope.future {
            Optional.ofNullable(dao.queryLoginAttempt(email, hashAttempt));
        }
    }

}
