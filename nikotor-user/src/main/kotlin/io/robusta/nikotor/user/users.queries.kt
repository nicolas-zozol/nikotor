package io.robusta.nikotor.user

import io.robusta.nikotor.Try
import io.robusta.nikotor.futureNow


fun queryUserByEmail(email:String): Try<User?>{
    return  futureNow(usersDatabase[email])
}

