package com.food.nextdoor.activity.seller

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.food.nextdoor.R
import com.food.nextdoor.adapter.ViewedSharedAdapter
import com.food.nextdoor.model.ViewedShared
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import kotlinx.android.synthetic.main.seller_user_activities.*
import retrofit2.Call
import retrofit2.Response
import system.Utility

class SellerUserActivity : AppCompatActivity() {
    private var dishId=0
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_user_activities)
         dishId = intent.getIntExtra(Utility.DISH_ID_KEY, 0)
        getViewedList()
        textView80.setOnClickListener{
            getViewedList()
            it.setBackgroundResource(R.color.White)
            (it as TextView).setTextColor(
                ContextCompat.getColor(
                    it.context,
                    R.color.Black
                ))
            textView115.setBackgroundResource(R.color.DarkBlue)
            textView115.setTextColor(
                ContextCompat.getColor(
                    it.context,
                    R.color.White
                ))
        }
        textView115.setOnClickListener{
            getSharedList()
            it.setBackgroundResource(R.color.White)
            (it as TextView).setTextColor(
                ContextCompat.getColor(
                    it.context,
                    R.color.Black
                ))
            textView80.setBackgroundResource(R.color.DarkBlue)
            textView80.setTextColor(
                ContextCompat.getColor(
                    it.context,
                    R.color.White
                ))
        }
    }
/* dishID haerded coded ...uncomment the dish Id while */
    private fun getSharedList() {
        val viewedListService=RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        viewedListService?.getSharedFeedByDishIdFull(  "application/json",
            "application/json",dishId).also {
            it?.enqueue(object: retrofit2.Callback<ArrayList<ViewedShared>>{
                override fun onFailure(call: Call<ArrayList<ViewedShared>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<ViewedShared>>,
                    response: Response<ArrayList<ViewedShared>>
                ) {
                    if(response.body()!=null) {
                        textView115.text= "Shared (${response.body()!!.size})"
                        rv_viewedAndShare.apply {
                            layoutManager = LinearLayoutManager(this@SellerUserActivity)
                            adapter = ViewedSharedAdapter(response.body()!!)

                        }
                    }
                }

            })
        }
    }
    /* dishID haerded coded ...uncomment the dish Id while */
    private fun getViewedList() {
        val viewedListService=RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        viewedListService?.getViewedFeedByDishId(  "application/json",
            "application/json",dishId).also {
            it?.enqueue(object: retrofit2.Callback<ArrayList<ViewedShared>>{
                override fun onFailure(call: Call<ArrayList<ViewedShared>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<ViewedShared>>,
                    response: Response<ArrayList<ViewedShared>>
                ) {
                    if(response.body()!=null) {
                        textView80.text= "Viewed (${response.body()!!.size})"
                        rv_viewedAndShare.apply {
                            layoutManager = LinearLayoutManager(this@SellerUserActivity)
                            adapter = ViewedSharedAdapter(response.body()!!)

                        }
                    }
                }

            })
        }
    }
}
