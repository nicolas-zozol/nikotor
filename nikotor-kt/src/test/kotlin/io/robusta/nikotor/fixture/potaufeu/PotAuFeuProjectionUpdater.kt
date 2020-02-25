package io.robusta.nikotor.fixture.potaufeu

import io.robusta.nikotor.core.Event
import io.robusta.nikotor.core.PersistedEvent
import io.robusta.nikotor.core.ProjectionUpdater
import java.util.concurrent.CompletableFuture

// id, object
val potAuFeuDatabase = mutableMapOf<Int, PotAuFeu>()


object potAuFeuProjectionUpdater: ProjectionUpdater {
    override val concernedEvents = listOf(PotAuFeuEvents.POT_AU_FEU_STARTED)

    override fun  updateWithEvent(event: Event<*>): CompletableFuture<Void> {

        val nextIndex = potAuFeuDatabase.size +1
        when(event.type){
            PotAuFeuEventStarted-> {
                val payload = event.payload
                potAuFeuDatabase[nextIndex]=payload
            }
        }

        return CompletableFuture.runAsync {  }

    }

}












