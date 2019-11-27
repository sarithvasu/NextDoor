package com.food.nextdoor.activity.buyer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.ReviewTagAdapter
import com.food.nextdoor.adapter.buyer.WriteReviewAdapter
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.listeners.OnItemClickListener
import com.food.nextdoor.model.OrderHistory
import com.food.nextdoor.model.ReviewTag
import com.food.nextdoor.model.post.PostReview
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_write_review.*
import kotlinx.android.synthetic.main.review_tag_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.Manager
import system.Manager.Companion.buyerInfo
import system.Utility
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

import kotlin.collections.ArrayList




class WriteReviewActivity : AppCompatActivity() {

    private var mTagname: String = ""
    private lateinit var writeAdapter: WriteReviewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_review)


        val orderHistory = intent.getParcelableExtra<OrderHistory>(Utility.WRITE_REVIEW_KEY)
        val groupId = intent.getIntExtra(Utility.GROUP_NAME_KEY,0)
        tv_date_write_review_buyer.text=Utility.standardDate( Utility.NETWORK_STANDARD_TIME,orderHistory.OrderSummary.find { s-> s.GroupId==groupId }!!.OrderDate)
        tv_chef_name_write_review_buyer.text=orderHistory.OrderSummary.find { s-> s.GroupId==groupId }!!.ChefName
        writeAdapter=WriteReviewAdapter(orderHistory.Dishes.filter { dishe -> dishe.GroupId==groupId  },orderHistory.OrderSummary.find { s-> s.GroupId==groupId }!!.ChefId)
        rv_write_review.apply {
            layoutManager=androidx.recyclerview.widget.LinearLayoutManager(this@WriteReviewActivity)
            adapter=writeAdapter
        }

        tv_chef_name_write_review_buyer.text
        ok_button_write_review_buyer.setOnClickListener {

        }
        ok_button_write_review_buyer.setOnClickListener {
            val reviewPostService =
                RetrofitInstantBuilder.buildService(RetrofitService::class.java)?.postWriteReview("application/json",
                    "application/json",writeAdapter.retriveReviews()).also {
                    it?.enqueue(object:Callback<ResponseCode>{
                        override fun onFailure(call: Call<ResponseCode>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<ResponseCode>,
                            response: Response<ResponseCode>
                        ) {
                          if(response?.body()!=null){
                              Utility.createCustomDialog(this@WriteReviewActivity,response?.body()?.Message.toString(),View.OnClickListener {
                                  finish()
                              })
                          }
                        }

                    })
                }
        }


    }





}





