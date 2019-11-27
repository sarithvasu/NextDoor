package com.food.nextdoor.activity

import com.food.nextdoor.adapter.ImageSliderAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import java.util.*
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.graphics.Point
import android.util.Log
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.HomeActivity
import com.food.nextdoor.activity.seller.SellerRegistration
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.model.DeliveryType
import com.food.nextdoor.model.PackingType
import com.food.nextdoor.model.post.PostViewed
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.model.post.SharedDish
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import system.Preference
import system.UserType
import system.Utility
import kotlin.collections.ArrayList
import androidx.core.text.HtmlCompat
import com.food.nextdoor.activity.seller.SellerDashBoard
import com.food.nextdoor.model.UserStatus
import retrofit2.Callback
import system.UserStatusHolder


class MainActivity : AppCompatActivity() {

    // Used in SetSliderTimer Method

    private var arry: ArrayList<DeliveryType> = ArrayList<DeliveryType>()
    private var arr2: ArrayList<PackingType> = ArrayList<PackingType>()
    private lateinit var mUserInfo: HashMap<String, Int>
    private var mUserId: Int = -1
    private var mUserTypeId: Int? = 0
    private var mClickedOntheSeller = false
    var currentPage = 0
    internal lateinit var timer: Timer
    val DELAY_MS: Long = 300//delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 2000 // time in milliseconds between successive task executions.

    // Major variables
    internal lateinit var adapter: androidx.viewpager.widget.PagerAdapter
    internal lateinit var sliderViewPager: androidx.viewpager.widget.ViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SetImagSlideHeight()

        adapter = ImageSliderAdapter(this)
        sliderViewPager.adapter = adapter
        mUserInfo = Preference(this).getUserBasicInfo()
        mUserId = mUserInfo[Utility.USER_ID]!!
        mUserTypeId = mUserInfo[Utility.USER_TYPE_ID]!!


        // After setting the adapter use the timer
        SetSliderTimer()
        val a = 2 or 4
        val result = 4 and a
        val sarith = "s"
        // dbSampleInsertAndQuery()

