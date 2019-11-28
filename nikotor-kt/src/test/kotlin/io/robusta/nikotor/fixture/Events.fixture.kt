package main.kotlin.nikotor.io.robusta.nikotor.fixtures

import io.robusta.nikotor.NikEvent
import io.robusta.nikotor.NikotorEvent
import main.kotlin.nikotor.io.robusta.nikotor.fixture.PotAuFeu
import main.kotlin.nikotor.io.robusta.nikotor.fixture.potAuFeu
import java.util.*

class PotAuFeuEventStarted (override val payload: PotAuFeu):NikotorEvent<PotAuFeu>{
    override val type = "POT_AU_FEU_STARTED";
    override val technicalDate = Date().time;
}

val potAuFeuStarted = PotAuFeuEventStarted(PotAuFeu("Tomato, Beef, Lemon", 1500));

// Looks much more simple ...
val potAuFeuEnded = NikEvent("POT_AU_FEU_ENDED", potAuFeu);







