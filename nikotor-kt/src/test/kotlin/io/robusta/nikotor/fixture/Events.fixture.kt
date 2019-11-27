package main.kotlin.nikotor.io.robusta.nikotor.fixtures

import io.robusta.nikotor.NikEvent
import io.robusta.nikotor.NikotorEvent
import java.util.*

class PotAuFeu(val ingredients: String, val time: Long) {
}

val potAuFeu = PotAuFeu("Tomato, Beef, Lemon", 1500);

class PotAuFeuEventStarted (override val payload:PotAuFeu):NikotorEvent<PotAuFeu>{
    override val type = "POT_AU_FEU_EVENT";
    override val technicalDate = Date().time;
}

val potAuFeuStarted = PotAuFeuEventStarted(PotAuFeu("Tomato, Beef, Lemon", 1500));

// Looks much more simple ...
val potAuFeuEnded = NikEvent("POT_AU_FEU_ENDED", potAuFeu);







