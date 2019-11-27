package com.food.nextdoor.adapter.buyer

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.activity.buyer.CheckoutActivity
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.listeners.OnItemCountListener

import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.model.post.DishItem
import kotlinx.android.synthetic.main.checkout_row.view.*
import system.*

class CheckoutAdapter(val context: Context) : RecyclerView.Adapter<OrderDetailViewHolder>() {

    private lateinit var listener: OnItemCountListener
    private var mTotalPackingCharges:Int = 0 // Packing charges is applicable per dish Item
    private  var mDishes :ArrayList<HomeFeed.Dish> = DataHolder.homeFeedInstance.dishes as ArrayList<HomeFeed.Dish>

    override fun getItemCount(): Int {
        return ShoppingCart.itemCount
      }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dishRowView = layoutInflater.inflate(com.food.nextdoor.R.layout.checkout_row, parent, false)
        this.listener= context as CheckoutActivity
        return OrderDetailViewHolder(dishRowView)
    }
    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val cartItem = ShoppingCart.getCartItems()[position]
        val dishInfo: HomeFeed.Dish? = mDishes.find { d -> d.DishId == cartItem.dishItem.dishId }

        if (dishInfo == null) {
            Utility.createCustomDialog(context,  "dishInfo is null in onBindViewHolder",true)
        } else {
            // set Dish Symbol
            val dishSymbol = Utility.setDishSymbol(dishInfo!!.DishType, holder.view.img_dish_symbol_rv.context)
            holder.view.img_dish_symbol_rv.setImageDrawable(dishSymbol)

            holder.view.tv_dish_name_rv.text = dishInfo.DishName
            holder.view.tv_dish_price_rv.text = "\u20B9 ${cartItem.dishItem.unitPrice.toString()}"
            holder.view.tv_dish_quantity_rv.text = cartItem.dishItem.quantity.toString()
            holder.view.tv_dish_delivery_time_rv.text =
                Utility.standardDateToTimeSlot(cartItem.dishItem.deliveryStartTime.toString()) + " | " +  Utility.standardDateToTimeSlot(cartItem.dishItem.deliveryEndTime.toString())
            holder.view.tv_dish_packing_type_rv.text = NextDoorDB.invoke(context).daoAccess.getPackingDescriptionById(cartItem.dishItem.packingTypeId) //cartItem.dishItem.packingTypeId.toString()

            //holder.view.tv_dish_delivery_type_rv.text =  Manager.Companion.Preference().getDeliveryDescriptionById(cartItem.dishItem.deliveryTypeId)
            holder.view.tv_dish_delivery_type_rv.text =  NextDoorDB.invoke(context).daoAccess.getDeliveryDescriptionById(cartItem.dishItem.deliveryTypeId)


            val itemTotal: Int = (dishInfo.UnitPrice * cartItem.dishItem.quantity)
            holder.view.tv_total_unit_price_rv.text = "₹ ${itemTotal.toString()}"

            val packingCharges: Int = this.getPackingCharges(dishInfo, cartItem.dishItem)
            mTotalPackingCharges += packingCharges
            holder.view.tv_packing_charges_rv.text = (packingCharges).toString()

            holder.view.tv_delivery_charges_rv.text = this.getDeliveryCharges(dishInfo, cartItem.dishItem).toString()
            if (holder.view.tv_dish_quantity_rv.text == 0.toString()) {
                holder.view.tv_delivery_charges_rv.text = "0"
            }
            // Reset  Variables to Zero
            //itemTotal = 0
            mTotalPackingCharges = 0

            // Set ClickListener for + Button
            holder.view.tv_plus_chechout_rv.setOnClickListener {
                var count = holder.view.tv_dish_quantity_rv.text.toString().toInt()

                if (count < 99) {
                    count++
                    ShoppingCart.updateItemQuantity(cartItem.dishItem.dishId, UpdateType.ADD)
                    holder.view.tv_dish_quantity_rv.text = count.toString()
                    notifyItemChanged(position)
                    listener.updateCount()
                } else {
                    // Soumen Need to write tost
                }
            }


            holder.view.tv_minus_checkout_rv.setOnClickListener {
                var count = holder.view.tv_dish_quantity_rv.text.toString().toInt()

                if (count > 1) {
                    count--
                    ShoppingCart.updateItemQuantity(cartItem.dishItem.dishId, UpdateType.REMOVE)
                    notifyItemChanged(position)
                    listener.updateCount()
                    holder.view.tv_dish_quantity_rv.text = count.toString()
                } else {
                    Utility.RemoveItem(cartItem.dishItem.dishId)
                    holder.view.tv_dish_quantity_rv.text = "0"
                    notifyDataSetChanged()
                    listener.updateCount()
                }
            }
        }
    }







    private fun getDeliveryCharges(dishInfo: HomeFeed.Dish, dishItem: DishItem): Int {
        var deliveryAmount = 0

        // deliveryTypeId = 1= => Home delivery
        if (dishItem.deliveryTypeId == 1) {
            deliveryAmount = dishInfo.DeliveryCharge
        }

        return deliveryAmount
    }
    private fun getPackingCharges(dishInfo: HomeFeed.Dish, dishItem: DishItem): Int {
        var packingAmount = 0

        // packingTypeId = 2 => Parcel in disposable box
        if (dishItem.packingTypeId == 2) {
            packingAmount += (dishInfo.PackageCharge * dishItem.quantity)
        }
        return packingAmount
    }
}



class OrderDetailViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
}

