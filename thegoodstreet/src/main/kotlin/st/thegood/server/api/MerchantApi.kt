package st.thegood.server.api

import st.thegood.server.runtime.apiServer


internal fun configureMerchantApi(){
    apiServer.get("/about") { ctx -> ctx.result("The Good Street - Copyright 2020-2021 Robusta Code") }
}

