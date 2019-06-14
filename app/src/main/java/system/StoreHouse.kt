package system
import com.food.nextdoor.model.CartItem



// Singleton implementation by Soumen
class StoreHouse{

    private var cartItemList: ArrayList<CartItem> = arrayListOf()


    fun getCartItems(): ArrayList<CartItem>{
        return this.cartItemList
    }

    fun saveCartItem(cartItem: CartItem) {
       cartItemList.add(cartItem)
    }

    fun removeCartItem(cartItem: CartItem) {
        cartItemList.remove(cartItem)

    }

    private constructor() {
        println("Instance Created")
    }

    companion object{
        val instance: StoreHouse by lazy {StoreHouse()}
    }
}


// Calling

//fun main(){
//    val s1 = StoreHouse.instance
//    s1.ShoppingCart = "Soumen"
//    println(s1.ShoppingCart)
//
//
//    val s2 = StoreHouse.instance
//    println(s2.ShoppingCart) // will print "soumen"
//
//
//
//    val s3 = StoreHouse.instance
//    println(s3.ShoppingCart) // will print "soumen"
//
//}
