package com.food.nextdoor.activity.buyer

import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.adapter.buyer.HomeAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.WindowManager
import com.food.nextdoor.R
import com.food.nextdoor.model.OrderDetail
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.home.*
import okhttp3.*
import system.Utility
import java.io.IOException





class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home)
        //recyclerView_home_buyer.setBackgroundColor(Color.BLUE)



        recyclerView_home_buyer.layoutManager = LinearLayoutManager(this)
        //recyclerView_home_buyer.adapter =  HomeAdapter() // Soumen Instead Assigned in readJsondata Method

       // Draw line Divider between two rows in recyclerview
        drawRowDivider()
       //fetchJsonFromServer()
        readfromAsset()
        //fetchJsonFromServerUsingRefrofit()




    }



    private fun readfromAsset() {
        val json_string = application.assets.open("home_json.json").bufferedReader().use{
            it.readText()
        }

        val gson = GsonBuilder().serializeNulls().create()
        val homeFeed =  gson.fromJson(json_string, HomeFeed::class.java)


        // Soumen: Bring the control back to Ui Thread
        runOnUiThread {
            Utility.DataHolder.homeFeedInstance = homeFeed
            val buyerHomeFeed = Utility.DataHolder.homeFeedInstance

            recyclerView_home_buyer.apply {
                recyclerView_home_buyer.adapter = HomeAdapter(buyerHomeFeed)
            }
        }
    }

    private fun fetchJsonFromServerUsingRefrofit() {
        val buyerHomeScreenService: RetrofitService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        val requestCall: retrofit2.Call<HomeFeed> = buyerHomeScreenService.getBuyerHomeData()
        requestCall.enqueue(object: retrofit2.Callback<HomeFeed> {
            override fun onFailure(call: retrofit2.Call<HomeFeed>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: retrofit2.Call<HomeFeed>, response: retrofit2.Response<HomeFeed>) {
                if(response.isSuccessful){
                    Log.e("","xc';fd"+response.body())
                    val dataFeeder : HomeFeed = response.body()!!
                    recyclerView_home_buyer.adapter = HomeAdapter(dataFeeder)
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
                //val gson = GsonBuilder().create()

                val gson = GsonBuilder().serializeNulls().create()
                val homeFeed =  gson.fromJson(body, HomeFeed::class.java)


                // Soumen: Bring the control back to Ui Thread
                runOnUiThread {
                   Utility.DataHolder.homeFeedInstance = homeFeed
                    val buyerHomeFeed = Utility.DataHolder.homeFeedInstance

                    recyclerView_home_buyer.apply {
                        recyclerView_home_buyer.adapter = HomeAdapter(buyerHomeFeed)
                    }
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

    override fun onResume() {
        super.onResume()
        if(Utility.DataHolder.homeFeedInstance!=null) {
            recyclerView_home_buyer.apply {
                recyclerView_home_buyer.adapter = HomeAdapter(Utility.DataHolder.homeFeedInstance)
            }
        }

    }




//    private fun testMap(){
//        val myMap: Map<Int,String> = mapOf<Int, String>()
//
//        for(p in myMap.plus(Pair(5, "Rohan"))){
//            println("Element at key ${p.key} = ${p.value}")
//        }
//
//
//        myMap.plus(Pair(6, "Soumen"))
//
//
//    }


}

//class Singleton private constructor(){
//
//    companion object {
//        private var INSTANCE: Singleton ? = null
//
//        fun  getInstance(): Singleton {
//            synchronized(this) {
//                if(INSTANCE == null){
//                    INSTANCE = Singleton()
//                }
//                return INSTANCE!!
//            }
//        }
//    }
//
//}
//
//class Singleton1 private constructor(){
//    companion object {
//        @Volatile private var INSTANCE: Singleton ? = null
//        fun  getInstance(): Singleton {
//            return INSTANCE?: synchronized(this){
//                Singleton().also {
//                    INSTANCE = it
//                }
//            }
//        }
//    }
//}
//
//
//class SomeSingleton private constructor() {
//
//    init {
//        INSTANCE = this
//        println("init complete")
//    }
//
//    companion object {
//        var INSTANCE: SomeSingleton
//
//        init {
//            SomeSingleton()
//        }
//    }
//}