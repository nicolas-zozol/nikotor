package st.thegood.server.api

import st.thegood.server.runtime.apiServer


fun configureProductApi(){
    apiServer.get("/products") { ctx -> ctx.result("Banana, Olives") }
}

