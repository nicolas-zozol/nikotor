package io.robusta.nikotor

import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @result: true if validation succeed
 * @reasons: Map of key / errors on the key
 */
class ValidationResult(var result:Boolean=true){

    private val reasons = mutableListOf<String>()
    fun check(condition:Boolean, reason:String): ValidationResult{
        if (!condition){
            result= false
            reasons+= reason
        }
        return this
    }

    fun throwIfInvalid(){
        if (result){
            throw NikotorValidationException(reasons.toString(), reasons)
        }
    }
}



/**
 * Commands are NOT responsible for authorization of its execution. Processing a
 * command comes afterward.
 *
 * A Command has a surface validation (payload empty, negative, not valid email...)
 * then is run(), which can do :
 *      - deeper validation against application state and maybe throw a IllegalStateException
 *      - side effect (write in a file, make a http query to Stripe...)
 *      - return a value of type CommandResult
 *
 * Some Command (#SimpleCommand, #ThrowableCommand) return a kotlin Unit result
 */
interface Command<out Payload, CommandResult>{

    val payload: Payload
    /**
     * Used for surface validation, mostly checking the values of the command Payload.
     *
     * Use the `run()` function to ensure the state of application is compatible with
     * your command
     *
     * Use `return ValidationResult()` to return a always true validation
     */
    fun validate(): ValidationResult


    /**
     * Warning: should not modify the projections !
     * Side effects are http request to external system, write in files....
     * It can also decide if projection state is suitable for executing command in nominal case.
     * For example the result of `run()` can be `true` or false, or throw a
     * `InsufficientStockException` that will be catch by the engine.
     *
     * If `run()` does nothing, you probably want a #SimpleCommand
     * If `run()` throws exception without returning a result, you probably want a #ThrowableCommand
     *
     */
    fun run(): Await<CommandResult>

    // TODO: Not happy at all with this * invariance
    fun  generateEvent(result:CommandResult): NikotorEvent<*>

    // TODO: Command should return many events ?
}




abstract class ThrowableCommand<out Payload>(override val payload: Payload) :Command<Payload, Unit> {

    override fun run():Await<Unit>{
        runUnit()
        return awaitUnit
    }

    abstract fun runUnit()

    override fun generateEvent(result: Unit): NikotorEvent<*> {
        return this.generateEvent()
    }

    abstract fun generateEvent(): NikotorEvent<*>


}

/**
 * Command that does not run anything. Therefore the generated event is predictive if validation is
 */
abstract class SimpleCommand<out Payload>(override val payload: Payload) :Command<Payload, Unit> {
    override fun generateEvent(result: Unit): NikotorEvent<*> {
        return this.generateEvent()
    }

    abstract fun generateEvent(): NikotorEvent<*>

    override fun run(): CompletableFuture<Unit> = awaitUnit
}





interface NikotorEvent<EventPayload> {
    val type: String
    /**
     * the date when the event was really created, or any pertinent date of your choice
     */
    val technicalDate: Long
    val payload: EventPayload // the business information related to the event
}

data class SimpleEvent<P>(override val type: String, override val payload: P) :NikotorEvent<P>{
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

    val concernedEvents:List<String>
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
