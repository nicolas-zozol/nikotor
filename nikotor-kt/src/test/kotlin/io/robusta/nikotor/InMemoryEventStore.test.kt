package main.kotlin.nikotor.io.robusta.nikotor

import io.robusta.nikotor.InMemoryEventStore
import io.robusta.nikotor.fixture.potaufeu.potAuFeuEnded
import io.robusta.nikotor.fixture.potaufeu.potAuFeuStarted
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertSame

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryEventStoreTest {

    private lateinit var store: InMemoryEventStore

    @Before
    fun init() {
        store = InMemoryEventStore()
    }

    @Test
    fun testEmpty() {
        assert(store.events.size == 0)
    }

    @Test
    fun add() {
        runBlocking {
            store.persist(potAuFeuStarted)
            store.persist(potAuFeuEnded)
            assert(store.events.size == 2)
        }
    }

    @Test
    fun resetWith() {

        runBlocking {
            store.persist(potAuFeuStarted)
            store.persist(potAuFeuEnded)
            store.resetWith(listOf(potAuFeuEnded))
            assert(store.events.size == 1)

        }
    }

    @Test
    fun fromIndex() {

        runBlocking {
            store.persist(potAuFeuStarted)
            store.persist(potAuFeuEnded)
            val events = store.getAllEventsStartingFromIndex(1)
            val value = events[0]
            assertSame(value.event.type, potAuFeuEnded.type)
        }

    }

}
