package st.thegood.server.runtime

import io.github.cdimascio.dotenv.dotenv
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import st.thegood.server.api.configureMerchantApi
import st.thegood.server.api.configureProductApi

fun main(args: Array<String>) {

    print("%%%%%%% Running SERVER from MAIN ######")
    val server = configureServer()
    server.config.addStaticFiles("/javalin", "web", Location.CLASSPATH)
    apiServer.start(7000)

}

