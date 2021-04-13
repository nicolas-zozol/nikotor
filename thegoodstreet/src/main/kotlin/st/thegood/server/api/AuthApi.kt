package st.thegood.server.api

import com.google.gson.JsonParser
import io.github.cdimascio.dotenv.dotenv
import io.javalin.http.Context
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import kong.unirest.Unirest
import st.thegood.server.authentication.Profile
import st.thegood.server.runtime.apiServer
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*





private val jsonParser = JsonParser()
/**
 * Implementation of OAuth, only Google for the moment
 * Good explanations here:
 * https://developers.google.com/identity/protocols/oauth2/web-server
 *
 * JWT 101 in Java: https://stormpath.com/blog/beginners-guide-jwts-in-java
 */
internal fun configureAuthApi() {
    apiServer.get("/create-authorization-url", ::callGoogle)
    apiServer.get("/auth-callback-url", ::callbackGoogle)
    apiServer.get("/google/exchange-code-token", ::callbackExchange)
}


private fun callGoogle(ctx: Context) {

    val env = dotenv()
    val googleId = env["OAUTH_GOOGLE_ID"]
    val googleSecret = env["OAUTH_GOOGLE_SECRET"]
    val testCallback = env["TEST_CALLBACK"]
    val urlCallback = URLEncoder.encode(testCallback, "UTF-8")

    val secretState = "secret" + Random().nextInt(999999)

    val url = "https://accounts.google.com/o/oauth2/v2/auth" +
            "?access_type=online&prompt=consent&response_type=code" +
            "&client_id=$googleId" +
            "&redirect_uri=$urlCallback" +
            "&scope=profile%20email&state=$secretState"

    val map = mapOf(Pair("url", url));
    ctx.json(map)
}

private fun callbackGoogle(ctx: Context){
    val env = dotenv()

    val googleId = env["OAUTH_GOOGLE_ID"]
    val googleSecret = env["OAUTH_GOOGLE_SECRET"]
    val testCallback = env["TEST_CALLBACK"]

    val code = ctx.req.getParameter("code");
    val secretState = ctx.req.getParameter("secret")
    println("$code --$secretState")

    // Exchange to token
    val tokenUrl = "https://oauth2.googleapis.com/token";
    val response = Unirest.post(tokenUrl).header("Content-Type", "application/x-www-form-urlencoded")
            .field("client_id", googleId)
            .field("client_secret", googleSecret)
            .field("code", code)
            .field("grant_type", "authorization_code")
            .field("redirect_uri", testCallback)
            .asString();
    println(response)
    val jsonString = response.body

    val json = jsonParser.parse(jsonString).asJsonObject;
    /** TODO: should validate the JWT token, without calling google
    * https://developers.google.com/identity/sign-in/web/backend-auth#verify-the-integrity-of-the-id-token
    * https://stackoverflow.com/a/38719026/968988
    */

    val accessToken = json.get("access_token");
    val idToken = json.get("id_token")

    val jsonBodyToken = decodeJwtPayload(idToken.asString)

    val dataJson = jsonParser.parse(jsonBodyToken).asJsonObject;


    /* Extracting interesting data from the token */
    val email = dataJson.get("email").asString
    val fullName = dataJson.get("name").asString
    val pictureUrl = dataJson.get("picture").asString
    val locale = dataJson.get("locale").asString
    val profile = Profile(email, fullName)
    profile.locale = locale
    profile.pictureUrl = pictureUrl

    // command : save Profile

    ctx.result("Success!");

}

fun decodeJwtPayload(idToken:String):String{
    val decoder = Base64.getUrlDecoder()
    // split out the "parts" (header, payload and signature)
    val parts= idToken.split(".")

    val payloadJson = String(decoder.decode(parts[1]))
    return payloadJson;
}



private fun callbackExchange(ctx: Context){
    val env = dotenv()

    val googleId = env["OAUTH_GOOGLE_ID"]
    val googleSecret = env["OAUTH_GOOGLE_SECRET"]
    val testCallback = env["EXCHANGE_CALLBACK"]

    val accessToken = ctx.req.getParameter("access_token");
    val expires = ctx.req.getParameter("expires_in")
    val refreshToken = ctx.req.getParameter("refresh_token")
    val scope = ctx.req.getParameter("scope")
    val tokenType = ctx.req.getParameter("token_type")


}



private fun builtTokenGoogle(ctx: Context) {

    // From here, we should have an Authentication object from Google
    // Read the code
    // To exchange an authorization code for an access token, call the https://oauth2.googleapis.com/token endpoint and set the following parameters:

     val AUTHORITIES_KEY = "auth";


    val env = dotenv()
    var jwtSecret = env["JWT_SECRET"] ?: "jwt secret is not good";
    val keyBytes = jwtSecret.toByteArray(StandardCharsets.UTF_8);

    val key = Keys.hmacShaKeyFor(keyBytes);
    val tokenValidityInMilliseconds = 1000 * 3600;


    val authorities = listOf("Hahaha", "Hehehe");

    val now = Date().time
    val validity = Date(now + tokenValidityInMilliseconds)


    val token =  Jwts.builder()
            .setSubject("John Doe")
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
}



