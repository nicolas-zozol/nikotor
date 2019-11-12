package io.robusta.nikotor

import java.util.concurrent.CompletableFuture

interface Command<Payload> {
    val type: String;
    val payload: Payload;
    fun validate(): CompletableFuture<Boolean>;
    fun <EventPayload>generateEvent():NikotorEvent<EventPayload>;
}

interface RunnableCommand<Payload, CommandResult>: Command<Payload>{
    /**
     * Warning: should not modify the projections !
     * Side effects are http request to external system, write in files....
     * It can also decide if projection state is suitable for executing command in nominal case.
     * For exemple the result of `run()` can be `true` or `InsufficiantStock`
     */
    fun run(): CompletableFuture<CommandResult>

    /**
     * The generateEvent() function will use the CommandResult to decide which event
     * will be generated.
     */
    fun getCommandResult() : CommandResult;

}

interface NikotorEvent<EventPayload>{
    val type: String;
    val technicalDate: Long; // the date when the event was really created
    val payload: EventPayload; // the business information related to the event
}


interface PersistedEvent<EventPayload> : NikotorEvent<EventPayload> {
    val sequenceId: Long;
}

interface NikotorSubscriber{
    val id: Long;
    fun <EventPayload>reactTo(event: NikotorEvent<EventPayload>): Void;
}

interface NikotorEngine{

    fun <Payload, EventPayload>process(command: Command<Payload>): CompletableFuture<NikotorEvent<EventPayload>>;
    fun subscribe(newSubscriber: NikotorSubscriber): Void;
    fun unsubscribe(oldSubscriber: NikotorSubscriber): Void;

}
