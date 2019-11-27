package system

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.CheckoutActivity
import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.model.post.DishItem
import kotlinx.android.synthetic.main.checkout_btn_layout.view.*
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


enum class UpdateType {
    ADD, REMOVE
}


enum class CountType {
    PRODUCT, SHARE
}


enum class PakingDescription(val value: String) {
    DESC_GET_YOUROWN_BOX("Get your own box"),
    DESC_PARCEL_IN_DISPOSABLE_BOX("Parcel in disposable box")
}

enum class DeliveryDescription(val value: String) {
    DESC_HOME_DELIVERY("Home delivery"),
    DESC_SELF_PICK("Self pick")
}


enum class PakingOption(val value: Int) {
    GET_YOUROWN_BOX(2),
    PARCEL_IN_DISPOSABLE_BOX(4),
    BOTH(2 and 4)
}

enum class SpecilizeOption(val value: Int) {
    VEG(2),
    NON_VEG(4),
    BOTH(6)
}

enum class DeliveryOption(val value: Int) {
    HOME_DELIVERY(2),
    SELF_PICK(4),
    BOTH(2 and 4)
}

enum class UserType(val value: Int) {
    BUYER(1),
    SELLER(2)
}

enum class UserStatus(val value: String) {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    PENDING_APPROVAL("Pending Approval")
}

enum class DishState {
    ACTIVE, IN_ACTIVE, SIGNATURE
}

enum class DateFromTo {
    FROM, TO
}

