package io.robusta.nikotor.fixture.potaufeu

import io.robusta.nikotor.core.SimpleEvent



class PotAuFeuEventStarted (override val payload: PotAuFeu): SimpleEvent<PotAuFeu>(payload)
class PotAuFeuWork(override val payload: Pair<String, Int>):SimpleEvent<Pair<String, Int>>(payload)
class PotAuFeuCookedEvent (override val payload: PotAuFeu): SimpleEvent<PotAuFeu>(payload)

val potAuFeuStarted = PotAuFeuEventStarted(PotAuFeu("Tomato, Beef, Lemon", 1500))

val work = PotAuFeuWork(Pair("Potatoes", 500))

// Looks much more simple ...
val potAuFeuEnded = PotAuFeuCookedEvent(potAuFeuFinished)

