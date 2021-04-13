package st.thegood.server.domain.merchant
import java.util.*

interface TgsUser {

    val language: String

    val firstName: String
    val lastName: String
    val description: String
    val email: String

    fun getName(): String

    fun getRoles(): List<String>

    fun lastConnection(): Date



}

interface Category{

}

/**
 * Separated object
 */
interface Picture{
    val id:String
    val alt:String
    val base64: String
}

/**
 * A product has one ProductDescription per language
 */
interface ProductDescription{
    val id: String
    val product: Product
    val language: String
    val description:String


}

interface ProductPrice{
    val id: String
    val product: Product
    val price: String
    val currency:String;
}

interface Product{

    val id: String
    val name: String
    val url: String
    val descriptions: List<ProductDescription>
    val merchant:Merchant
    val category: Category
    val prices: List<ProductPrice>
    // 0->100, based on 10 initials stars
    val quality: Int

}

interface Incident{
    val reason:String
    val date: Date;
    val explanation:String;
}

interface Merchant:TgsUser{

    val incidents: List<Incident>
    var company: String
    var userReferralLink: String
}

















