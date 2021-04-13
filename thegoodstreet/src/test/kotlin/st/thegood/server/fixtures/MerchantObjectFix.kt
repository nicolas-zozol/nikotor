package st.thegood.server.fixtures

import st.thegood.server.domain.merchant.Incident
import st.thegood.server.domain.merchant.Merchant
import java.util.*



class TestMerchant(override val email:String):Merchant{

    override var company =""
    override var userReferralLink: String
        get() = "ABCD"
        set(value) {}

    override val incidents: List<Incident> = listOf()

    override val language ="fr"
    override val firstName = "John"
    override val lastName = "Doe"
    override val description = "Solid Merchant"


    override fun getName(): String {
        return email
    }

    override fun getRoles(): List<String> {
        return listOf("Merchant")
    }

    override fun lastConnection(): Date {
        return Date()
    }
}

val merchants = listOf<Merchant>(TestMerchant("nz@acme.com"))