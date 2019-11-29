package main.kotlin.nikotor.io.robusta.nikotor.fixture


data class PotAuFeu(val ingredients: String, val time: Long) {
}

val potAuFeu = PotAuFeu("Tomato, Beef, Lemon", 1500)
val badPotAuFeu = PotAuFeu("", 0)

