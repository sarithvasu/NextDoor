package com.food.nextdoor.model
import com.google.gson.annotations.SerializedName
import system.StoreHouse
import kotlin.system.exitProcess


//data class OrderDetail (
//    @SerializedName("order") val order : Order
//)


class OrderDetail {
    @SerializedName("order")  var order : Order? = null

    companion object {
        val instance = OrderDetail()
    }

    data class Order (
        @SerializedName("chef_id") var chefId : Int,
        @SerializedName("seller_user_id") var   sellerUserId : Int,
        @SerializedName("seller_payee_id") var   sellerPayeeId : String,
        @SerializedName("buyer_id") var   buyerId : Int,
        @SerializedName("order_date_time") var   orderDateTime : String,
        @SerializedName("payment_mode") var   paymentMode : String,
        @SerializedName("dishes") var   dishes : ArrayList<DishItem>,
        @SerializedName("transactionDetail") var   transactionDetail : TransactionDetail
    )


}



 class DishItem {
    @SerializedName("dish_id")  var dishId: Int = -1
    @SerializedName("quantity") var quantity: Int = -1
    @SerializedName("delivery_start_time")  var deliveryStartTime: String = "Empty"
    @SerializedName("delivery_end_time")  var deliveryEndTime: String = "Empty"
    @SerializedName("packing_type_id")  var packingTypeId: Int = -1
    @SerializedName("delivery_type_id")  var deliveryTypeId: Int = -1
   }



data class TransactionDetail (
    @SerializedName("transaction_id") var   transactionId : String,
    @SerializedName("transaction_ref_id") var   transactionRefId : Int,
    @SerializedName("response_code") var   responseCode : Int,
    @SerializedName("transaction_status") var   transactionStatus : Int,
    @SerializedName("transaction_note") var   transactionNote : Int,
    @SerializedName("transaction_amount") var   transactionAmount : Int,
    @SerializedName("currency_code") var   currencyCode : String,
    @SerializedName("approvar_ref_no_beneficiary") var   approvarRefNoBeneficiary : String,
    @SerializedName("transaction_date_time") var   transactionDateTime : String
)



data class Product(
    @SerializedName("description")
    var description: String? = null,

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("quantity")
    var quantity: Int = -1,

    @SerializedName("ProductName")
    var name: String? = null,

    @SerializedName("price")
    var price: String? = null,

    @SerializedName("photos")
    var photos: ArrayList<Photo> = arrayListOf()
)

data class Photo(
    @SerializedName("filename")
    var filename: String? = null
)

// represents a single shopping cart item
data class CartItem(var product: Product, var quantity: Int = 0)





class ShoppingCart {

    companion object {
        fun addItem(cartItem: CartItem) {
            val cartItems: ArrayList<CartItem> = ShoppingCart.getCartItems()

            if(cartItems.size > 0 ) {
                // Check if the product is already there in the cart Item
                checkAndUpdateTheCart(cartItems, cartItem)
            } else {
                ShoppingCart.saveCartItem(cartItem)
            }
        }

        private fun checkAndUpdateTheCart(cartItems: ArrayList<CartItem>, cartItem: CartItem) {
            val existingCartItem: CartItem? = cartItems.singleOrNull { e -> e.product.id == cartItem.product.id }

            // product already there in the cart
            if (existingCartItem != null) {
                removeItem(existingCartItem)

                // Temp code Read back for Checking
                val cart = getCartItems()

                // Update the current product quantity with previous qty + 1 and save it to cart
                cartItem.product.quantity = existingCartItem.product.quantity + 1
                saveCartItem(cartItem)


                // Temp code Read back for Checking
                val cart1 = getCartItems()

                val s = "Stop"


            } else {
                saveCartItem(cartItem)
            }
        }


        fun removeItem(cartItem: CartItem) {
//            val cart = ShoppingCart.getCartItems()
//
//            val targetItem = cart!!.singleOrNull { it.product.id == cartItem.product.id }
//            if (targetItem != null) {
//                if (targetItem.quantity > 0) {
//                    targetItem.quantity--
//                } else {
//                    cart!!.remove(targetItem)
//                }
//            }
//
//            ShoppingCart.saveCart(cart)

            ShoppingCart.removeCartItem(cartItem)
        }


        fun removeCartItem(cartItem: CartItem) {
            val storeHouse = StoreHouse.instance
            storeHouse.removeCartItem(cartItem)
        }

        fun saveCartItem(cartItem: CartItem) {
            val storeHouse = StoreHouse.instance
            storeHouse.saveCartItem(cartItem)
        }

        fun getCartItems(): ArrayList<CartItem> {
            val storeHouse = StoreHouse.instance
            return storeHouse.getCartItems()
        }



        fun getShoppingCartSize(): Int {
            var cartSize = 0
            ShoppingCart.getCartItems()!!.forEach {
                cartSize += it.quantity;
            }

            return cartSize
        }
    }

}



