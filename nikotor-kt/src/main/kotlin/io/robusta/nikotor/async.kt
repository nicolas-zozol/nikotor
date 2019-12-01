package io.robusta.nikotor

import java.util.concurrent.CompletableFuture

typealias Await<T> = CompletableFuture<T>
val awaitUnit = CompletableFuture.completedFuture( Unit)!!

fun <T>awaitNow(any:T):Await<T>{
    return CompletableFuture.completedFuture(any)
}
