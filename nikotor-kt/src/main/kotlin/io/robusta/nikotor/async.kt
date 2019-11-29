package io.robusta.nikotor

import java.util.concurrent.CompletableFuture

typealias Try<T> = CompletableFuture<T>
val futureUnit = CompletableFuture.completedFuture( Unit)!!

fun <T>futureNow(any:T):Try<T>{
    return CompletableFuture.completedFuture(any)
}
