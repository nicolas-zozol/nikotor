package io.robusta.nikotor


import main.kotlin.nikotor.io.robusta.nikotor.fixture.StartCommand
import main.kotlin.nikotor.io.robusta.nikotor.fixture.badPotAuFeu
import main.kotlin.nikotor.io.robusta.nikotor.fixture.startCommand
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NikotorEngineTest {

    lateinit var store: InMemoryEventStore
    lateinit var engine: NikotorEngine

    @Before
    fun init() {
        store = InMemoryEventStore()
        engine = SimpleNikotorEngine(store)
    }


    @Test
    fun processSuccess() {
        val event = engine.process(startCommand).get()
        assert(store.events.size == 1)
        assert(event.sequenceId == 1L)

    }


    @Test
    fun processFailValidation() {
        assertThrows<NikotorValidationException> { engine.process(StartCommand(badPotAuFeu)) }

    }


}


