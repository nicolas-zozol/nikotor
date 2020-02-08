package io.robusta.nikotor



import io.robusta.nikotor.core.*
import java.util.concurrent.CompletableFuture

class InMemoryEventStore : EventStore {
    val events: MutableList<Persisted> = ArrayList()

    override fun <P> persist(event: Event<P>): CompletableFuture<Persisted> {

        val persistedEvent = SequencePersisted(event)
        events.add(persistedEvent)
        return CompletableFuture.completedFuture(persistedEvent)

    }

    override fun persistAll(events: Events): CompletableFuture<PersistedEvents> {
        val list = events.map { e -> persist(e) }
        return CompletableFuture.allOf(*list.toTypedArray())
            .thenApply { list.map { future -> future.get() } }

    }


    fun getAllEventsStartingFromIndex(originIndex: Int = 0): CompletableFuture<PersistedEvents> {
        return CompletableFuture.completedFuture(this.events.subList(originIndex, this.events.size))
    }

    override fun loadInitialEvents(): CompletableFuture<PersistedEvents> {
        return this.getAllEventsStartingFromIndex(0)
    }

    override fun resetWith(events: Events): CompletableFuture<PersistedEvents> {
        this.events.clear()
        return this.persistAll(events)
    }
}
