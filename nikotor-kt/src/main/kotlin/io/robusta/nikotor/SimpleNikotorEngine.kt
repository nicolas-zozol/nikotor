package io.robusta.nikotor

import java.lang.Exception
import java.util.concurrent.CompletableFuture

/**
 * Very simple engine with InMemoryEventStore and no subscription
 */
class SimpleNikotorEngine(

    projectionsUpdaters: List<ProjectionUpdater> = emptyList()
) : NikotorEngine {
    private val eventStore = InMemoryEventStore();

    override fun <Payload, EventPayload, CommandResult> process(command: Command<Payload, CommandResult>): CompletableFuture<PersistedEvent<EventPayload>> {

        val validation = command.validate();
        if (!validation.result) {
            throw NikotorException(validation.reasons.toString(), 400, ErrorTypes.VALIDATION_ERROR, validation.reasons);
        }
        val result: CommandResult;
        try {
            result = command.run().get();
            val event = command.generateEvent<EventPayload>(result);
            val persistedEvent = eventStore.persist(event);
            return persistedEvent;
        } catch (nikException: NikotorException) {
            // detected error, just rethrow
            throw nikException;
        } catch (e: Exception) {
            throw NikotorException(e.message.orEmpty(), 500, ErrorTypes.COMMAND_ERROR, e.message.orEmpty());
        }
    }


    override fun subscribe(newSubscriber: NikotorSubscriber): Void {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe(oldSubscriber: NikotorSubscriber): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}