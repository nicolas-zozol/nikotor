package io.robusta.nikotor.core

import io.robusta.nikotor.Await
import io.robusta.nikotor.NikotorValidationException
import io.robusta.nikotor.awaitUnit
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
        if (result) {
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
    fun run(): Await<CommandResult>

    // TODO: Not happy at all with this * invariance
    fun generateEvent(result: CommandResult): Event<*>

    // TODO: Command should return many events ?
}


abstract class ThrowableCommand<out Payload>(override val payload: Payload) :
    Command<Payload, Unit> {

    override fun run(): Await<Unit> {
        runUnit()
        return awaitUnit
    }

    abstract fun runUnit()

    override fun generateEvent(result: Unit): Event<*> {
        return this.generateEvent()
    }

    abstract fun generateEvent(): Event<*>


}

/**
 * Command that does not run anything. Therefore the generated event is predictive if validation is
 */
abstract class SimpleCommand<out Payload>(override val payload: Payload) :
    Command<Payload, Unit> {
    override fun generateEvent(result: Unit): Event<*> {
        return this.generateEvent()
    }

    abstract fun generateEvent(): Event<*>

    override fun run(): CompletableFuture<Unit> = awaitUnit
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

open class SimpleEvent<P>(override val payload: P) : Event<P> {

    override val type = this.findType()
    override val id = UUID.randomUUID().toString()
    override val technicalDate = Date().time

    private fun findType(): String {
        return this.javaClass.name
    }
}

interface ProjectionUpdater {
    fun updateWithEvent(event: Event<*>): CompletableFuture<Void>

    val concernedEvents: List<String>
}

interface NikotorSubscriber {
    val id: Long
    fun <EventPayload> reactTo(event: Event<EventPayload>): Void
}

interface NikotorEngine {

    fun <Payload, CommandResult> process(
        command: Command<Payload, CommandResult>
    ): CompletableFuture<PersistedEvent<*, *>>

    fun subscribe(newSubscriber: NikotorSubscriber): Void
    fun unsubscribe(oldSubscriber: NikotorSubscriber): Void
    val eventStore: EventStore

}
