package com.food.nextdoor.activity.buyer

import com.food.nextdoor.model.BuyerHomeFeed
import com.food.nextdoor.adapter.buyer.BuyerHomeAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.food.nextdoor.R
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.home.*
import okhttp3.*
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        //recyclerView_home_buyer.setBackgroundColor(Color.BLUE)

        recyclerView_home_buyer.layoutManager = LinearLayoutManager(this)
        //recyclerView_home_buyer.adapter =  BuyerHomeAdapter() // Soumen Instead Assigned in readJsondata Method

       // Draw line Divider between two rows in recyclerview
        drawRowDivider()
       fetchJsonFromServer()
        //fetchJsonFromServerUsingRefrofit()
    }

    private fun fetchJsonFromServerUsingRefrofit() {


        val buyerHomeScreenService: RetrofitService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        val requestCall: retrofit2.Call<BuyerHomeFeed> = buyerHomeScreenService.getBuyerHomeData()
        requestCall.enqueue(object: retrofit2.Callback<BuyerHomeFeed> {
            override fun onFailure(call: retrofit2.Call<BuyerHomeFeed>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: retrofit2.Call<BuyerHomeFeed>, response: retrofit2.Response<BuyerHomeFeed>) {
                if(response.isSuccessful){
                    Log.e("","xc';fd"+response.body())
                    val buyerDataFeeder : BuyerHomeFeed = response.body()!!
                    recyclerView_home_buyer.adapter = BuyerHomeAdapter(buyerDataFeeder)


                }
            }

        })

    }


    fun fetchJsonFromServer() {
        println("Attemptying to fetch JSON")
        val strUrl = "https://60ced595-ba5a-4f3c-859d-447cda1bc7a6.mock.pstmn.io/home"
        val request = Request.Builder().url(strUrl).build()

        val client = OkHttpClient()
        // Soumen: enqueue Means running on background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val  body = response?.body()?.string()
                println("Successfully executed request")
                val gson = GsonBuilder().create()
                val buyerHomeFeed =  gson.fromJson(body, BuyerHomeFeed::class.java)

                // Soumen: Bring the control back to Ui Thread
                runOnUiThread {
                    recyclerView_home_buyer.adapter = BuyerHomeAdapter(buyerHomeFeed)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }


    // <editor-fold desc="Helper Method for Cell Divider">
    private fun drawRowDivider() {
        val itemDecorator = DividerItemDecoration(recyclerView_home_buyer.getContext(), DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                recyclerView_home_buyer.getContext(),
                R.drawable.divider
            )!!
        )
        recyclerView_home_buyer.addItemDecoration(itemDecorator)
    }
    //</editor-fold>

}
