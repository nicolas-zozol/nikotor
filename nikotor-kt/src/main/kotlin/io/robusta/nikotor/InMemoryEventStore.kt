package io.robusta.nikotor


import io.robusta.nikotor.core.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CompletableFuture

class InMemoryEventStore : EventStore {
    val events: MutableList<Persisted> = ArrayList()

    override suspend fun <P> persist(event: Event<P>): Persisted {

        val persistedEvent = SequencePersisted(event)
        events.add(persistedEvent)
        return persistedEvent
    }

    fun getAllEventsStartingFromIndex(originIndex: Int = 0): PersistedEvents {
        return this.events.subList(originIndex, this.events.size)
    }

    override suspend fun loadInitialEvents(): PersistedEvents {
        return this.getAllEventsStartingFromIndex(0)
    }

    override suspend fun resetWith(events: Events): PersistedEvents {
        this.events.clear()
        return this.persistAll(events)
    }
}
