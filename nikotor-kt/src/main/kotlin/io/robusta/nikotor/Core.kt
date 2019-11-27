package io.robusta.nikotor

import java.util.concurrent.CompletableFuture

interface Command<out Payload> {
    val type: String;
    val payload: Payload;
    fun validate(): CompletableFuture<Boolean>;
    fun <EventPayload> generateEvent(): NikotorEvent<EventPayload>;
}

interface RunnableCommand<Payload, CommandResult> : Command<Payload> {
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
    fun getCommandResult(): CommandResult;

}

interface NikotorEvent<out EventPayload> {
    val type: String;
    val technicalDate: Long; // the date when the event was really created
    val payload: EventPayload; // the business information related to the event
}

data class NikEvent<out P>(override val type: String, override val technicalDate: Long, override val payload: P) :
    NikotorEvent<P> {

}

data class PersistedNikEvent<P>(
    override val sequenceId: Long,
    override val type: String,
    override val technicalDate: Long,
    override val payload: P
) : PersistedEvent<P> {




}

interface PersistedEvent<EventPayload> : NikotorEvent<EventPayload> {
    val sequenceId: Long;
}

typealias Events = List<NikotorEvent<*>>
typealias PersistedEvents = List<PersistedEvent<*>>

interface EventStore {
    fun <EventPayload> add(event: NikotorEvent<EventPayload>): CompletableFuture<PersistedEvent<EventPayload>>;

    fun addAll(events: Events): CompletableFuture<PersistedEvents>;

    fun loadInitialEvents(): CompletableFuture<PersistedEvents>;

    fun resetWith(events: Events): CompletableFuture<PersistedEvents>;
}

interface ProjectionUpdater {
    fun <EventPayload> updateWithEvent(event: PersistedEvent<EventPayload>): CompletableFuture<Void>;
}

interface NikotorSubscriber {
    val id: Long;
    fun <EventPayload> reactTo(event: NikotorEvent<EventPayload>): Void;
}

interface NikotorEngine {

    fun <Payload, EventPayload> process(command: Command<Payload>): CompletableFuture<NikotorEvent<EventPayload>>;
    fun subscribe(newSubscriber: NikotorSubscriber): Void;
    fun unsubscribe(oldSubscriber: NikotorSubscriber): Void;

}