enum class Gender(val value: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHERS("Others")
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun String.upper(): String {
    return this.toUpperCase()
}

class Utility {

    var currentPage = 0
    internal lateinit var timer: Timer
    val DELAY_MS: Long = 300//delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 2000 // time in milliseconds between successive task executions.


    companion object {
        val DISH_FEED: String = "dish_feed"
        var mIsFirst_Item: Boolean = true
        const val DB_ACCESS_TOKEN: String =
            "COWS_9zMWAAAAAAAAAACW_OXEy8dVmcZYsXpfYhrezUJWx-ogV8z_hwHFLko9ewb"
        val myMap: Map<String, MutableList<CartItem>> = mapOf<String, MutableList<CartItem>>()

        var IS_INITIALIZE = false


        const val USER_ID = "USER_ID"

        const val SLASH = "/"

        const val USER_TYPE_ID = "USER_TYPE_ID"
        const val MY_PREFERENCE = "MY_PREFERENCE"


        const val HH_MM_A = "hh:mm a"
        const val DD_MM_YYYY = "DD-MM-YYYY"
        const val HH_MM_SS = "HH:mm:ss"
        const val STANDARD_DATE_FORMATE = "yyyy-MM-dd HH:mm:ss.SSS"
        const val SELLER_DATE_TIME_FORMATE = "yyyy MMMM dd, hh:mm a"
        const val NETWORK_STANDARD_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        const val NETWORK_STANDARD_TIME_2 = "yyyy-MM-dd'T'HH:mm:ss"
        const val NETWORK_STANDARD_TIME_3 = "yyyy-MM-dd'T'HH:mm:ss"

        const val WHATS_APP_PACKAGE_NAME = "com.whatsapp"
        const val SHARE_BY_CHOOSER = 10
        const val SHARE_BY_CHOOSER_NEW = 1001


        val CART_TOTAL = "CART_TOTAL"
        val PACKING_CHARGES = "PACKING_CHARGES"
        val DELIVERY_CHARGES = "DELIVERY_CHARGES"
        val BILL_TOTAL = "BILL_TOTAL"

        const val ACCESS_TOKEN = "access_token"
        const val USER_CHEF_ID = "user_chef_id"


        val BUYER_HOME_FEED_KEY = "BUYER_HOME_FEED"
        val DISH_ID_KEY = "DISH_ID"

        val WRITE_REVIEW_KEY = "WRITE_REVIEW_KEY"
        val DISH_NAME_KEY = "DISH_NAME_ID"
        val GROUP_NAME_KEY = "GROUP_NAME_KEY"
        val CHEF_ID_KEY = "CHEF_ID"
        val SELLER_USER_ID_KEY = "SELLER_USER_ID"
        val DISH_ITEM_KEY = "DISH_ITEM"

        val ACTIVITY_NAME = "ACTIVITY_NAME"
        //val ACTIVITY_VALUE = "ACTIVITY_VALUE"

        // Start:- Handel Cant palace order for two different chef
        fun isDifferentChef(chefId: Int): Boolean {

            if (mIsFirst_Item) {
                Manager.chefId = chefId
                this.mIsFirst_Item = false
            }

            var result: Boolean = false
            if (Manager.chefId != 0 && Manager.chefId != chefId) {
                result = true
            }

            return result
        }

        fun resetFlag() {
            Manager.chefId = 0
            this.mIsFirst_Item = true
        }
        // End:- Handel Cant palace order for two different chef


        fun currentDate(dateFormate: String): String {
            val sdf1: SimpleDateFormat = SimpleDateFormat(dateFormate, Locale.US)
            val currentDate1 = sdf1.format(Date())

            return currentDate1
        }

        fun standardDate(dateFormate: String, date: String): String {
            try {
                val sdf1 = SimpleDateFormat(dateFormate, Locale.US)
                val sdf2 = SimpleDateFormat(STANDARD_DATE_FORMATE, Locale.US)
                return sdf2.format(sdf1.parse(date))
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun toNetworkDate(dateFormate: String, date: String): String {
            try {
                val sdf1 = SimpleDateFormat(dateFormate, Locale.US)
                val sdf2 = SimpleDateFormat(NETWORK_STANDARD_TIME_2, Locale.US)
                return sdf2.format(sdf1.parse(date))
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun standardDateToTimeSlot(date: String): String {
            try {
                val sdf1 = SimpleDateFormat(NETWORK_STANDARD_TIME_2, Locale.US)
                val sdf2 = SimpleDateFormat(HH_MM_A, Locale.US)
                return sdf2.format(sdf1.parse(date))
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun fromCaldarToDate(calendar: Calendar?): String {
            try {
                val date = calendar?.time
                val sdf1 = SimpleDateFormat(SELLER_DATE_TIME_FORMATE, Locale.US)
                return sdf1.format(date)
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun fromCaldarToNetworkDate(calendar: Calendar?): String {
            try {
                val date = calendar?.time
                val sdf1 = SimpleDateFormat(NETWORK_STANDARD_TIME_2, Locale.US)
                return sdf1.format(date)
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun getDateFromSting(date:String):Date?{
            try {
                val sdf1 = SimpleDateFormat(NETWORK_STANDARD_TIME_2, Locale.US)

                return (sdf1.parse(date))
            } catch (e: ParseException) {
                return null
            }
        }

        fun fromNetworkToToSellerTimelot(date: String): String {
            try {
                val sdf1 = SimpleDateFormat(NETWORK_STANDARD_TIME_2, Locale.US)
                val sdf2 = SimpleDateFormat(SELLER_DATE_TIME_FORMATE, Locale.US)
                return sdf2.format(sdf1.parse(date))
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun fromSellerTimelotToToNetwork(date: String): String {
            try {
                val sdf1 = SimpleDateFormat(SELLER_DATE_TIME_FORMATE, Locale.US)
                val sdf2 = SimpleDateFormat(NETWORK_STANDARD_TIME_3, Locale.US)
                return sdf2.format(sdf1.parse(date))
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun standardDateToTimeSlotForViewed(date: String): String {
            try {
                val sdf1 = SimpleDateFormat(NETWORK_STANDARD_TIME_2, Locale.US)
                val sdf2 = SimpleDateFormat(HH_MM_A, Locale.US)
                return sdf2.format(sdf1.parse(date))
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun change24hoursTimeSlot(date: String): String {
            try {
                val sdf1 = SimpleDateFormat(HH_MM_A, Locale.US)
                val sdf2 = SimpleDateFormat(HH_MM_SS, Locale.US)
                return sdf2.format(sdf1.parse(date))
            } catch (e: ParseException) {
                return e.localizedMessage
            }
        }

        fun SplitString(email: String): String {
            val split = email.split("@")
            return split[0]
        }

        fun createDialog(context: Context, title: String, msg: String) {

            val builder = AlertDialog.Builder(context)
            // Set the alert dialog title
            builder.setTitle(title.upper())
            // Display a message on alert dialog
            builder.setMessage(msg)
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
            }
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()
            // Display the alert dialog on app interface
            dialog.show()
        }

        fun createCustomDialog(context: Context, msg: String, listner: View.OnClickListener) {

            val dialog: Dialog = Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
            (dialog.findViewById(R.id.tv_msg) as TextView).text = msg
            (dialog.findViewById(R.id.btn_cancel) as Button).setOnClickListener{ dialog.dismiss() };
            (dialog.findViewById(R.id.btn_yes) as Button).setOnClickListener(listner);
            dialog.show();
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        fun createCustomDialog(context: Context, msg: String) {

            val dialog = Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
            (dialog.findViewById(R.id.tv_msg) as TextView).text = msg
            (dialog.findViewById(R.id.btn_yes) as Button).setOnClickListener{ dialog.dismiss() };
            dialog.show();
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        fun createCustomDialog(context: Context, msg: String,isOneBuuton:Boolean) {

            val dialog = Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
            (dialog.findViewById(R.id.tv_msg) as TextView).text = msg
            if(isOneBuuton) {
                (dialog.findViewById(R.id.btn_cancel) as Button).visibility = GONE
                (dialog.findViewById<View>(R.id.view27)).visibility= GONE
                (dialog.findViewById<View>(R.id.view28)).visibility= GONE

            }
            (dialog.findViewById(R.id.btn_yes) as Button).setOnClickListener{ dialog.dismiss() };
            dialog.show();
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        fun createCustomTwoButtonDialog(
            context: Context,
            msg: String,
            listner: View.OnClickListener
        ) {

            val dialog: Dialog = Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
            (dialog.findViewById(R.id.tv_msg) as TextView).text = msg
            (dialog.findViewById(R.id.btn_cancel) as Button).setOnClickListener(View.OnClickListener { dialog.dismiss() });
            (dialog.findViewById(R.id.btn_yes) as Button).text="Proced"
            (dialog.findViewById(R.id.btn_yes) as Button).setOnClickListener(listner)
            dialog.show();
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );


        }

        fun createCustomTwoButtonDialog(
            context: Context,
            msg: String,
            postiveButton: String,
            negetiveButtton: String,
            listner: View.OnClickListener
        ) {

            val dialog: Dialog = Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
            (dialog.findViewById(R.id.tv_msg) as TextView).text = msg
            (dialog.findViewById(R.id.btn_cancel) as Button).setOnClickListener(View.OnClickListener { dialog.dismiss() });
            (dialog.findViewById(R.id.btn_yes) as Button).setOnClickListener(listner)
            (dialog.findViewById(R.id.btn_yes) as Button).text = postiveButton
            (dialog.findViewById(R.id.btn_cancel) as Button).text = negetiveButtton
            dialog.show();
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );


        }

        fun appInstalledOrNot(context: Context, uri: String): Boolean {
            val pm = context.getPackageManager()
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                return true;
            } catch (e: PackageManager.NameNotFoundException) {
            }
            return false;
        }

        fun createDialog(
            context: Context,
            title: String,
            msg: String,
            listner: DialogInterface.OnClickListener
        ) {

            val builder = AlertDialog.Builder(context)
            // Set the alert dialog title
            builder.setTitle(title.upper())
            // Display a message on alert dialog
            builder.setMessage(msg)
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("OK", listner)
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()
            // Display the alert dialog on app interface
            dialog.show()
        }

        fun getCurrentDateAndTimeWithPattern(): String {
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val calendar = Calendar.getInstance()
            val time = Calendar.getInstance().time
            val strDate = sdf.format(time)
            return strDate
        }

        // in Use but
        fun resizeBitmap(context: Context, imgPath: String): Bitmap {
            //  75 is half of the Image view width and height and it is allways hardcoded
            val desWidth = 50.dpToPx(context.resources.displayMetrics)
            val desHeight = 50.dpToPx(context.resources.displayMetrics)

            // inSampleSize is a scale factor
            val inSampleSize = calculateInSampleSize(imgPath, desWidth, desHeight)

            // Image after scelling
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inJustDecodeBounds = false // to load into memory
            bitmapOptions.inSampleSize = inSampleSize // Must be of power of 2
            val smallBitmap: Bitmap = BitmapFactory.decodeFile(imgPath, bitmapOptions)
            val imgSize = getImageSizeInString(smallBitmap.getByteCount())
            return FixRotation(imgPath, smallBitmap)
        }

        // Not in Use but it works and it is second Choice
        fun resizeBitmapDirect(context: Context, imgPath: String): Bitmap {

            val bitmapOptions = BitmapFactory.Options().apply {
                inJustDecodeBounds = false // to load into memory
            }

            val srcImage = BitmapFactory.decodeFile(imgPath, bitmapOptions);
            val desWidth = 150.dpToPx(context.resources.displayMetrics)
            val desHeight = 150.dpToPx(context.resources.displayMetrics)

            val reducedBitmap = Bitmap.createScaledBitmap(srcImage, desWidth, desHeight, false)
            val imgSizeCurrentNew = getImageSizeInString(reducedBitmap.getByteCount())

            // Return
            return FixRotation(imgPath, reducedBitmap)
        }

        // Not in Use but it works and it is thired Choice
        fun resizeBitmapAlternative(context: Context, imgPath: String): Bitmap {

            val srcOptions = BitmapFactory.Options().apply {
                inJustDecodeBounds = false // to load into memory
            }
            val srcWidth: Int = srcOptions.outWidth

            val desWidth = 150.dpToPx(context.resources.displayMetrics)
            val desHeight = 150.dpToPx(context.resources.displayMetrics)

            // inSampleSize is a scale factor
            val inSampleSize = calculateInSampleSize(imgPath, desWidth, desHeight)

            // Image after scelling
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inScaled = true
            bitmapOptions.inSampleSize = inSampleSize
            bitmapOptions.inDensity = srcWidth
            bitmapOptions.inTargetDensity = desWidth * bitmapOptions.inSampleSize
            val currentBitmap = BitmapFactory.decodeFile(imgPath, bitmapOptions)


            // Return
            return FixRotation(imgPath, currentBitmap)
        }


        fun getImageSizeInString(size: Int): String {
            if (size <= 0)
                return "0"
            val units = arrayOf<String>("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(
                size / Math.pow(
                    1024.0,
                    digitGroups.toDouble()
                )
            ) + " " + units[digitGroups]
        }

        private fun Int.dpToPx(displayMetrics: DisplayMetrics): Int =
            (this * displayMetrics.density).toInt()

        private fun Int.pxToDp(displayMetrics: DisplayMetrics): Int =
            (this / displayMetrics.density).toInt()

        // Once we reduce the bitmap it rotered automatically, to fix this we are using this method
        private fun FixRotation(photoPath: String, bitmap: Bitmap): Bitmap {
            val ei = ExifInterface(photoPath)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val rotatedBitmap: Bitmap
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90F)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180F)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270F)
                ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
                else -> rotatedBitmap = bitmap
            }

            // Return
            return rotatedBitmap
        }

        private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true
            )
        }

        // Private Method
        private fun calculateInSampleSize(imgPath: String, reqWidth: Int, reqHeight: Int): Int {

            val bitmapOptions = BitmapFactory.Options().apply {
                //   inJustDecodeBounds = true beacuse We don’t want to load bitmap into memory. We just want to get information(width, height)
                // If we check options.bitmap  wiill return null. but if we set to false, then it load to momory.
                inJustDecodeBounds = true
            }

            val originalImg = BitmapFactory.decodeFile(imgPath, bitmapOptions);

            // Raw height and width of image
            val (height: Int, width: Int) = bitmapOptions.run { outHeight to outWidth }
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight: Int = height / 2
                val halfWidth: Int = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }


        fun manageCheckoutButton(cartItems: ArrayList<CartItem>, view: View, rv: View?) {
            fun Int.dpToPx(displayMetrics: DisplayMetrics?): Int =
                (this * displayMetrics!!.density).toInt()

            val dismet: DisplayMetrics? = rv?.context?.resources?.displayMetrics
            if (cartItems.size > 0) {

                view.visible()
                view.tv_item_count.text = ShoppingCart.totalCartQuantityCount.toString() + " items"
                view.tv_total_amount.text =
                    "Rs. " + ShoppingCart.getTotalCartAmount().toString()
                // ShoppingCart.getTotalAmount1().toString() //
                if (rv != null) {

                    val param = rv!!.layoutParams as ConstraintLayout.LayoutParams
                    param.setMargins(
                        8.dpToPx(dismet),
                        8.dpToPx(dismet),
                        8.dpToPx(dismet),
                        75.dpToPx(dismet)
                    )
                    rv.layoutParams = param
                    val param1 =
                        view.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
                    param1.setMargins(0, -75.dpToPx(dismet), 0, 0)
                    view.layoutParams = param1

                }

            } else {

                view.gone()
                if (rv != null) {
                    val param = rv!!.layoutParams as ConstraintLayout.LayoutParams
                    param.setMargins(8.dpToPx(dismet), 8.dpToPx(dismet), 8.dpToPx(dismet), 0)
                    rv.layoutParams = param
                    val param1 =
                        view.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
                    param1.setMargins(0, 0, 0, 0)
                    view.layoutParams = param1
                }

            }
            view.tv_proced_btn.setOnClickListener {
                val intent = Intent(view.tv_proced_btn.context, CheckoutActivity::class.java)
                view.tv_proced_btn.context.startActivity(intent)
            }
        }

        fun changeColorOfChid(layout: LinearLayout, textView: View) {
            layout.forEach {
                if (it is TextView) {
                    (it as TextView).setTextColor(
                        ContextCompat.getColor(
                            layout.context,
                            R.color.SkyBlue
                        )
                    )
                    it.setBackgroundResource(R.drawable.rounded_corner_grey)
                }
            }
            (textView as TextView).setTextColor(
                ContextCompat.getColor(
                    layout.context,
                    R.color.filterText
                )
            )
            textView.setBackgroundResource(R.drawable.buy_button_round_corner)
        }

        fun changeColorOfChidSeller(layout: LinearLayout, textView: View) {
            layout.forEach {
                if (it is TextView) {
                    it.setTextColor(
                        ContextCompat.getColor(
                            layout.context,
                            R.color.DarkBlue
                        )
                    )
                    it.setBackgroundResource(R.drawable.border)
                }
            }
            (textView as TextView).setTextColor(
                ContextCompat.getColor(
                    layout.context,
                    R.color.White
                )
            )
            textView.setBackgroundColor(
                ContextCompat.getColor(
                    layout.context,
                    R.color.DarkBlue
                )
            )
        }

        fun AddItem(dishInfo: HomeFeed.Dish) {
            val dishItem: DishItem = DishItem()
            dishItem.dishId = dishInfo.DishId
            dishItem.deliveryStartTime = dishInfo.DishAvailableStartTime
            dishItem.deliveryEndTime = dishInfo.DishAvailableEndTime
            dishItem.packingTypeId = dishInfo.PackingOptions
            dishItem.deliveryTypeId = dishInfo.DeliveryOptions
            dishItem.quantity = 1
            dishItem.unitPrice = dishInfo.UnitPrice

            val cartItem = CartItem(dishItem)
            ShoppingCart.addToCart(cartItem)

            // Read back for Checking
            val listOfCartItem = ShoppingCart.getCartItems()


//        val gson = GsonBuilder().serializeNulls().create()
//        val thisorder =  gson.toJson(listOfCartItem.toList())
        }


        fun RemoveItem(dishId: Int) {
//            var dishItem: DishItem = DishItem()
//            dishItem.dishId = dishInfo.DishId
//            dishItem.deliveryStartTime = dishInfo.DishAvailableStartTime
//            dishItem.deliveryEndTime = dishInfo.DishAvailableEndTime
//            dishItem.packingTypeId = dishInfo.PackingOptions
//            dishItem.deliveryTypeId = dishInfo.DeliveryOptions
//            dishItem.quantity = 1
//            dishItem.unitPrice = dishInfo.UnitPrice

            //val thiDishItem = CartItem(dishItem)

            ShoppingCart.removeFromCart(dishId)

            // Read back for Checking
            val cart = ShoppingCart.getCartItems()
        }


        fun setDishSymbol(dishType: String, context: Context): Drawable? {
            var drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.nonveg_symbol)
            if (dishType == "Veg") {
                drawable = ContextCompat.getDrawable(context, R.drawable.veg_symbol)
            }
            return drawable
        }


        private val arrSliderImages = arrayOf(
            R.drawable.kadai_panner,
            R.drawable.egg_biryani,
            R.drawable.roti_new,
            R.drawable.tandoori_new
        )


        fun getImageSliderCount(): Int {
            return arrSliderImages.count()
        }


        fun SetImageSliderHeight(
            windowManager: WindowManager,
            sliderviewPager: androidx.viewpager.widget.ViewPager
        ) {
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


        fun SetSliderTimer(sliderviewPager: androidx.viewpager.widget.ViewPager) {
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











