package io.robusta.nikotor.user

import io.robusta.nikotor.user.User

public val usersDatabase = mutableMapOf<String, User>()

fun getDatabase(): MutableMap<String, User> {
    return usersDatabase
}
