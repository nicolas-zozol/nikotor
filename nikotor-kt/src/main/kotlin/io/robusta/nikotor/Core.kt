package io.robusta.nikotor

import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @result: true if validation succeed
 * @reasons: Map of key / errors on the key
 */
class ValidationResult(var result:Boolean=true){

    val reasons = mutableMapOf<String,String>()
    fun check(key:String, condition:Boolean, reason:String): ValidationResult{
        if (!condition){
            result= false
            reasons[key] = reason
        }
        return this
    }


}



interface Command<out Payload, CommandResult>{

    val type: String
    val payload: Payload
    /**
     * Used for surface validation, mostly checking the values of the command Payload.
     * Use the `run()` function to ensure the state of application is compatible with
     * your command
     */
    fun validate(): ValidationResult

    // TODO: Not happy at all with this * invariance
    fun  generateEvent(result:CommandResult?): NikotorEvent<*>
    /**
     * Warning: should not modify the projections !
     * Side effects are http request to external system, write in files....
     * It can also decide if projection state is suitable for executing command in nominal case.
     * For example the result of `run()` can be `true` or false, or throw a
     * `InsufficientStockException` that will be catch by the engine.
     */
    fun run(): CompletableFuture<CommandResult?>


}

abstract class SimpleCommand<out Payload>(override val type: String, override val payload: Payload) :Command<Payload, Void> {
    override fun generateEvent(result: Void?): NikotorEvent<*> {
        return this.generateEvent()
    }

    abstract fun generateEvent(): NikotorEvent<*>

    override fun run(): CompletableFuture<Void?> = CompletableFuture.runAsync{}
}





interface NikotorEvent<EventPayload> {
    val type: String
    val technicalDate: Long // the date when the event was really created
    val payload: EventPayload // the business information related to the event
}

data class NikEvent<P>(override val type: String, override val payload: P) :NikotorEvent<P>{
    override val technicalDate = Date().time
}

data class PersistedNikEvent<P>(
    override val sequenceId: Long,
    override val type: String,
    override val technicalDate: Long,
    override val payload: P
) : PersistedEvent<P>

interface PersistedEvent<EventPayload> : NikotorEvent<EventPayload> {
    val sequenceId: Long
}

typealias Events = List<NikotorEvent<*>>
typealias PersistedEvents = List<PersistedEvent<*>>

interface EventStore {
    fun  <P>persist(event: NikotorEvent<P>): CompletableFuture<PersistedEvent<*>>

    fun persistAll(events: Events): CompletableFuture<PersistedEvents>

    fun loadInitialEvents(): CompletableFuture<PersistedEvents>

    fun resetWith(events: Events): CompletableFuture<PersistedEvents>
}

interface ProjectionUpdater {
    fun <EventPayload> updateWithEvent(event: PersistedEvent<EventPayload>): CompletableFuture<Void>
}

interface NikotorSubscriber {
    val id: Long
    fun <EventPayload> reactTo(event: NikotorEvent<EventPayload>): Void
}

interface NikotorEngine {

    fun <Payload, CommandResult> process(command: Command<Payload, CommandResult>): CompletableFuture<PersistedEvent<*>>
    fun subscribe(newSubscriber: NikotorSubscriber): Void
    fun unsubscribe(oldSubscriber: NikotorSubscriber): Void
    val eventStore:EventStore

}
