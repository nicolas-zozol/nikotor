package main.kotlin.nikotor.io.robusta.nikotor

import io.robusta.nikotor.InMemoryEventStore
import main.kotlin.nikotor.io.robusta.nikotor.fixtures.potAuFeuEnded
import main.kotlin.nikotor.io.robusta.nikotor.fixtures.potAuFeuStarted
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import java.util.function.Consumer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryEventStoreTest {

    lateinit var store: InMemoryEventStore;

    @Before
    fun init() {
        store = InMemoryEventStore();
    }

    @Test
    fun testEmpty() {
        assert(store.events.size == 0);
    }

    @Test
    fun add() {
        val future = store
                .add(potAuFeuStarted)
                .thenAccept { store.add(potAuFeuEnded) }
                .thenAccept { assert(store.events.size == 2) };
        future.get();

    }


}