        txt_profile.setOnClickListener { start() }
    }

    override fun onResume() {
        super.onResume()
        checkSellerApporovalStatus()

    }

    private fun checkSellerApporovalStatus() {
        val sellerCheckApprovalStatus: RetrofitService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        sellerCheckApprovalStatus.getCheckApprovalstatusByUserId(
            "application/JSON",
            "application/JSON",
            mUserId
        ).also {
            it.enqueue(object : Callback<UserStatus> {
                override fun onFailure(call: Call<UserStatus>, t: Throwable) {
                    Utility.createCustomDialog(
                        this@MainActivity,
                        " Something went wrong.",true
                    )
                }

                override fun onResponse(call: Call<UserStatus>, response: Response<UserStatus>) {
                    if(response.isSuccessful&&response.body()!=null){
                        UserStatusHolder.userStatus=response.body()!!
                            updateUiChefApproval()

                    }

                }

            })
        }
    }

    override fun onPause() {
        super.onPause()
        mClickedOntheSeller=false
    }

    private fun updateUiChefApproval() {
        if (UserStatusHolder.userStatus.UserTypeId == UserType.SELLER.value && UserStatusHolder.userStatus.Status == system.UserStatus.PENDING_APPROVAL.value) {
            val text =
                "<font color=#000000>Sell Home Food</font> <br><font color=#D81B60><small>(Pending approval)</small></font>"
            txt_sell.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
            if (mClickedOntheSeller) {
                Utility.createCustomDialog(
                    this@MainActivity,
                    " Approval in progress, please wait",true
                )
            }
        } else  if (UserStatusHolder.userStatus.UserTypeId == UserType.SELLER.value && UserStatusHolder.userStatus.Status == system.UserStatus.ACTIVE.value){
            txt_sell.text = "Sell Home made Food"
            val basicInfo: HashMap<String, Int> = hashMapOf()
            basicInfo.put(Utility.USER_ID, mUserId)
            basicInfo.put(
                Utility.USER_TYPE_ID,
                UserType.SELLER.value
            )

            val preference = Preference(this@MainActivity)
            preference.saveUserBasicInfo(basicInfo)
            NextDoorDB.invoke(this@MainActivity).daoAccess.updateBuyerUserType(
                mUserId,
                UserType.SELLER.value
            )
            mUserTypeId = UserType.SELLER.value
            if (mClickedOntheSeller) {
                startActivity(Intent(this, SellerDashBoard::class.java))
            }
        }


    }

    private fun dbSampleInsertAndQuery() = Thread {
        val deliveryType = DeliveryType()
        deliveryType.deliveryTypeId = 4
        deliveryType.deliveryDescription = "delivered in home"

        val packingType = PackingType()
        packingType.packingTypeId = 2
        packingType.packingDescription = "packing in thire"

        var insertDeliveryType: Long =
            NextDoorDB.invoke(this).daoAccess.insertDeliveryType(deliveryType = deliveryType)
        var insertPackingType: Long =
            NextDoorDB.invoke(this).daoAccess.insertPackingType(packingType = packingType)

        arry = NextDoorDB.invoke(this).daoAccess.getDeliveryTypeList() as ArrayList<DeliveryType>
        arr2 = NextDoorDB.invoke(this).daoAccess.getPackingTypeList() as ArrayList<PackingType>
    }.start()


    open fun buyerOnClick(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }


    open fun sellerOnClick(view: View) {
        mClickedOntheSeller = true

        when (UserStatusHolder.userStatus.UserTypeId) {
            UserType.SELLER.value -> updateUiChefApproval()
            UserType.BUYER.value -> startActivity(Intent(this, SellerRegistration::class.java))
        }
    }


    // <editor-fold desc="Helper Method for Slider Timer and dynamic Height">

    // Dynamically setting the height of Image slider to 35 %
    private fun SetImagSlideHeight() {
        sliderViewPager = findViewById(R.id.SliderViewPager)
        var constraintLayout: ConstraintLayout


        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Log.e("Width", "" + width)
        Log.e("height", "" + height)

        val lp = sliderViewPager.getLayoutParams() as ConstraintLayout.LayoutParams
        lp.height = (height * 35) / 100
        sliderViewPager.setLayoutParams(lp);
    }

    // Setting the slider scroll speed to 2 seconds
    private fun SetSliderTimer() {
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === Utility.getImageSliderCount()) {
                currentPage = 0
            }
            sliderViewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread
        timer.schedule(object : TimerTask() { // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    //</editor-fold>

    private fun start() {
        //startActivity(Intent(this, RegistrationActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        val postVieweService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        postVieweService?.postAddNewViewed(
            "application/json",
            "application/json", getDishViewed()
        ).also {
            it?.enqueue(object : retrofit2.Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    val sarith = "sarith_vasu"
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    if (response.isSuccessful) {
                        val sarith = "sarithvasu"
                        NextDoorDB.invoke(this@MainActivity).daoAccess.deleteViewed()

                    }
                }

            })
        }
        callPostShared()
    }

    private fun callPostShared() {
        val postVieweService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        postVieweService?.postAddNewShared(
            "application/json",
            "application/json", getDishShared()
        ).also {
            it?.enqueue(object : retrofit2.Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    if (response.isSuccessful) {
                        val sarith = "sarithvasu"
                        NextDoorDB.invoke(this@MainActivity).daoAccess.deleteSharedDish()

                    }
                }

            })
        }

    }

    private fun getDishShared(): ArrayList<SharedDish?> {
        return NextDoorDB.invoke(this).daoAccess.getSharedDishList() as ArrayList<SharedDish?>
    }

    private fun getDishViewed(): ArrayList<PostViewed?> {
        return NextDoorDB.invoke(this).daoAccess.getDishViewList() as ArrayList<PostViewed?>
    }
}
