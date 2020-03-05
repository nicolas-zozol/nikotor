package io.robusta.nikotor.user

import kotlinx.coroutines.runBlocking

val dao = UserDatasetDao()

fun queryUserByEmail(email: String): User? {
    return runBlocking {
        dao.findByEmail(email)
    }
}

fun queryHashedPassword(email: String): String? {
    return runBlocking {
        dao.findByEmail(email)?.password
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

}

class UserDatasetDao : UserDao {}

