package io.robusta.nikotor

import io.robusta.nikotor.core.*
import java.lang.Exception
import java.util.concurrent.CompletableFuture

/**
 * Very simple engine with InMemoryEventStore and no subscription
 */
class SimpleNikotorEngine(
    override val eventStore: EventStore = InMemoryEventStore(),
    private val projectionsUpdaters: List<ProjectionUpdater> = emptyList()
) : NikotorEngine {

    override fun <Payload, CommandResult> process(command: Command<Payload, CommandResult>): CompletableFuture<PersistedEvent<*, *>> {

        command.validate().throwIfInvalid()

        val result: CommandResult?
        try {
            result = command.run().get()
            val event = command.generateEvent(result)
            // todo: problem with a catch
            val future = eventStore.persist(event)
            future.thenAccept { update(it) }
            return future
        } catch (nikException: NikotorException) {
            // detected error, just rethrow
            throw nikException
        } catch (e: Exception) {
            throw NikotorException(e.message.orEmpty(), 500, ErrorTypes.COMMAND_ERROR, e.message.orEmpty())
        }
    }

    private fun update(persistedEvent: PersistedEvent<*, *>){
        projectionsUpdaters.forEach { it.updateWithEvent(persistedEvent.event) }
    }


    override fun subscribe(newSubscriber: NikotorSubscriber): Void {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe(oldSubscriber: NikotorSubscriber): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
