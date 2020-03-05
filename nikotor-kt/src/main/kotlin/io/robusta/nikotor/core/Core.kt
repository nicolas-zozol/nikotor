package io.robusta.nikotor.core

import io.robusta.nikotor.NikotorValidationException
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
interface Command<out Payload, CommandResult> {

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
    suspend fun run(): CommandResult

    // TODO: Not happy at all with this * invariance
    fun generateEvents(result: CommandResult): Events
    //fun generateEvent(result: CommandResult): Event<*>

    // TODO: Command should return many events ?
}

/**
 * Command that create an event that depends on the run() result
 */
abstract class RunnableCommand<out Payload, Result>(override val payload: Payload) :
        Command<Payload, Result> {


    override fun generateEvents(result: Result): Events {
        return listOf(this.generateEvent(result))
    }

    abstract fun generateEvent(result:Result): Event<*>


}

/**
 * Command that could fail before creating an event
 */
abstract class ThrowableCommand<out Payload>(override val payload: Payload) :
        Command<Payload, Unit> {

    override suspend fun run(): Unit {
        runUnit()
    }

    abstract fun runUnit()

    override fun generateEvents(result: Unit): Events {
        return listOf(this.generateEvent())
    }

    abstract fun generateEvent(): Event<*>


}

/**
 * Command that does not run anything. Therefore the generated event is predictive if validation is
 */
abstract class SimpleCommand<out Payload>(override val payload: Payload) :
        Command<Payload, Unit> {

    override fun generateEvents(result: Unit): Events {
        return listOf(this.generateEvent())
    }

    abstract fun generateEvent(): Event<*>

    override suspend fun run() {

    }
}


interface Event<EventPayload> {
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

class PayloadId(val id: String)


interface ProjectionUpdater {
    suspend fun updateWithEvent(event: Event<*>)

    val concernedEvents: List<Class<*>>
}

interface NikotorSubscriber {
    val id: Long
    fun <EventPayload> reactTo(event: Event<EventPayload>): Void
}

interface NikotorEngine {

    suspend fun <Payload, CommandResult> process(
            command: Command<Payload, CommandResult>
    ): PersistedEvent<*, *>

    fun subscribe(newSubscriber: NikotorSubscriber): Void
    fun unsubscribe(oldSubscriber: NikotorSubscriber): Void
    val eventStore: EventStore

}
