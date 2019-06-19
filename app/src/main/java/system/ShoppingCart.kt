package system
import com.food.nextdoor.model.DishItem


class ShoppingCart {
    companion object {
        fun addToCart(cartItem: CartItem) {
            // Get all cart Items
            val cartItems: ArrayList<CartItem> = ShoppingCart.getItemsFromStoreHouse()

            // Cart is NOT EMPTY so check if the product is already there and Update the cart
            if (cartItems.size > 0) {
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
            if (cartItems.size > 0) {
                // If item exist then update the cart by increasing the quantity of the item
                this.UpdateStoreHouse(cartItems, cartItem, UpdateType.REMOVE)
            }
        }
        fun getCartItems(): ArrayList<CartItem> {
            return this.getItemsFromStoreHouse()
        }

        private fun getItemsFromStoreHouse(): ArrayList<CartItem> {
            return StoreHouse.instance.cartItemList
        }
        private fun removeItemFromStoreHouse(cartItem: CartItem) {
            StoreHouse.instance.cartItemList.remove(cartItem)
        }
        private fun saveItemInStoreHouse(cartItem: CartItem) {
            StoreHouse.instance.cartItemList.add(cartItem)
        }
        private fun UpdateStoreHouse(cartItems: ArrayList<CartItem>, cartItem: CartItem, updateType: UpdateType) {

            // Check if item exist in the cart
            val previousCartItem: CartItem? =
                cartItems.singleOrNull { e -> e.dishItem.dishId == cartItem.dishItem.dishId }

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
