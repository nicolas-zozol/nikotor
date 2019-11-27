package io.robusta.nikotor

import io.robusta.nikotor.Command
import io.robusta.nikotor.NikotorEngine
import io.robusta.nikotor.NikotorEvent
import io.robusta.nikotor.NikotorSubscriber
import java.util.concurrent.CompletableFuture

class NikotorEngineImpl(
    eventStore: EventStore,
    projectionsUpdaters: List<ProjectionUpdater> = emptyList()
) : NikotorEngine {
    override fun <Payload, EventPayload> process(command: Command<Payload>): CompletableFuture<NikotorEvent<EventPayload>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe(newSubscriber: NikotorSubscriber): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe(oldSubscriber: NikotorSubscriber): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
