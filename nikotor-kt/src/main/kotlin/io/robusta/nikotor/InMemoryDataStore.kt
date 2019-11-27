package io.robusta.nikotor

import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class InMemoryEventStore : EventStore {
    val events: MutableList<PersistedNikEvent<*>> = ArrayList();

    override fun <P> add(event: NikotorEvent<P>): CompletableFuture<PersistedEvent<P>> {

        val persistedEvent = PersistedNikEvent((events.size+1).toLong(), event.type, event.technicalDate, event.payload);
        events.add(persistedEvent);
        return CompletableFuture.completedFuture(persistedEvent);

    }

    override fun addAll(events: Events): CompletableFuture<PersistedEvents>
    {
        val list = events.map { e->add(e) };
        return CompletableFuture
            .allOf(*list.toTypedArray())
            .thenApply { list.map { future -> future.get() } };
    }




    fun getAllEventsStartingFromIndex(originIndex = 0): Promise<SamEvent[]>
    {
        return new Promise (resolve => {
        resolve(this.events);
    });
    }

    fun loadInitialEvents(): Promise<PersistedSamEvent[]>
    {
        const startsEvents =[
        ];
        const promises : Array < Promise < PersistedSamEvent > > =[];

        for (const event of startsEvents) {
        promises.push(this.add(event));
    }
        console.log("Loaded in memory all saved events : " + startsEvents.length);
        return Promise.all(promises);
    }

    fun resetWith(events: SamEvent[]): Promise<PersistedSamEvent[]>
    {
        this.events = [];
        return this.addAll(events);
    }
}
