package com.food.nextdoor.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.TimeSlotActivity
import com.food.nextdoor.adapter.buyer.DishDetailAdapter
import com.food.nextdoor.listeners.OnItemCountListener
import com.food.nextdoor.model.HomeFeed
import kotlinx.android.synthetic.main.add_remove_layout.view.*
import system.*


class AddCartView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.add_remove_layout, this, true)
        orientation = VERTICAL
    }

    fun configDishItem(
        dishInfo: HomeFeed.Dish,
        listener: OnItemCountListener
    ) {
        val cartItem: CartItem? = ShoppingCart.getCartItems().find { s -> s.dishItem.dishId == dishInfo.DishId }
        if (cartItem != null && cartItem.dishItem.quantity > 0) {
            btn_lay_detail.visibility = View.VISIBLE
            btn_buy_detail.visibility = View.GONE
            tv_quantity_detail.text = cartItem.dishItem.quantity.toString()
        } else {
            btn_lay_detail.visibility = View.GONE
            btn_buy_detail.visibility = View.VISIBLE
        }
        btn_buy_detail.setOnClickListener {
            if(dishInfo.ChefId != Preference(context).getUserChefId()) {
                val intent = Intent(context, TimeSlotActivity::class.java)
                intent.putExtra(Utility.DISH_ID_KEY, dishInfo.DishId as Int)
                intent.putExtra(Utility.CHEF_ID_KEY, dishInfo.ChefId as Int)
                (context as Activity).startActivityForResult(
                    intent,
                    DishDetailAdapter.REQUEST_CODE
                )
            }else{
                Utility.createCustomDialog(context, "Your trying to buy your own item." ,true)
            }
        }


        tv_plus_detail.setOnClickListener {
            var count = tv_quantity_detail.text.toString().toInt()

            if (count < 99) {

                if (count == 0) {
                    Utility.AddItem(dishInfo)
                } else if (count > 0) {

                    ShoppingCart.updateItemQuantity(dishInfo.DishId, UpdateType.ADD)
                }
                listener.updateCount()
                count++

                tv_quantity_detail.text = count.toString()
            }
        }


        tv_minus_detail.setOnClickListener {
            var count = tv_quantity_detail.text.toString().toInt()

            if (count > 1) {
                count--
                ShoppingCart.updateItemQuantity(dishInfo.DishId, UpdateType.REMOVE)


                tv_quantity_detail.text = count.toString()
            } else {
                Utility.RemoveItem(dishInfo.DishId)

                btn_lay_detail.visibility = View.GONE
                btn_buy_detail.visibility = View.VISIBLE
            }
            listener.updateCount()
        }

    }

}