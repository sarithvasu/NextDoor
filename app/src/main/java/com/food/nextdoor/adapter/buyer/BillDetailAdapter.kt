package com.food.nextdoor.adapter.buyer

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.model.CheckoutBillSummery
import kotlinx.android.synthetic.main.bill_detail_row.view.*


class BillDetailAdapter (val context: Context, var checkoutBillSummery: ArrayList<CheckoutBillSummery>) : androidx.recyclerview.widget.RecyclerView.Adapter<BillDetailViewHolder>() {

    override fun getItemCount(): Int {
        var size = 0
        if (checkoutBillSummery.size > 0) {
            size = checkoutBillSummery.size
        }

        return size
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BillDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowObject = layoutInflater.inflate(com.food.nextdoor.R.layout.bill_detail_row, parent, false)
        return BillDetailViewHolder(rowObject)
    }

    override fun onBindViewHolder(holder: BillDetailViewHolder, position: Int) {
        if (checkoutBillSummery.size > 0) {
            val checkoutBillDetail = checkoutBillSummery.get(position)
            holder.itemView.bill_detail_item_checkout_row.text = checkoutBillDetail.name
            holder.itemView.bill_detail_item_checkout_row_price.text = checkoutBillDetail.price.toString()
        }
    }

}


 class BillDetailViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
}