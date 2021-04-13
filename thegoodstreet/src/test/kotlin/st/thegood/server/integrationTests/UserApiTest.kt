package st.thegood.server.integrationTests

import io.javalin.plugin.json.JavalinJson
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import org.assertj.core.api.Assertions
import org.junit.Test
import st.thegood.server.fixtures.merchants
import st.thegood.server.runtime.configureServer



class UserFunctionalTest {

    val port = 1234
    private val app = configureServer() // inject any dependencies you might have
    private val merchantsJson = JavalinJson.toJson(merchants)

    @Test
    fun `Create a User`() {

        app.start(1234)

        val response: HttpResponse<String> = Unirest.post("http://localhost:$port/merchant")
                .asString()
        Assertions.assertThat(response.status).isEqualTo(201)

        app.stop()
    }

}

