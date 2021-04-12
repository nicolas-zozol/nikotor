package io.robusta.nikotor.fixture.potaufeu

import io.robusta.nikotor.core.Event
import io.robusta.nikotor.core.ProjectionUpdater
import io.robusta.nikotor.data.DataSet
import java.util.concurrent.CompletableFuture

// id, object
val potAuFeuDatabase = DataSet<PotAuFeu>()


object PotAuFeuProjectionUpdater : ProjectionUpdater {
    override val concernedEvents = listOf(
            PotAuFeuEventStarted::class.java,
            PotAuFeuWork::class.java)

    override suspend fun updateWithEvent(event: Event<*>) {

        when (event) {
            is PotAuFeuEventStarted -> {
                val payload = event.payload
                potAuFeuDatabase.add(payload)
            }
        }
    }

}












