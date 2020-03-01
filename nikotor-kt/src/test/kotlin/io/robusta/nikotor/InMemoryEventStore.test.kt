package main.kotlin.nikotor.io.robusta.nikotor

import io.robusta.nikotor.InMemoryEventStore
import io.robusta.nikotor.fixture.potaufeu.potAuFeuEnded
import io.robusta.nikotor.fixture.potaufeu.potAuFeuStarted

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
        val future = store
                .persist(potAuFeuStarted)
                .thenAccept { store.persist(potAuFeuEnded) }
                .thenAccept { assert(store.events.size == 2) }
        future.get()
    }

    @Test
    fun resetWith() {
        val future = store
            .persist(potAuFeuStarted)
            .thenAccept { store.persist(potAuFeuEnded) }
            .thenAccept { store.resetWith(listOf(potAuFeuEnded)) }
            .thenAccept { assert(store.events.size == 1) }
        future.get()
    }

    @Test
    fun fromIndex(){
        val future = store
            .persist(potAuFeuStarted)
            .thenApply { store.persist(potAuFeuEnded) }
            .thenCompose { store.getAllEventsStartingFromIndex(1) }
        val value = future.get()[0]
        assertSame(value.event.type, potAuFeuEnded.type)


    }

}
