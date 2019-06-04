package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.BuyerHomeAdapter
import com.food.nextdoor.adapter.buyer.SignatureDishAdopter
import com.food.nextdoor.model.BuyerHomeFeed
import com.food.nextdoor.model.ChefProfile
import com.food.nextdoor.model.ChefProfileFeed
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.chef_profile.*
import kotlinx.android.synthetic.main.home.*
import okhttp3.*
import system.Utility
import java.io.IOException

class ChefProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chef_profile)

        val chefId = intent.getIntExtra(Utility.CHEF_ID_KEY,0)
        supportActionBar?.title = chefId.toString()
        recyclerView_signature_dishes.layoutManager=LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)


        // https://13bc56b9-2477-403c-b115-8c7e79545518.mock.pstmn.io/profileById?chefId=1
        fetchJsonFromServer1()

    }



    fun fetchJsonFromServer1() {
        println("Attemptying to fetch JSON")
        val strUrl = "https://13bc56b9-2477-403c-b115-8c7e79545518.mock.pstmn.io/profileById?chefId=1"
        val request = Request.Builder().url(strUrl).build()

        val client = OkHttpClient()
        // Soumen: enqueue Means running on background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val  body = response?.body()?.string()
                println("Successfully executed request")
                val gson = GsonBuilder().create()
                val chefProfile =  gson.fromJson(body, ChefProfileFeed::class.java)

                // Soumen: Bring the control back to Ui Thread
                runOnUiThread {
                    recyclerView_signature_dishes.adapter = SignatureDishAdopter(chefProfile)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }




    fun loadCheckOut(view: View){
        startActivity(Intent(this, CheckoutActivity::class.java))
    }
}
