package com.food.nextdoor.activity.buyer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.ReadReviewAdapter
import com.food.nextdoor.model.ReadReviewNetwork
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import kotlinx.android.synthetic.main.read_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.Utility

class ReadReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.read_review)

        // val readReviewFeed = this.readFromAsset("read_review_feed.json")
        val dishId = intent.getIntExtra(Utility.DISH_ID_KEY, 0)
        getReadReviwList(dishId)
        this.configureReadReviewRecycleView()
//        this.drawRowDivider()
    }

    private fun getReadReviwList(dishId: Int) {
        val readReviewService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        readReviewService?.getReviewByDishId(
            "application/json",
            "application/json", dishId
        ).also {
            it?.enqueue(object : Callback<ArrayList<ReadReviewNetwork>> {
                override fun onFailure(call: Call<ArrayList<ReadReviewNetwork>>, t: Throwable) {
val sarith= "sarith"
                }

                override fun onResponse(
                    call: Call<ArrayList<ReadReviewNetwork>>,
                    response: Response<ArrayList<ReadReviewNetwork>>
                ) {
                    if (response?.body() != null) {
                        review_recycleview.apply {
                            review_recycleview.adapter =
                                ReadReviewAdapter(this@ReadReviewActivity, response?.body()!!)
                        }
                    }
                }

            })
        }
    }


    private fun configureReadReviewRecycleView() {
        review_recycleview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.RecyclerView.VERTICAL,
            false
        )

    }

//    private fun drawRowDivider() {
//        val itemDecorator = androidx.recyclerview.widget.DividerItemDecoration(
//            review_recycleview.getContext(),
//            androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
//        )
//        itemDecorator.setDrawable(
//            ContextCompat.getDrawable(
//                review_recycleview.getContext(),
//                R.drawable.divider
//            )!!
//        )
//        review_recycleview.addItemDecoration(itemDecorator)
//    }


}
