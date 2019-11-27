package com.food.nextdoor.activity.buyer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.MyOrderAdapter
import com.food.nextdoor.model.MyOrderFeed
import com.food.nextdoor.model.OrderHistory

import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.my_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.Preference



class MyOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_order)

        rv_my_order.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

//        this.drawRowDivider()
       // this.readFromAsset("my_orders_feed.json")

        callOrderHistoryApi()




    }

    private fun callOrderHistoryApi() {
        val orderHistoryService
                = RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        orderHistoryService?.getOrderHistoryByBuyerId("application/json",
            "application/json", Preference(this).getUserId()).also {
            it?.enqueue(object : Callback<OrderHistory> {
                override fun onFailure(call: Call<OrderHistory>, t: Throwable) {

                }
                override fun onResponse(
                    call: Call<OrderHistory>,
                    response: Response<OrderHistory>
                ) {
                    rv_my_order.apply {
                        rv_my_order.adapter = MyOrderAdapter(this@MyOrderActivity,response.body()!!)
                    }
                }

            })
        }
    }


//    private fun drawRowDivider() {
//        val itemDecorator = androidx.recyclerview.widget.DividerItemDecoration(
//            rv_my_order.getContext(),
//            androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
//        )
//        itemDecorator.setDrawable(
//            ContextCompat.getDrawable(
//                rv_my_order.getContext(),
//                R.drawable.divider
//            )!!
//        )
//        rv_my_order.addItemDecoration(itemDecorator)
//    }

    private fun readFromAsset(fileName: String) {
        val json_string = application.assets.open(fileName).bufferedReader().use{
            it.readText()
        }

        val gson = GsonBuilder().serializeNulls().create()
        val myOrderFeed =  gson.fromJson(json_string, MyOrderFeed::class.java)

    /*    rv_my_order.apply {
            rv_my_order.adapter = MyOrderAdapter(this@MyOrderActivity,myOrderFeed)
        }*/

    }
}
