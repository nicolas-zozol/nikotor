package st.thegood.server.runtime

import io.javalin.Javalin
import st.thegood.server.api.configureAuthApi
import st.thegood.server.api.configureMerchantApi
import st.thegood.server.api.configureProductApi

val apiServer = Javalin.create()

fun configureServer():Javalin{
    configureMerchantApi()
    configureProductApi()
    configureAuthApi()
    return apiServer
}


internal fun getJavalinApplication():Javalin{
    return apiServer;
}

