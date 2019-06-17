package com.food.nextdoor.model
import com.google.gson.annotations.SerializedName
import system.StoreHouse
import system.UpdateType


//data class OrderDetail (
//    @SerializedName("order") val order : Order
//)


//class OrderDetail {
//    var order : Order? = null
//
//    companion object {
//        val instance = OrderDetail()
//    }
//}


data class Order(
    var chefId : Int? = null,
    var sellerUserId : Int? = null,
    var sellerPayeeId : Int? = null,
    var buyerId : Int? = null,
    var orderDateTime : String? = null,
    var paymentMode : String? = null,
    var dishes : ArrayList<DishItem> = arrayListOf(),
    var transactionDetail : TransactionDetail? = null
)

class DishItem (
    var dishId :  Int = -1,
    var quantity:  Int = -1,
    var deliveryStartTime: String? = null,
    var deliveryEndTime:String? = null,
    var packingTypeId: Int? = null,
    var deliveryTypeId: Int? = null
)

data class TransactionDetail (
    var transactionId : String? = null,
    var transactionRefId : String? = null,
    var responseCode : String? = null,
    var transactionStatus :String? = null,
    var transactionNote : String? = null,
    var transactionAmount : Double? = null,
    var currencyCode : String? = null,
    var approvarRefNoBeneficiary :String? = null,
    var transactionDateTime : String? = null
)

data class Product(
    @SerializedName("description") var description: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("quantity") var quantity: Int = -1,
    @SerializedName("Product_name") var name: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("photos") var photos: ArrayList<Photo> = arrayListOf()
)

data class Photo(
    @SerializedName("filename")
    var filename: String? = null
)

// represents a single shopping cart item
//data class CartItem(var product: Product, var cartQuantity: Int = 0)
data class CartItem(var dishItem: DishItem)





class ShoppingCart {

    companion object {
        fun addToCart(cartItem: CartItem) {
            // Get all cart Items
            val cartItems: ArrayList<CartItem> = ShoppingCart.getItemsFromStoreHouse()

            // Cart is NOT EMPTY so check if the product is already there and Update the cart
            if(cartItems.size > 0 ) {
                // If item exist then update the cart by increasing the quantity of the item
                this.UpdateStoreHouse(cartItems, cartItem, UpdateType.ADD)
            } else {
                // It's Empty cart...so Add directly
                this.saveItemInStoreHouse(cartItem)
            }
        }
        fun removeFromCart(cartItem: CartItem) {
            // Get all cart Items
            val cartItems: ArrayList<CartItem> = ShoppingCart.getItemsFromStoreHouse()

            // Cart is NOT EMPTY so check if the item is already there and Update the cart
            if(cartItems.size > 0 ) {
                // If item exist then update the cart by increasing the quantity of the item
                this.UpdateStoreHouse(cartItems, cartItem, UpdateType.REMOVE)
            }
        }
        fun getCartItems(): ArrayList<CartItem> {
            return this.getItemsFromStoreHouse()
        }

        private fun getItemsFromStoreHouse(): ArrayList<CartItem> {
            val storeHouse = StoreHouse.instance
            return storeHouse.getCartItems()
        }
        private fun removeItemFromStoreHouse(cartItem: CartItem) {
            val storeHouse = StoreHouse.instance
            storeHouse.removeCartItem(cartItem)
        }
        private fun saveItemInStoreHouse(cartItem: CartItem) {
            val storeHouse = StoreHouse.instance
            storeHouse.saveCartItem(cartItem)
        }
        private fun UpdateStoreHouse(cartItems: ArrayList<CartItem>, cartItem: CartItem, updateType: UpdateType) {

            // Check if item exist in the cart
            val previousCartItem: CartItem? = cartItems.singleOrNull { e -> e.dishItem.dishId == cartItem.dishItem.dishId}

            // Item already exist in the cart (Duplicate Item)
            if (previousCartItem != null) {
                //So, remove it from the cart
                this.removeItemFromStoreHouse(previousCartItem)

                // We will increase or decrease the qty based on Enum Value
                if (updateType == UpdateType.ADD) {
                    // Update the current product quantity with previous qty + 1
                    cartItem.dishItem.quantity = previousCartItem.dishItem.quantity + 1
                    this.saveItemInStoreHouse(cartItem) // save it to cart
                } else if (updateType == UpdateType.REMOVE) {
                    if (previousCartItem.dishItem.quantity > 1) {
                        cartItem.dishItem.quantity = previousCartItem.dishItem.quantity - 1
                        this.saveItemInStoreHouse(cartItem) // save it to cart
                    }
                }
            } else {
                // Its not a duplicate Item so...We will save or remove the item based on Enum Value
                if (updateType == UpdateType.ADD) {
                    this.saveItemInStoreHouse(cartItem)
                } else if (updateType == UpdateType.REMOVE) {
                    this.removeItemFromStoreHouse(cartItem)
                }
            }
        }
    }

}












