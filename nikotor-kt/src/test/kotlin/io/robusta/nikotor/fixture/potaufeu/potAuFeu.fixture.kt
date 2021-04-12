package io.robusta.nikotor.fixture.potaufeu

import io.robusta.nikotor.data.DataSet
import io.robusta.nikotor.data.SimpleEntity


data class PotAuFeu(val ingredients: String, val time: Long) : SimpleEntity();

val potAuFeuStart = PotAuFeu("", 0)
val potAuFeuFinished = PotAuFeu("Tomato, Wine, Beef, Potatoes, Lemon", 1500)

val set = DataSet<PotAuFeu>()