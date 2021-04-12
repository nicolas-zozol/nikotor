package io.robusta.nikotor.core

import io.robusta.nikotor.NikotorValidationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @result: true if validation succeed
 * @reasons: Map of key / errors on the key
 */
class ValidationResult(var result: Boolean = true) {

    private val reasons = mutableListOf<String>()
    fun check(condition: Boolean, reason: String): ValidationResult {
        if (!condition) {
            result = false
            reasons += reason
        }
        return this
    }

    fun throwIfInvalid() {
        if (!result) {
            throw NikotorValidationException(reasons.toString(), reasons)
        }
    }
}

interface ICommandResult{
    val result: Boolean;
}

class OkResult:ICommandResult{
    override val result=true;
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
interface Command<out Payload, RunResult:ICommandResult>{

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
     * `InsufficientStockException`, `EmailAlreadyRegisteredException` that will be catch by the engine.
     *
     * It's up to you to design if the fact that the email is registered is a
     * CommandResult or a thrown exception, thought I would typically design it as a CommandResult
     *
     * If `run()` does nothing, you probably want a #SimpleCommand
     * If `run()` throws exception without returning a result, you probably want a #ThrowableCommand
     *
     */
    suspend fun run(): RunResult


    fun generateEvents(result: RunResult): Events


}

/**
 * Command that create an event that depends on the run() result
 */
abstract class RunnableCommand<out Payload, RunResult:ICommandResult>(override val payload: Payload) :
        Command<Payload, RunResult> {


    override fun generateEvents(result: RunResult): Events {
        return listOf(this.generateEvent(result))
    }

    abstract fun generateEvent(result:RunResult): Event<*>


}

/**
 * Command that could fail before creating an event.
 * @deprecated: just use RunnableCommand
 */
abstract class ThrowableCommand<out Payload>(override val payload: Payload) :
        Command<Payload, OkResult> {

    override suspend fun run(): OkResult {
        return OkResult()
    }

    override fun generateEvents(result: OkResult): Events {
        return listOf(this.generateEvent())
    }

    abstract fun generateEvent(): Event<*>
}

/**
 * Command that does not run anything. Therefore the generated event is predictive if validation is
 */
abstract class SimpleCommand<Payload>(override val payload: Payload) :
        Command<Payload, OkResult> {

    override fun generateEvents(result: OkResult): Events {
        return listOf(this.generateEvent())
    }

    /**
     * It's not the problem of the Command to decide what kind of Event
     * will be produced by the later system
     */
    abstract fun generateEvent(): Event<*>

    override suspend fun run() :OkResult{
        return OkResult();
    }
}


interface Event<out EventPayload> {
    val type: String
    val id: String
    /**
     * the date when the event was really created, or any pertinent date of your choice
     */
    val technicalDate: Long
    val payload: EventPayload // the business information related to the event
}

/**
 * Abstract because the generated type should never be SimpleEvent. That's not precise enough.
 */
abstract class SimpleEvent<P>(override val payload: P) : Event<P> {

    override val type = this.findType()
    override val id = UUID.randomUUID().toString()
    override val technicalDate = Date().time

    private fun findType(): String {
        return this.javaClass.name
    }
}




interface ProjectionUpdater {
    suspend fun updateWithEvent(event: Event<*>)

    val concernedEvents: List<Class<*>>
}

interface NikotorSubscriber {
    val id: Long
    fun <EventPayload> reactTo(event: Event<EventPayload>): Void
}

// TODO: when calm, try to change <Payload, CommandResult:ICommandResult> by <*,*> to simplify stuff
interface NikotorEngine {

    // TODO: there may be two process: one waiting for all the events,
    // one for waiting with one Event,
    // but more often waiting for the CommandResult
    suspend fun <Payload, CommandResult:ICommandResult> process(
            command: Command<Payload, CommandResult>
    ): PersistedEvent<*, *>

    /**
     * Used for Java interaction
     */
    fun <Payload, CommandResult:ICommandResult>processAsync(command: Command<Payload, CommandResult>)
            : CompletableFuture<PersistedEvent<*, *>>{
        return     GlobalScope.future { process(command) }

    }

    fun subscribe(newSubscriber: NikotorSubscriber): Void
    fun unsubscribe(oldSubscriber: NikotorSubscriber): Void
    val eventStore: EventStore

}
