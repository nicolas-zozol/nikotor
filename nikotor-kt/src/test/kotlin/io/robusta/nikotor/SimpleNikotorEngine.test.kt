package io.robusta.nikotor


import io.robusta.nikotor.core.NikotorEngine
import io.robusta.nikotor.fixture.potaufeu.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NikotorEngineTest {

    lateinit var store: InMemoryEventStore
    lateinit var engine: NikotorEngine
    private val updater = PotAuFeuProjectionUpdater

    @Before
    fun init() {
        store = InMemoryEventStore()
        engine = SimpleNikotorEngine(store, listOf(updater))
        potAuFeuDatabase.clear()
    }


    @Test
    fun processSuccess() {
        runBlocking {
            assert(potAuFeuDatabase.isEmpty())
            val event = engine.process(startCommand)
            assert(store.events.size == 1)
            assertEquals(event.sequenceId, 1L, "Expect 1L, got ${event.sequenceId}")
            assert(potAuFeuDatabase.size == 1)
        }
    }


    @Test
    fun processFailValidation() {

        assert(potAuFeuDatabase.isEmpty())
        assertThrows<NikotorValidationException> {
            runBlocking { engine.process(StartCommand(potAuFeuStart)) }
        }
        assert(potAuFeuDatabase.isEmpty())
    }


}


