import io.robusta.nikotor.InMemoryEventStore
import io.robusta.nikotor.core.NikotorEngine
import io.robusta.nikotor.SimpleNikotorEngine
import io.robusta.nikotor.user.UsersProjectionUpdater
import io.robusta.nikotor.user.usersDatabase
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoreTest {

    private lateinit var store: InMemoryEventStore
    private lateinit var engine: NikotorEngine
    private val updater = UsersProjectionUpdater()

    @Before
    fun init() {
        store = InMemoryEventStore()
        engine = SimpleNikotorEngine(store, listOf(updater))
        usersDatabase.clear()
    }

    @Test
    fun testBasic(){

    }


}
