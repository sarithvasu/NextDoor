package system

import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.model.post.DishItem


class ShoppingCart {
    companion object {


        //private lateinit var m_buyerInfo: BuyerInfo
        val itemCount: Int
            get() = this.getCartItems().size

        val totalCartQuantityCount: Int
            get() = this.getTotalCartQuantity()

        fun getProductIdList() : ArrayList<Int> {
            val list: ArrayList<Int> = ArrayList()
            val cartItems = ShoppingCart.getCartItems()

            for(cartItem in cartItems) {
                val dishId = cartItem.dishItem.dishId
                list.add(dishId)
            }

           return list
        }

        fun addToCart(cartItem: CartItem) {
            StoreHouse.instance.cartItemList.add(cartItem)
        }
        fun removeFromCart(dishId: Int) {
            val cartItems: ArrayList<CartItem> = ShoppingCart.getItemsFromStoreHouse()
            val matchingElement: CartItem? = cartItems.find { c -> c.dishItem.dishId == dishId }
            if (matchingElement != null) {
                StoreHouse.instance.cartItemList.remove(matchingElement)
                val cartItems123: ArrayList<CartItem> = ShoppingCart.getItemsFromStoreHouse()
                var nice = "sdjsldj"
                nice = nice + "sdjksdhs"

            } else {
                // Soumen Need to handel Exception
            }
        }
        fun updateItemQuantity(dishId: Int, updateType: UpdateType) {
            val cartItems: ArrayList<CartItem> = ShoppingCart.getItemsFromStoreHouse()


            val matchingElement: CartItem? = cartItems.find { c-> c.dishItem.dishId == dishId }
            if (matchingElement != null) {
                val index = cartItems.indexOf(matchingElement)

                if (updateType == UpdateType.ADD) {
                    matchingElement.dishItem.quantity = matchingElement.dishItem.quantity + 1
                } else if (updateType == UpdateType.REMOVE) {
                    matchingElement.dishItem.quantity = matchingElement.dishItem.quantity - 1
                }

                cartItems.set(index, matchingElement)

                val cartItems123: ArrayList<CartItem> = ShoppingCart.getItemsFromStoreHouse()
                var nice = "sdjsldj"
                nice = nice + "sdjksdhs"
            } else {
                // Soumen Need to handel Exception
            }

        }
        fun getCartItems(): ArrayList<CartItem> {
            return getItemsFromStoreHouse()
        }
        fun getTotalCartAmount(): Int {
            val cartItems = ShoppingCart.getCartItems()

            var qty = 0; var price = 0; var amount = 0
            for(cartItem in cartItems){
                qty = cartItem.dishItem.quantity
                price = cartItem.dishItem.unitPrice
                amount =  amount + qty * price //  number *= 5   // number = number*5

                qty = 0; price = 0
            }

            return amount //  //return cartItems.map { s -> s.dishItem.unitPrice }.sum()
        }

        fun clearCart() {
            StoreHouse.instance.cartItemList.clear()
        }
        fun getTotalDeliveryCharges(): Int {
            val cartItems = ShoppingCart.getCartItems()
            var totalDeliveryCharges = 0
            val buyerHomeFeed = DataHolder.homeFeedInstance

            for (cartItem in cartItems) {
                val thisDishId = cartItem.dishItem.dishId
                val thisDeliveryId = cartItem.dishItem.deliveryTypeId

                // PackingOptions": 2 means => Parcel in disposable box and it is chargable
                if (thisDeliveryId == 1) {
                    val dishInfo: HomeFeed.Dish =
                        buyerHomeFeed.dishes.single { d -> d.DishId == thisDishId }
                    val thisDeliveryCharge = dishInfo.DeliveryCharge
                    totalDeliveryCharges += thisDeliveryCharge
                }
            }
            return totalDeliveryCharges
        }
        fun getTotalPackingCharges(): Int {
            val cartItems = ShoppingCart.getCartItems()
            var totalpackingcharges = 0
            val homeFeed: HomeFeed = DataHolder.homeFeedInstance

            for (cartItem in cartItems) {
                val thisDishId = cartItem.dishItem.dishId
                val thisPackingId = cartItem.dishItem.packingTypeId

                // PackingOptions": 2 means => Parcel in disposable box and it is chargable
                if (thisPackingId == 2) {
                    val dishInfo: HomeFeed.Dish = homeFeed.dishes.single { d -> val b = d.DishId == thisDishId
                        b
                    }
                    val thisPackingCharges = dishInfo.PackageCharge
                    val thisPackingTotal = thisPackingCharges * cartItem.dishItem.quantity
                    totalpackingcharges += thisPackingTotal
                }
            }
            return totalpackingcharges
        }

        private fun getTotalCartQuantity() : Int{
            val cartItems = ShoppingCart.getCartItems()
            var qty = 0;
            for(cartItem in cartItems){
                val currentQty = cartItem.dishItem.quantity
                qty = qty + currentQty
            }
            return qty
        }
        private fun getItemsFromStoreHouse(): ArrayList<CartItem> {
            return StoreHouse.instance.cartItemList
        }

    }
    private class StoreHouse{
        internal var cartItemList: ArrayList<CartItem> = arrayListOf()

        private constructor() {
            println("Instance Created")
        }

        companion object{
            val instance: StoreHouse by lazy {StoreHouse()}
        }
    }
}

// represents a single shopping cart item
data class CartItem(var dishItem: DishItem)
