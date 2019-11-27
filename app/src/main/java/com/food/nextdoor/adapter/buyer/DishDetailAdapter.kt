package com.food.nextdoor.adapter.buyer
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.activity.buyer.DishDetailActivity
import com.food.nextdoor.activity.buyer.TimeSlotActivity
import com.food.nextdoor.listeners.OnItemCountListener
import com.food.nextdoor.listeners.YouMayLikeSelectListener
import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dish_detail_row.view.*
import system.CartItem
import system.ShoppingCart
import system.UpdateType
import system.Utility
import kotlin.collections.ArrayList


class DishDetailAdapter(val cotext: Context, val homeFeed: HomeFeed?, val mFiveDishes: List<HomeFeed.Dish>) : androidx.recyclerview.widget.RecyclerView.Adapter<DishDetailViewHolder>() {
    private lateinit var listener: OnItemCountListener
    //private lateinit var mFiveDishes:List<HomeFeed.Dish>
    private var mShuffledDishes: ArrayList<HomeFeed.Dish> =   ArrayList<HomeFeed.Dish>()
    private lateinit var m_listOfCartItem :ArrayList<CartItem>

    companion object {
        val REQUEST_CODE:Int=100
    }

    override fun getItemCount(): Int {
        //mFiveDishes = homeFeed!!.signatureDishes.take(5)
        return mFiveDishes.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DishDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        m_listOfCartItem = ShoppingCart.getCartItems()
        this.listener= cotext as DishDetailActivity
        val dishRowObject = layoutInflater.inflate(com.food.nextdoor.R.layout.dish_detail_row, parent, false)
        return DishDetailViewHolder(dishRowObject)
    }


    override fun onBindViewHolder(holder: DishDetailViewHolder, position: Int) {
        val dishInfo: HomeFeed.Dish = mFiveDishes.get(position)

        val dishClickListener: YouMayLikeSelectListener = holder.view.context as DishDetailActivity


        holder.view.setOnClickListener {
            dishClickListener.onDishRowClick(dishInfo)
        }

        // set Dish Symbol
        val dishSymbol =   Utility.setDishSymbol(dishInfo.DishType,holder.view.context)
        holder.view.img_dish_symbol_detail_rv.setImageDrawable(dishSymbol)

        holder.view.tv_dish_name_detail_main_rv.text = dishInfo.DishName
        holder.view.tv_serving_per_person_detail_rv.text = (dishInfo.ServingsPerPlate.toString())
        holder.view.tv_dish_price_detail_rv.text =  " Rs. " +(dishInfo.UnitPrice.toString())

        //Bind Chef Name and Flat Number
        val chefinfo: HomeFeed.Chef = homeFeed!!.chefs.filter { s-> dishInfo.ChefId==s.ChefId}.single()
        holder.view.tv_chef_name_with_flat_number_detail_rv.text = chefinfo.FullName + " | " + chefinfo.FlatNumber

        // Bind Dish Image
        Picasso.with(holder.view.context).load(mFiveDishes.get(position).DishImageUrl).into(holder.view.img_dish_image_detail_rv)


        //mplementBuyFunctionality(dishInfo, holder)


    }

    private fun mplementBuyFunctionality(
        dishInfo: HomeFeed.Dish,
        holder: DishDetailViewHolder
    ) {
        // Disable buy button and enable (+) and (-) button If the item is already added to the cart
        if (m_listOfCartItem.any { x -> x.dishItem.dishId == dishInfo.DishId }) {
            // val cartItem: CartItem = m_listOfCartItem.filter { s-> dishInfo.DishId==s.dishItem.dishId}.single()
            //val cartItem: CartItem = m_listOfCartItem.filter { s-> s.dishItem.dishId == dishInfo.DishId}.single()

            val cartItem: CartItem? = m_listOfCartItem.find { s -> s.dishItem.dishId == dishInfo.DishId }
            if (cartItem == null) {
                // Soumen Throw exception
            }

            if (cartItem!!.dishItem.quantity > 0) {
                holder.view.btn_lay_detail_row.visibility = View.VISIBLE
                holder.view.btn_buy_detail_row.visibility = View.GONE
                holder.view.tv_quantity_detail_row.text = cartItem.dishItem.quantity.toString()
            }
        }


        holder.view.btn_buy_detail_row.setOnClickListener {
            val intent = Intent(holder.view.btn_buy_detail_row.context, TimeSlotActivity::class.java)
            intent.putExtra(Utility.DISH_ID_KEY, dishInfo.DishId as Int)
            intent.putExtra(Utility.CHEF_ID_KEY, dishInfo.ChefId as Int)
            (holder.view.btn_buy_detail_row.context as DishDetailActivity).startActivityForResult(intent, REQUEST_CODE)
        }


        holder.view.tv_plus_detail_row.setOnClickListener {
            var count = holder.view.tv_quantity_detail_row.text.toString().toInt()

            if (count < 99) {

                if (count == 0) {
                    Utility.AddItem(dishInfo)
                } else if (count > 0) {
                    ShoppingCart.updateItemQuantity(dishInfo.DishId, UpdateType.ADD)
                }

                count++
                listener.updateCount()
                holder.view.tv_quantity_detail_row.text = count.toString()
            }
        }


        holder.view.tv_minus_detail_row.setOnClickListener {
            var count = holder.view.tv_quantity_detail_row.text.toString().toInt()

            if (count > 1) {
                count--
                ShoppingCart.updateItemQuantity(dishInfo.DishId, UpdateType.REMOVE)
                listener.updateCount()
                holder.view.tv_quantity_detail_row.text = count.toString()
            } else {
                Utility.RemoveItem(dishInfo.DishId)
                listener.updateCount()
                holder.view.btn_lay_detail_row.visibility = View.GONE
                holder.view.btn_buy_detail_row.visibility = View.VISIBLE
            }
        }
    }


}


class DishDetailViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
}