// fun write(myMap: Map<Int,String>){


class MyStorage{

    //var price: Map<String,Product>? = null
    //myMap=mapOf<String, Product>()
    //val myMap1: Map<Int,String> =
    //val myMap: Map<String,Product> = mapOf<String, Product>()
    //val caseInsensitive = TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER)

    //private lateinit var myMap: Map<String,MutableList<CartItem>>
    //  //product.photos = ArrayList<Photo>()

//    private fun isInitialize() {
//        if (!Utility.IS_INITIALIZE) {
//            val myMap: Map<String,MutableList<CartItem>> = mapOf<String, MutableList<CartItem>>()
//
//            //myMap = mapOf<String, MutableList<CartItem>>()
//            Utility.IS_INITIALIZE = true
//        }
//    }


//        fun write(key: String, value: CartItem) {
//            //isInitialize()
//            myMap.plus(Pair(key, value))
//        }
//
//
//
//        fun read(key: String): ArrayList<CartItem> {
//            //isInitialize()
//           val iscontainsKey =  myMap.containsKey(key)
//           if (iscontainsKey) {
//                return myMap.getValue(key)
//            }
//            else {
//               return null
//           }
//        }


}

//class OrderDetail {
//    @SerializedName("order") lateinit  var order : Order
//}
//
//data class Order (
//    @SerializedName("chef_id") var chef_id : Int,
//    @SerializedName("seller_user_id") val seller_user_id : Int,
//    @SerializedName("seller_payee_id") val seller_payee_id : String,
//    @SerializedName("buyer_id") val buyer_id : Int,
//    @SerializedName("order_date_time") val order_date_time : String,
//    @SerializedName("payment_mode") val payment_mode : String,
//    @SerializedName("dishes") val dishes : List<DishOrder>,
//    @SerializedName("transactionDetail") val transactionDetail : TransactionDetail
//)
//
//data class DishOrder (
//    @SerializedName("dish_id") val dish_id : Int,
//    @SerializedName("quantity") val quantity : Int,
//    @SerializedName("delivery_start_time") val delivery_start_time : String,
//    @SerializedName("delivery_end_time") val delivery_end_time : String,
//    @SerializedName("packing_type_id") val packing_type_id : Int,
//    @SerializedName("delivery_type_id") val delivery_type_id : Int
//)
//
//data class TransactionDetail (
//    @SerializedName("transaction_id") val transaction_id : String,
//    @SerializedName("transaction_ref_id") val transaction_ref_id : Int,
//    @SerializedName("response_code") val response_code : Int,
//    @SerializedName("transaction_status") val transaction_status : Int,
//    @SerializedName("transaction_note") val transaction_note : Int,
//    @SerializedName("transaction_amount") val transaction_amount : Int,
//    @SerializedName("currency_code") val currency_code : String,
//    @SerializedName("approval_ref_no_beneficiary") val approval_ref_no_beneficiary : String,
//    @SerializedName("transaction_date_time") val transaction_date_time : String
//)



