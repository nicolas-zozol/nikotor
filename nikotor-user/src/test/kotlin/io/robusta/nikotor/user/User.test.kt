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
import kotlin.test.assertTrue


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

            engine.process(registerJohn())
            engine.process(registerJane())

            assert(store.events.size == 2)
            assert(usersDatabase.find(johnDoeEmail) == getNewJohnDoe())

        }
    }

    @Test
    fun testActivate() {

        runBlocking {

            val email = johnDoeEmail
            engine.process(registerJohn())
            val token = queryActivationTokenByEmail(email) ?: fail("no token found")
            engine.process(ActivateUserCommand(TokenPayload(email, token)))
            val activatedJohn = queryUserByEmail(email) ?: fail("No john")
            assertEquals(activatedJohn.activated, true)
        }

    }

    @Test
    fun testChangePassword() {
        runBlocking {
            engine.process(registerJane())
            val newPassword = "ABCD";
            val changeJanePassword = ChangePasswordCommand(EmailPasswordPayload(janeDoeEmail, newPassword))
            engine.process(changeJanePassword)

            // cheking that login is now ok with new password
            val loginCommand = LoginCommand(LoginAttemptPayload(janeDoeEmail, newPassword))
            val result = engine.process(loginCommand)
            assertTrue(result.sequenceId > 1)
            assertTrue(privateQueryCheckPassword(janeDoeEmail, "ABCD"))


        }

    }


    @Test
    fun testRegisterThenActivateThenLogin(){
        runBlocking {
            val email = johnDoeEmail
            engine.process(registerJohn())
            val token = queryActivationTokenByEmail(email) ?: fail("no token found")
            engine.process(ActivateUserCommand(TokenPayload(email, token)))

            engine.process(loginJohn())
            assertTrue(privateQueryCheckPassword(email, johnDoePassword))

        }
    }


}
