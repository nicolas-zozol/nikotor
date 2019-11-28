package main.kotlin.nikotor.io.robusta.nikotor.fixture

import io.robusta.nikotor.Command
import io.robusta.nikotor.NikotorEvent
import java.util.concurrent.CompletableFuture

class StartCommand(override val payload: PotAuFeu) : Command<PotAuFeu, Void>{
    override val type = "START_COMMAND";
    override fun validate(): Boolean {
        return payload.ingredients.isNotEmpty();
    }

    override fun <EventPayload> generateEvent(result: Void): NikotorEvent<EventPayload> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun run(): CompletableFuture<Void> {
        return CompletableFuture.runAsync{};
    }

    override fun getCommandResult(): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
