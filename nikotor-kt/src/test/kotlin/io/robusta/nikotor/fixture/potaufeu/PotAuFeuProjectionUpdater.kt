package io.robusta.nikotor.fixture.potaufeu

import io.robusta.nikotor.PersistedEvent
import io.robusta.nikotor.ProjectionUpdater
import java.util.concurrent.CompletableFuture

// id, object
val potAuFeuDatabase = mutableMapOf<Int, PotAuFeu>()


object potAuFeuProjectionUpdater: ProjectionUpdater{
    override fun <EventPayload> updateWithEvent(event: PersistedEvent<EventPayload>): CompletableFuture<Void> {

        val nextIndex = potAuFeuDatabase.size +1
        when(event.type){

            PotAuFeuEvents.POT_AU_FEU_STARTED-> {
                val payload = event.payload as PotAuFeu
                potAuFeuDatabase[nextIndex]=payload
            }
        }

        return CompletableFuture.runAsync {  }

    }

}












