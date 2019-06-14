package system

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.WindowManager
import com.food.nextdoor.R
import com.food.nextdoor.model.CartItem

import com.food.nextdoor.model.HomeFeed
import java.util.*


enum class UpdateType {
    ADD, REMOVE
}



class Utility {
















    class Manager{

        private constructor() {
            println("Instance Created")
        }

        companion object{
            val instance: Manager by lazy {Manager()}
        }

        var thisBuyerInfo: BuyerInfo
            get() = BuyerInfo().buyerInfo
            set(value) {
                BuyerInfo().buyerInfo = value
            }


//        fun saveBuyerInfo(buyerInfo: BuyerInfo) {
//            BuyerInfo().buyerInfo = buyerInfo
//        }
//
//        fun getBuyerInfo(): BuyerInfo{
//            return BuyerInfo().buyerInfo
//        }

        class BuyerInfo () {

            internal lateinit var buyerInfo: BuyerInfo


            var userid: Int? = null
            var apartmentId: Int? = null
            var userName: String? = null
            var flatNumber: String? = null
            var mobileNumber: String? = null
            var email: String? = null
            var gender: String? = null
            var profileImageUrl: String? = null
            var isActive: Boolean = true
            var dateInsertion: String? = null
            var dateRevision: String? = null

            // :this() pointing to empty constractor
            constructor (
                userid: Int,
                apartmentId: Int,
                userName: String,
                flatNumber: String,
                mobileNumber: String,
                email: String,
                gender: String,
                profileImageUrl: String,
                isActive: Boolean,
                dateInsertion: String,
                dateRevision: String
            ) : this() {
                this.userid = userid
                this.apartmentId = apartmentId
                this.userName = userName
                this.flatNumber = flatNumber
                this.mobileNumber = mobileNumber
                this.email = email
                this.gender = gender
                this.profileImageUrl = profileImageUrl
                this.isActive = isActive
                this.dateInsertion = dateInsertion
                this.dateRevision = dateRevision
            }


            private fun saveBuyerInfo(buyerInfo: BuyerInfo) {
                this.buyerInfo = buyerInfo
            }

            private fun getBuyerInfo(): BuyerInfo{
                return this.buyerInfo
            }
        }

    }




    class DataHolder {

        companion object
        {
            init {
                println("Singleton class invoked.")
            }

            var homeFeedInstance: HomeFeed? = null

            private fun printName() : HomeFeed?
            {return  homeFeedInstance}
        }
        init {
            println("Class init method.")
        }
    }



    var currentPage = 0
    internal lateinit var timer: Timer
    val DELAY_MS: Long = 300//delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 2000 // time in milliseconds between successive task executions.




    companion object {

        val myMap: Map<String,MutableList<CartItem>> = mapOf<String, MutableList<CartItem>>()

        var IS_INITIALIZE = false


        val BUYER_HOME_FEED_KEY = "BUYER_HOME_FEED"
        val DISH_ID_KEY = "DISH_ID"
        val CHEF_ID_KEY = "CHEF_ID"



         fun setDishSymbol(dishType: String, context: Context) : Drawable?{
            var drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.nonveg_symbol)
            if (dishType == "Veg") {
                drawable = ContextCompat.getDrawable(context, R.drawable.veg_symbol)
            }
            return drawable
        }



        private val arrSliderImages =  arrayOf(R.drawable.kadai_panner, R.drawable.egg_biryani, R.drawable.roti_new, R.drawable.tandoori_new)


        fun getImageSliderCount (): Int {
            return arrSliderImages.count()
        }




        fun SetImageSliderHeight (windowManager: WindowManager,sliderviewPager: ViewPager) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val height = size.y
            Log.e("Width", "" + width)
            Log.e("height", "" + height)
            val lp = sliderviewPager.getLayoutParams() as ConstraintLayout.LayoutParams
            lp.height = (height * 55) / 100 // set to 35% of total screen height
            sliderviewPager.setLayoutParams(lp);
        }



         fun SetSliderTimer(sliderviewPager: ViewPager) {
            var currentPage = 0
            var timer: Timer
            val DELAY_MS: Long = 300//delay in milliseconds before task is to be executed
            val PERIOD_MS: Long = 2000 // time in milliseconds between successive task executions.

            val handler = Handler()
            val Update = Runnable {
                if (currentPage === 5 - 1) {
                    currentPage = 0
                }
                sliderviewPager.setCurrentItem(currentPage++, true)
            }

            timer = Timer() // This will create a new Thread
            timer.schedule(object : TimerTask() { // task to be scheduled
                override fun run() {
                    handler.post(Update)
                }
            }, DELAY_MS, PERIOD_MS)
        }
    }





}