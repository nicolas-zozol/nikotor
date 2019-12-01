package io.robusta.nikotor.user

import io.robusta.nikotor.Await
import io.robusta.nikotor.awaitNow


fun queryUserByEmail(email:String): Await<User?>{
    return  awaitNow(usersDatabase[email])
}

fun queryHashedPassword(email:String): Await<String?>{
    return  awaitNow(usersDatabase[email]).thenApply{it?.password}
}

