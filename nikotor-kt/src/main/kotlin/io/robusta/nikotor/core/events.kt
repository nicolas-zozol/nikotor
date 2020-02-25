package io.robusta.nikotor.core

import java.util.*
import java.util.concurrent.CompletableFuture

interface PersistedEvent<E, P> where E : Event<P> {
    val event: E
    val sequenceId: Long
    val technicalDate: Long
}


typealias Persisted = PersistedEvent<*,*>
typealias Events = List<Event<*>>
typealias PersistedEvents = List<PersistedEvent<*,*>>

abstract class AbstractPersistedEvent<E, P>(override val event: E)
    : PersistedEvent<E, P> where E : Event<P> {
    override val technicalDate = Date().time
}

object LocalSequence{
    private var localSequenceId = 0L
    fun next(): Long {
        localSequenceId++
        return localSequenceId
    }
}


class SequencePersisted<E, P>(event: E) : AbstractPersistedEvent<E, P>(event) where E : Event<P> {
    override val sequenceId = LocalSequence.next()
}


interface EventStore {
    fun <P> persist(event: Event<P>): CompletableFuture<PersistedEvent<*,*>>

    fun persistAll(events: Events): CompletableFuture<PersistedEvents>

    fun loadInitialEvents(): CompletableFuture<PersistedEvents>

    fun resetWith(events: Events): CompletableFuture<PersistedEvents>
}

