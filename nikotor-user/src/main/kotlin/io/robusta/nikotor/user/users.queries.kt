package io.robusta.nikotor.user

import io.robusta.nikotor.Await
import io.robusta.nikotor.awaitNow


fun queryUserByEmail(email:String): Await<User?>{
    return  awaitNow(usersDatabase.find(email))
}

fun queryHashedPassword(email:String): Await<String?>{
    return  awaitNow(usersDatabase.find(email)).thenApply{it?.password}
}

