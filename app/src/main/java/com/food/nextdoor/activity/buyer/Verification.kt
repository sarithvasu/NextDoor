package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.food.nextdoor.R
import com.food.nextdoor.activity.LaunchActivity
import com.food.nextdoor.activity.MainActivity
import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_verification.*
import okhttp3.*
import system.Preference
import system.Utility
import system.Validation
import java.io.IOException

class Verification : AppCompatActivity() {

    // private lateinit var launcherApartment : LauncherApartment
    val validation: Validation = Validation()
    private lateinit var mUserInfo: HashMap<String, Int>
    private var mUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mUserInfo = Preference(this).getUserBasicInfo()
        mUserId = mUserInfo[Utility.USER_ID]!!


        // Registered user
        if (mUserId > 0) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_verification)
          // callHomeFeed()

           //fetchJsonFromServer()
            // launcherApartment = intent.getParcelableExtra("apartment")
            tv_next_activity_verification.setOnClickListener {
                if (validation.isValidPhone(tv_mobile_number_activity_verification.text.toString())) {
                    /* Just for bye-pass */
/*
                    var intent1 = Intent(this, OTPActivity::class.java)
                    intent1.putExtra(
                        "phonenumber",
                        tv_mobile_number_activity_verification.text.toString().trim()
                    )
                    //intent1.putExtra("apartment",launcherApartment)
                    startActivity(intent1)
                    finish()*/
                    val intent1 =
                        Intent(this@Verification, LaunchActivity::class.java)

                    intent1.putExtra("phone", tv_mobile_number_activity_verification.text.toString().trim());
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    startActivity(intent1)
                    finish()

                } else {
                    Utility.createCustomDialog(this,
                        "Invalid phone number",true)
                }
            }

        }
    }
    private fun callHomeFeed() {
        val buyerHomeScreenService: RetrofitService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        buyerHomeScreenService.getBuyerHomeData(
            "application/json",
            "application/json",1
        ).also {
            it.enqueue(object : retrofit2.Callback<HomeFeed> {
                override fun onFailure(call: retrofit2.Call<HomeFeed>, t: Throwable) {
                    val s = "sdadd"


                }

                override fun onResponse(
                    call: retrofit2.Call<HomeFeed>,
                    response: retrofit2.Response<HomeFeed>
                ) {
                    if (response.isSuccessful) {

                    }
                }
            })
        }
    }
    fun fetchJsonFromServer() {
        println("Attemptying to fetch JSON")
        val strUrl = "http://192.168.2.186:8080/api/Feed/GetHomeFeedByApartmentId?ApartmentId=2"
        val request = Request.Builder().url(strUrl).build()

        val client = OkHttpClient()
        // Soumen: enqueue Means running on background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val s = "sdadd"
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println("Successfully executed request")
                //val gson = GsonBuilder().create()

                val gson = GsonBuilder().serializeNulls().create()
                val homeFeed = gson.fromJson(body, HomeFeed::class.java)
            }})

        // Soumen: Bring the control back to Ui Thread
    }
}
