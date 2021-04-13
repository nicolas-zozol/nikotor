package st.thegood.server.fixtures

import com.google.gson.JsonParser
import kong.unirest.HttpResponse
import kong.unirest.Unirest


var jsonParser = JsonParser()

fun jsonContains(json: String, key: String, value: String): Boolean {
    val root = jsonParser.parse(json);
    return root.asJsonObject.getAsJsonPrimitive(key).asString == value
}


fun jsonContains(json: String, key: String, value: Int): Boolean {
    val root = jsonParser.parse(json);
    return root.asJsonObject.getAsJsonPrimitive(key).asInt == value
}

fun postJson(url:String, body:String):HttpResponse<String>{
    val response: HttpResponse<String> = Unirest
            .post(url)
            .header("content-type", "application/json")
            .body(body)
            .asString()

    return response
}