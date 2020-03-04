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

/**
 * Associated DAO
 */
interface UserDao {

    suspend fun findByEmail(email: String): User? {
        return usersDatabase.find(email)
    }

}

class UserDatasetDao : UserDao {}

