package io.robusta.nikotor

import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @result: true if validation succeed
 * @reasons: Map of key / errors on the key
 */
class ValidationResult(val result:Boolean, val reasons:Map<String, String>){

}

interface Command<out Payload, CommandResult> {
    val type: String;
    val payload: Payload;
    /**
     * Used for surface validation, mostly checking the values of the command Payload.
     * Use the `run()` function to ensure the state of application is compatible with
     * your command
     */
    fun validate(): ValidationResult;
    fun <EventPayload> generateEvent(result:CommandResult): NikotorEvent<EventPayload>;
    /**
     * Warning: should not modify the projections !
     * Side effects are http request to external system, write in files....
     * It can also decide if projection state is suitable for executing command in nominal case.
     * For example the result of `run()` can be `true` or false, or throw a
     * `InsufficientStockException` that will be catch by the engine.
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

data class NikEvent<out P>(override val type: String, override val payload: P) :NikotorEvent<P>{
    override val technicalDate = Date().time;
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
    fun <EventPayload> persist(event: NikotorEvent<EventPayload>): CompletableFuture<PersistedEvent<EventPayload>>;

    fun persistAll(events: Events): CompletableFuture<PersistedEvents>;

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

    fun <Payload, EventPayload, CommandResult> process(command: Command<Payload, CommandResult>): CompletableFuture<PersistedEvent<EventPayload>>;
    fun subscribe(newSubscriber: NikotorSubscriber): Void;
    fun unsubscribe(oldSubscriber: NikotorSubscriber): Void;

}
