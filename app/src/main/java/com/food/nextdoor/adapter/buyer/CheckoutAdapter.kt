package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.model.Order

import kotlinx.android.synthetic.main.checkout_row.view.*


class CheckoutAdapter(val context: Context, val OrderDetails: List<Order>) : RecyclerView.Adapter<CheckoutAdapter.OrderDetailViewHolder>() {

    override fun getItemCount(): Int {
        return OrderDetails.size
      }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dishRowView = layoutInflater.inflate(com.food.nextdoor.R.layout.checkout_row, parent, false)
        return OrderDetailViewHolder(dishRowView)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val orderDetail = OrderDetails[position]
        holder.setData(orderDetail,position)
    }


    inner class OrderDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentOrder: Order? = null
        var currentPosition: Int = 0

        fun setData(orderDetail: Order?, position: Int) {
            this.currentOrder = orderDetail
            this.currentPosition = position
            //itemView.tv_dishName.text =  myModelObject!!.dish_Name
        }


        init {
            itemView.setOnClickListener {
                //Toast.makeText(context, {currentOrder!!.chef_id}  + "Clicked !" , Toast.LENGTH_SHORT).show()
            }

            itemView.tv_dish_name_rv.setOnClickListener {
                //Toast.makeText(context, currentOrder!!.chef_id + " CLicked !", Toast.LENGTH_SHORT).show()
            }
        }
    }
























}

