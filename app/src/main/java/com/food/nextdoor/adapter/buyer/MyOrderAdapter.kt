package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.activity.buyer.ReadReviewActivity
import com.food.nextdoor.activity.buyer.WriteReviewActivity
import com.food.nextdoor.model.MyOrderFeed
import com.food.nextdoor.model.MyOrder
import com.food.nextdoor.model.OrderHistory
import com.food.nextdoor.model.OrderSummary
import system.Utility
import kotlinx.android.synthetic.main.my_order_row.view.*

class MyOrderAdapter(val cotext: Context, val myOrderFeed: OrderHistory) : androidx.recyclerview.widget.RecyclerView.Adapter<MyOrderFeedViewHolder>(){

    override fun getItemCount(): Int {
        return myOrderFeed.OrderSummary.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyOrderFeedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowObject = layoutInflater.inflate(com.food.nextdoor.R.layout.my_order_row, parent, false)
        return MyOrderFeedViewHolder(rowObject)
    }

    override fun onBindViewHolder(holder: MyOrderFeedViewHolder, position: Int) {
        val myOrder = myOrderFeed.OrderSummary[position]
        holder.setData(myOrder,position)

        // set On Click Listener for Write Review buttot
        holder.view.tv_write_review_history_row.setOnClickListener {
                val intent = Intent(holder.view.tv_write_review_history_row.context, WriteReviewActivity:: class.java)
                intent.putExtra(Utility.WRITE_REVIEW_KEY, myOrderFeed )
                intent.putExtra(Utility.GROUP_NAME_KEY, myOrder.GroupId)
                holder.view.tv_write_review_history_row.context.startActivity(intent)
            }



    }


}



class MyOrderFeedViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    fun setData(myOrder: OrderSummary, position: Int) {
        // set Dish Symbol
//        val dishSymbol = Utility.setDishSymbol(myOrder.DishType, view.img_dish_type_history_row.context)
//        view.img_dish_type_history_row.setImageDrawable(dishSymbol)

       // view.tv_order_id_history_row.text = myOrder.Order
        view.tv_order_date_and_time_history_row.text = Utility.standardDate( Utility.NETWORK_STANDARD_TIME,myOrder.OrderDate)
       // view.tv_dish_name_history_row.text= myOrder.dish_name
       // view.tv_unit_price_history_row.text= myOrder.unit_price.toString()

        view.textView31.text= myOrder.ItemCount.toString()+" Item/s"
        view.tv_dish_total_price_history_row.text= myOrder.OrderTotal.toString()
        view.tv_chef_name_history_row.text= myOrder.ChefName
        view.tv_chef_flat_number_history_row.text= myOrder.ChefFlatNumber
        view.tv_order_status_history_row.text= myOrder.OrderStatus
        view.ratingBar_history_row.rating= myOrder.DishRating.toFloat()
        view.tv_payment_mode_history_row.text=myOrder.PaymentMode
        view.tv_order_id_history_row.text=myOrder.GroupId.toString()





        // It’s already reviewed, so you can  read it
     /*   if ( myOrder.review_id != -1) {
            view.tv_write_review_history_row.text = "Read Review"
        }else if( myOrder.review_id == -1) {
            view.tv_write_review_history_row.text = "Write Review"
        }*/
    }

}