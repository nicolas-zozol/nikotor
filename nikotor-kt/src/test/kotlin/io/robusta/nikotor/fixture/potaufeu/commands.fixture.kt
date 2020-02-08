package io.robusta.nikotor.fixture.potaufeu

import io.robusta.nikotor.core.SimpleCommand
import io.robusta.nikotor.core.Event
import io.robusta.nikotor.core.ValidationResult

class StartCommand(payload: PotAuFeu): SimpleCommand<PotAuFeu>(payload){
    override fun generateEvent(): Event<PotAuFeu> {
        return potAuFeuStarted
    }


    override fun validate(): ValidationResult {

        return ValidationResult()
                .check(payload.ingredients.isNotEmpty(), "Ingredients should not be empty")
    }
}

class EndCommand(payload: PotAuFeu): SimpleCommand<PotAuFeu>(payload){
    override fun generateEvent(): Event<PotAuFeu> {
        return potAuFeuStarted
    }


    override fun validate(): ValidationResult {
        return ValidationResult()
    }
}

val startCommand = StartCommand(potAuFeu)
val endCommand = EndCommand(potAuFeu)
