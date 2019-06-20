package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.model.DishItem
import com.food.nextdoor.model.HomeFeed
import kotlinx.android.synthetic.main.checkout_row.view.*
import system.CartItem
import system.Manager
import system.ShoppingCart
import system.Utility


// class CheckoutAdapter(val context: Context, val OrderDetails: List<Order>) : RecyclerView.Adapter<CheckoutAdapter.OrderDetailViewHolder>() {

    class CheckoutAdapter(val context: Context) : RecyclerView.Adapter<com.food.nextdoor.adapter.buyer.CheckoutAdapter.OrderDetailViewHolder>() {

    override fun getItemCount(): Int {
        return ShoppingCart.itemCount
      }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dishRowView = layoutInflater.inflate(com.food.nextdoor.R.layout.checkout_row, parent, false)
        return OrderDetailViewHolder(dishRowView)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val cartItem = ShoppingCart.getCartItems()[position]
        holder.setData(cartItem,position)
    }


    inner class OrderDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(cartItem: CartItem, position: Int) {
            val dishInfo: HomeFeed.Dish =
                Utility.DataHolder.homeFeedInstance!!.dishes.filter { d -> d.dish_id == cartItem.dishItem.dishId }
                    .single()

            // set Dish Symbol
            val dishSymbol = Utility.setDishSymbol(dishInfo.dish_type, itemView.img_dish_symbol_rv.context)
            itemView.img_dish_symbol_rv.setImageDrawable(dishSymbol)

            itemView.tv_dish_name_rv.text = dishInfo.dish_name
            itemView.tv_dish_price_rv.text = dishInfo.unit_price.toString()
            itemView.tv_dish_quantity_rv.text = cartItem.dishItem.quantity.toString()
            itemView.tv_dish_delivery_time_rv.text =
                cartItem.dishItem.deliveryStartTime.toString() + cartItem.dishItem.deliveryEndTime.toString()
            itemView.tv_dish_packing_type_rv.text = Manager.Companion.Preference()
                .getPackingDescription(cartItem.dishItem.packingTypeId) //cartItem.dishItem.packingTypeId.toString()
            itemView.tv_dish_delivery_type_rv.text =
                Manager.Companion.Preference().getDeliveryDescription(cartItem.dishItem.deliveryTypeId)

            itemView.tv_total_dish_price_rv.text = (dishInfo.unit_price * cartItem.dishItem.quantity).toString()
            itemView.tv_packing_and_delivery_charges_rv.text =
                this.getPackingAndDeliveryCharges(dishInfo, cartItem.dishItem).toString()
        }


        init {
            itemView.setOnClickListener {
                //Toast.makeText(context, {currentOrder!!.chef_id}  + "Clicked !" , Toast.LENGTH_SHORT).show()
            }

            itemView.tv_dish_name_rv.setOnClickListener {
                //Toast.makeText(context, currentOrder!!.chef_id + " CLicked !", Toast.LENGTH_SHORT).show()
            }


        }

        private fun getPackingAndDeliveryCharges(dishInfo: HomeFeed.Dish, dishItem: DishItem): Int {
            var amount = -1
            if (dishItem.deliveryTypeId == 1) {
                amount = dishInfo.delivery_charges
            }

            if (dishItem.packingTypeId == 2) {
                amount = amount + dishInfo.packing_charges
            }

            return amount
        }


    }























}

