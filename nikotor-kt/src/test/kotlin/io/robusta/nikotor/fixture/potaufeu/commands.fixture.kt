package io.robusta.nikotor.fixture.potaufeu

import io.robusta.nikotor.SimpleCommand
import io.robusta.nikotor.NikotorEvent
import io.robusta.nikotor.ValidationResult

class StartCommand(payload: PotAuFeu): SimpleCommand<PotAuFeu>(payload){
    override fun generateEvent(): NikotorEvent<PotAuFeu> {
        return potAuFeuStarted
    }


    override fun validate(): ValidationResult {

        return ValidationResult()
                .check("ingredients", payload.ingredients.isNotEmpty(), "Ingredients should not be empty")
    }
}

class EndCommand(payload: PotAuFeu): SimpleCommand<PotAuFeu>(payload){
    override fun generateEvent(): NikotorEvent<PotAuFeu> {
        return potAuFeuStarted
    }


    override fun validate(): ValidationResult {
        return ValidationResult()
    }
}

val startCommand = StartCommand(potAuFeu)
val endCommand = EndCommand(potAuFeu)