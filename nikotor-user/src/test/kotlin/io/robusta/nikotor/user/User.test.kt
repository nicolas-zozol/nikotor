package io.robusta.nikotor.user

import io.robusta.nikotor.InMemoryEventStore
import io.robusta.nikotor.core.NikotorEngine
import io.robusta.nikotor.SimpleNikotorEngine
import io.robusta.nikotor.user.UsersProjectionUpdater
import io.robusta.nikotor.user.fixture.FakeEmailSender
import io.robusta.nikotor.user.fixture.johnDoe
import io.robusta.nikotor.user.fixture.registerJane
import io.robusta.nikotor.user.fixture.registerJohn
import io.robusta.nikotor.user.usersDatabase
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersFeatureTest {

    private lateinit var store: InMemoryEventStore
    private lateinit var engine: NikotorEngine
    private val updater = UsersProjectionUpdater(UserBundle(FakeEmailSender()))

    @Before
    fun init() {
        store = InMemoryEventStore()
        engine = SimpleNikotorEngine(store, listOf(updater))
        usersDatabase.clear()
    }

    @Test
    fun testRegister(){
        val event = engine
                .process(registerJohn)
                .thenAccept {engine.process(registerJane)}
                .get()
        assert(store.events.size == 2)
        assert(usersDatabase.find(johnDoe.email) == johnDoe)



    }


}
