package com.food.nextdoor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.food.nextdoor.R
import com.food.nextdoor.model.ViewedShared
import kotlinx.android.synthetic.main.seller_user_activities_row.view.*
import system.Utility

class ViewedSharedAdapter(var datas: ArrayList<ViewedShared>) : RecyclerView.Adapter<ViewedSharedViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewedSharedViewHolder {
        val layOutInflater = LayoutInflater.from(parent.context)
        val view= layOutInflater.inflate(R.layout.seller_user_activities_row,parent,false)
        return ViewedSharedViewHolder(view)
    }

    override fun getItemCount(): Int {
       return datas.size
    }

    override fun onBindViewHolder(holder: ViewedSharedViewHolder, position: Int) {

        holder.itemView.tv_viewed_dish_slr_user_activitu_row.text=datas[position].DishName
        holder.itemView.tv_buyer_mobile_number_slr_user_activitu_row.text=datas[position].MobileNumber

        if(datas[position].FullName!=null) {
            holder.itemView.tv_buyer_name_with_flat_number_slr_user_activitu_row.text =
                "${datas[position].FullName}, ${datas[position].FlatNumber}"
            holder.itemView.tv_viewed_start_end_time_slr_user_activitu_row.text =
                "${Utility.standardDateToTimeSlotForViewed(datas[position].ViewedStartTime.toString().trim())} - ${Utility.standardDateToTimeSlotForViewed(
                    datas[position].ViewedEndTime
                )}"
            holder.itemView.tv_viewed_duration_slr_user_activitu_row.text =
                datas[position].DurationInSeconds.toString()
        }else{
            holder.itemView.tv_buyer_name_with_flat_number_slr_user_activitu_row.text =
                "${datas[position].UserName}, ${datas[position].FlatNumber}"
            holder.itemView.tv_viewed_start_end_time_slr_user_activitu_row.text =
                "${datas[position].SharedVia}"
            holder.itemView.tv_viewed_duration_slr_user_activitu_row.text =
                ""
        }


    }
}
class ViewedSharedViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)