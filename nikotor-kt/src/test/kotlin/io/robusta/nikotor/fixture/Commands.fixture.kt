package main.kotlin.nikotor.io.robusta.nikotor.fixture

import io.robusta.nikotor.SimpleCommand
import io.robusta.nikotor.NikotorEvent
import io.robusta.nikotor.ValidationResult
import io.robusta.nikotor.fixture.PotAuFeuEventStarted
import io.robusta.nikotor.fixture.potAuFeuStarted
import java.util.concurrent.CompletableFuture

class StartCommand(payload: PotAuFeu): SimpleCommand<PotAuFeu>("START_COMMAND",payload){
    override fun generateEvent(): NikotorEvent<PotAuFeu> {
        return potAuFeuStarted
    }


    override fun validate(): ValidationResult {

        return ValidationResult()
                .check("ingredients", payload.ingredients.isNotEmpty(), "Ingredients should not be empty")
    }
}

class EndCommand(payload: PotAuFeu): SimpleCommand<PotAuFeu>("END_COMMAND",payload){
    override fun generateEvent(): NikotorEvent<PotAuFeu> {
        return potAuFeuStarted
    }


    override fun validate(): ValidationResult {
        return ValidationResult()
    }
}

val startCommand = StartCommand(potAuFeu)
val endCommand = EndCommand(potAuFeu)