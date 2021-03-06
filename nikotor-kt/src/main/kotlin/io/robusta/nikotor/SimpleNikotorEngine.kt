package io.robusta.nikotor

import io.robusta.nikotor.core.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.concurrent.CompletableFuture

/**
 * Very simple engine with InMemoryEventStore and no subscription
 */
class SimpleNikotorEngine(
    override val eventStore: EventStore = InMemoryEventStore(),
    private val projectionsUpdaters: List<ProjectionUpdater> = emptyList()
) : NikotorEngine {

    override suspend fun <Payload, CommandResult> process(command: Command<Payload, CommandResult>): PersistedEvent<*, *> {

        command.validate().throwIfInvalid()

        val result: CommandResult?
        try {
            result = command.run()
            val events = command.generateEvents(result)
            if(events.size>1){
                throw IllegalStateException("Multiple events not yet implemented in SimpleEngine")
            }
            val event = events[0]
            // todo: problem with a catch: check if ok with coroutines now
            val persistedEvent = eventStore.persist(event)
            updateProjections(persistedEvent)
            return persistedEvent
        } catch (nikException: NikotorException) {
            // detected error, just rethrow
            throw nikException
        } catch (e: Exception) {
            throw NikotorException(e.message.orEmpty(), 500, ErrorTypes.COMMAND_ERROR, e.message.orEmpty())
        }
    }

    private suspend fun updateProjections(persistedEvent: PersistedEvent<*, *>){
        projectionsUpdaters.forEach { it.updateWithEvent(persistedEvent.event) }
    }


    override fun subscribe(newSubscriber: NikotorSubscriber): Void {

        TODO("not implemented")
    }

    override fun unsubscribe(oldSubscriber: NikotorSubscriber): Void {
        TODO("not implemented")
    }

}
