package io.robusta.nikotor

import java.util.concurrent.CompletableFuture

class InMemoryEventStore : EventStore {
    val events: MutableList<PersistedNikEvent<*>> = ArrayList()

    override fun <P> add(event: NikotorEvent<P>): CompletableFuture<PersistedEvent<P>> {

        val persistedEvent =
            PersistedNikEvent((events.size + 1).toLong(), event.type, event.technicalDate, event.payload)
        events.add(persistedEvent)
        return CompletableFuture.completedFuture(persistedEvent)

    }

    override fun addAll(events: Events): CompletableFuture<PersistedEvents> {
        val list = events.map { e -> add(e) }
        return CompletableFuture
            .allOf(*list.toTypedArray())
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
        return this.addAll(events)
    }
}
