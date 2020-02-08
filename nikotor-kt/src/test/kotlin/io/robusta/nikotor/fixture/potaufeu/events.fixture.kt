package io.robusta.nikotor.fixture.potaufeu

import io.robusta.nikotor.core.SimpleEvent
import io.robusta.nikotor.core.Event
import java.util.*

object PotAuFeuEvents{
    const val POT_AU_FEU_STARTED = "POT_AU_FEU_STARTED"
    const val POT_AU_FEU_ENDED = "POT_AU_FEU_ENDED"
    val PROJECTION_ERROR = "PROJECTION_ERROR"
    val SERVER_ERROR = "SERVER_ERROR"
}


class PotAuFeuEventStarted (override val payload: PotAuFeu): Event<PotAuFeu> {
    override val type = PotAuFeuEvents.POT_AU_FEU_STARTED
    override val technicalDate = Date().time
}

val potAuFeuStarted = PotAuFeuEventStarted(PotAuFeu("Tomato, Beef, Lemon", 1500))

// Looks much more simple ...
val potAuFeuEnded = SimpleEvent(PotAuFeuEvents.POT_AU_FEU_ENDED, potAuFeu)







