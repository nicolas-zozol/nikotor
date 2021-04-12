package io.robusta.nikotor.core

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*
import java.util.concurrent.CompletableFuture

interface PersistedEvent<out E, P> where E : Event<P> {
    val event: E
    val sequenceId: Long
    val technicalDate: Long
}


typealias Persisted = PersistedEvent<*, *>
typealias Events = List<Event<*>>
typealias PersistedEvents = List<PersistedEvent<*, *>>

abstract class AbstractPersistedEvent<E, P>(override val event: E)
    : PersistedEvent<E, P> where E : Event<P> {
    override val technicalDate = Date().time
}

/**
 * Todo: move into InMemoryEventStore, so that multiple sequences can be built with separated indexes
 * Singleton is once again a bad idea :)
 */
object LocalSequence {
    private var localSequenceId = 0L
    fun next(): Long {
        localSequenceId++
        return localSequenceId
    }

    fun reset() {
        localSequenceId = 0L
    }
}

/**
 * TODO: move to InMemoryEventStore logic
 */
class SequencePersisted<E, P>(event: E) : AbstractPersistedEvent<E, P>(event) where E : Event<P> {
    override val sequenceId = LocalSequence.next()
}


interface EventStore {
    suspend fun <P> persist(event: Event<P>): PersistedEvent<*, *>

    suspend fun persistAll(events: Events): PersistedEvents {

        return coroutineScope {
            events.map { e -> async { persist(e) } }.awaitAll()
        }
    }

    suspend fun loadInitialEvents(): PersistedEvents

    suspend fun resetWith(events: Events): PersistedEvents
}

