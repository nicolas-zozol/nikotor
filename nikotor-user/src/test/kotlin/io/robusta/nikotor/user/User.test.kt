package io.robusta.nikotor.user

import kotlinx.coroutines.*

import io.robusta.nikotor.InMemoryEventStore
import io.robusta.nikotor.core.NikotorEngine
import io.robusta.nikotor.SimpleNikotorEngine
import io.robusta.nikotor.user.fixture.*
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.fail
import kotlin.test.assertEquals


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
        activationTokenDatabase.clear()
        accountSet.clear()
        visitSet.clear()
    }

    @Test
    fun testRegister() {
        runBlocking {

            engine.process(registerJohn)
            engine.process(registerJane)

            assert(store.events.size == 2)
            assert(usersDatabase.find(johnDoe.email) == johnDoe)

        }
    }

    @Test
    fun testActivate() {

        runBlocking {

            val email = johnDoe.email
            engine.process(registerJohn)
            val token = queryActivationTokenByEmail(email) ?: fail("no token found")
            engine.process(ActivateUserCommand(TokenPayload(email, token)))
            val activatedJohn = queryUserByEmail(email) ?: fail("No john")
            assertEquals(activatedJohn.activated, true)

        }
    }

    @Test
    fun testChangePassword() {
        runBlocking {
            engine.process(registerJane)
            val newPassword = "ABCD";
            val changeJanePassword = ChangePasswordCommand(HashedPasswordPayload(janeDoe.email, newPassword))
            engine.process(changeJanePassword)

            // cheking that login is now ok with new password
            LoginCommand(LoginHashPayload())


        }

    }


}
