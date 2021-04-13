package st.thegood.server.authentication

import io.robusta.nikotor.data.HasId
import st.thegood.server.domain.merchant.TgsUser

internal val authenticatedUsers: List<Profile> = mutableListOf();

internal interface User: HasId{
    val profile:Profile;
    val email : String
}

internal class Profile(val email: String, val name: String) {
    var pictureUrl: String? = null
    var locale: String = "en"
}


internal class Authentication {


    fun validateToken(token: String): Boolean {
        return true;
    }

    fun getEmail(token: String) {

    }


    fun login(token: String): TgsUser? {

        return null
    }


}