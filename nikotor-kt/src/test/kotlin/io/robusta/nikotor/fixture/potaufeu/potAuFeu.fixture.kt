package io.robusta.nikotor.fixture.potaufeu


data class PotAuFeu(val ingredients: String, val time: Long)

val potAuFeu = PotAuFeu("Tomato, Beef, Lemon", 1500)
val badPotAuFeu = PotAuFeu("", 0)

