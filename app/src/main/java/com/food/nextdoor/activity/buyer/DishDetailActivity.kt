package com.food.nextdoor.activity.buyer

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.DishDetailAdapter
import com.food.nextdoor.database.DbOperation
import com.food.nextdoor.listeners.OnItemCountListener
import com.food.nextdoor.listeners.YouMayLikeSelectListener

import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.checkout_btn_layout.view.*
import kotlinx.android.synthetic.main.dish_detail.*
import system.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

import android.app.Activity
import android.content.ComponentName
import android.os.Parcelable
import android.util.Log
import com.food.nextdoor.database.NextDoorDB
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.home.*
import com.dropbox.core.v2.teamlog.ActorLogInfo.app
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import com.food.nextdoor.activity.seller.SellerUserActivity
import com.food.nextdoor.dropbox.DropboxClientFactory
import com.food.nextdoor.dropbox.ImagePiccassoRequestHadler
import com.food.nextdoor.dropbox.PicassoClient
import com.food.nextdoor.model.post.*
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import kotlinx.android.synthetic.main.registration.*


class DishDetailActivity : AppCompatActivity(), OnItemCountListener, YouMayLikeSelectListener {


    //private  var listOfCartItem: ArrayList<CartItem> = arrayListOf()
    private var mCurrentDishId = 0
    private var mDishInfo: HomeFeed.Dish? = null
    private lateinit var mProductView : ProductView
    private lateinit var mStartTime:String
    private lateinit var mEndTime:String
    private var mSellerUserId: Int = -1
    private var mAppName = ""

    companion object {
        val REQUEST_CODE: Int = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.dish_detail)

        if (intent.hasExtra(Utility.DISH_ID_KEY) && intent.hasExtra(Utility.SELLER_USER_ID_KEY)) {
            this.mCurrentDishId = intent.getIntExtra(Utility.DISH_ID_KEY, 0)
            this.mSellerUserId = intent.getIntExtra(Utility.SELLER_USER_ID_KEY,0)
        }







        val homeFeed = DataHolder.homeFeedInstance
        //supportActionBar?.title = mCurrentDishId.toString()

        //listOfCartItem = ShoppingCart.getCartItems()
        Utility.manageCheckoutButton(ShoppingCart.getCartItems(), floating_proced_lay_dish, null)

        val dishInfo: HomeFeed.Dish? = homeFeed.dishes.find { d -> d.DishId == mCurrentDishId }
        dishInfo.let {
            mDishInfo = dishInfo
            setControls(dishInfo = mDishInfo!!)
            add_remove_btton.configDishItem(mDishInfo!!, this)
            setOngoingRecycleView(homeFeed) // Set Ongoing Recycle View with 4  rows Only
        }


        this.increaseCount(CountType.PRODUCT)

        if (dishInfo == null) Utility.createDialog(this, "Dish Detail", "dishInfo is null in onCreate")



        val clickedViewedShared= View.OnClickListener { val intent1= Intent(this@DishDetailActivity,SellerUserActivity::class.java)
        intent1.putExtra(Utility.DISH_ID_KEY,dishInfo?.DishId)
        startActivity(intent1)}
        btn_view_dish_detail.setOnClickListener(clickedViewedShared)
        btn_share_dish_detail.setOnClickListener(clickedViewedShared)

        btn_share_dish_detail.setOnClickListener {
            val socialMedia = SocialMedia()
             socialMedia.shareWIthWhatsAppOrChooserResult(this,"Test string","http://youtube.com/")
            //socialMedia.shareWIthWhatsAppOrChooser(this,"Soumen Test string","http://youtube.com/")






        }
    }






    private fun increaseCount(countType: CountType) {

        if (countType == CountType.PRODUCT) {
            mProductView = ProductView()

            mProductView.productId = mCurrentDishId
            mProductView.sellerId = mSellerUserId
            mProductView.date = Utility.currentDate(Utility.DD_MM_YYYY)
            mProductView.startTime = Utility.currentDate(Utility.HH_MM_SS)
        } else if (countType == CountType.SHARE) {
            val share = Share()

            share.productId = mCurrentDishId
            share.sellerId = mSellerUserId
            share.date = Utility.currentDate(Utility.DD_MM_YYYY)
            share.time = Utility.currentDate(Utility.HH_MM_SS)
            //share.shareType = ""

            val execute = DbOperation.InsertShare(this).execute(share)
            // To confirm
            val shareList = NextDoorDB.invoke(this).daoAccess.getShareList()

             var a = "Soumen"
             a = a + "Roy"
        }

    }


    private fun saveProductViewCount() {

        if (mProductView.endTime.isNullOrEmpty()) {
            mProductView.endTime = Utility.currentDate(Utility.HH_MM_SS)
            val execute = DbOperation.InsertProductView(this).execute(mProductView)

            // To confirm
            //val productView = NextDoorDB.invoke(this).daoAccess.getProductViewList()
        }
    }



    private fun increaseShareCount() {
        mProductView = ProductView()

        mProductView.productId = mCurrentDishId
        mProductView.sellerId = mSellerUserId
        mProductView.date = Utility.currentDate(Utility.DD_MM_YYYY)
        mProductView.startTime = Utility.currentDate(Utility.HH_MM_SS)
    }




    override fun onPause() {
        super.onPause()
        this.saveProductViewCount()
    }



    override fun onDestroy() {
        super.onDestroy()
        this.saveProductViewCount()
    }




    override fun onResume() {
        super.onResume()
        Utility.manageCheckoutButton(ShoppingCart.getCartItems(), floating_proced_lay_dish, null)
        mStartTime=Utility.currentDate(Utility.NETWORK_STANDARD_TIME)
    }

    fun loadAllDishes() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun setControls(dishInfo: HomeFeed.Dish) {

        //Soumen(dishInfo)

        // set main Dish Image
        
        PicassoClient.init(this, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
        PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(Utility.SLASH +dishInfo.DishImageUrl))!!
            .into(img_dish_Image_main)

        // set Dish Symbol
        val dishSymbol = Utility.setDishSymbol(dishInfo.DishType, img_dish_symbol_detail_main.context)
        img_dish_symbol_detail_main.setImageDrawable(dishSymbol)


        tv_dish_price_detail_main.text = "Rs." + (dishInfo.UnitPrice).toString()
        tv_dish_name_detail_main.text = dishInfo.DishName
        tv_dish_rating_detail_main.text = dishInfo.DishAverageRating.toString()
        tv_dish_rating_bar_detail.rating = dishInfo.DishAverageRating.toFloat()
        tv_dish_description_detail_main.text = dishInfo.DishDescription
//        tv_dish_reviews_detail.text=dishInfo.DishTotalReview.toString()


        // Click Listener for BUY Button





        if(dishInfo.DishTotalReview>0){
                tv_num_of_dish_reviews_detail.setOnClickListener {
                    val intent = Intent(
                        tv_num_of_dish_reviews_detail.context,
                        ReadReviewActivity::class.java
                    )
                    intent.putExtra(Utility.DISH_ID_KEY, dishInfo.DishId)
                    tv_num_of_dish_reviews_detail.context.startActivity(intent)
                }
            }
        tv_view_all_dishes_dish_detail.setOnClickListener {
            val intent = Intent(tv_view_all_dishes_dish_detail.context, HomeActivity::class.java)
            tv_view_all_dishes_dish_detail.context.startActivity(intent)
        }

        //OnClick BackButton in dishdetails
        fab_detail.setOnClickListener {
            finish()
        }




    }


    private fun setOngoingRecycleView(buyerHomeFeed: HomeFeed?) {
        recyclerView_dish_detail.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView_dish_detail.isNestedScrollingEnabled = false

        val fiveDishes: List<HomeFeed.Dish> = this.removeSelectedItem(buyerHomeFeed)
        recyclerView_dish_detail.adapter = DishDetailAdapter(this@DishDetailActivity, buyerHomeFeed, fiveDishes)
    }

    // This is require because, in the buy button click event from the DishDetailAdopter we are setting startActivityForResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DishDetailAdapter.REQUEST_CODE && data!=null) {
            val thisDishItem = data!!.getParcelableExtra<DishItem>(Utility.DISH_ITEM_KEY)

            thisDishItem?.let {
                val thiCartItem = CartItem(thisDishItem)
                ShoppingCart.addToCart(thiCartItem)
                //listOfCartItem = ShoppingCart.getCartItems()
            }
            add_remove_btton.configDishItem(mDishInfo!!, this)
        }
        else if(requestCode==Utility.SHARE_BY_CHOOSER_NEW){
            mAppName = data?.component?.flattenToShortString()!!;
            NextDoorDB.invoke(this).daoAccess.insertSharedDish(SharedDish(ChefId =mDishInfo!!.ChefId!!,UserId = Preference(this).getUserId(),DishId = mDishInfo!!.DishId,SharedVia = mAppName) )
            if (resultCode == Activity.RESULT_OK) {
                this.increaseCount(CountType.SHARE)

            }



          //val ri =  data!!.data


           startActivity(data)


        }


        else if(requestCode==1002){

            if (resultCode == Activity.RESULT_OK) {

            }


            if (resultCode == Activity.RESULT_CANCELED) {

            }



        }
    }



    override fun onStop() {
        super.onStop()
        mEndTime=Utility.currentDate(Utility.NETWORK_STANDARD_TIME)
        NextDoorDB.invoke(this).daoAccess.insertDishView(PostViewed(ChefId =mDishInfo!!.ChefId!!,UserId = Preference(this).getUserId(),DishId = mDishInfo!!.DishId,ViewedStartTime = mStartTime,ViewedEndTime = mEndTime) )
    }

    private fun removeSelectedItem(homeFeed: HomeFeed?): List<HomeFeed.Dish> {
        var mFiveDishes: List<HomeFeed.Dish> = arrayListOf()
        val mShuffledDishes: ArrayList<HomeFeed.Dish> = homeFeed!!.dishes.shuffled() as ArrayList<HomeFeed.Dish>

        //val productIdList = ShoppingCart.getProductIdList()
        // productIdList.remove(mCurrentDishId)

        /* Remove currently selected item */
        val matchingElement: HomeFeed.Dish? = mShuffledDishes.find { c -> c.DishId == mCurrentDishId }
        if (matchingElement != null) {
            mShuffledDishes.remove(matchingElement)
            mFiveDishes = mShuffledDishes.take(5)
        }


//        // Remove all items those are already added to cart
//        for(productId in productIdList) {
//            matchingElement = mShuffledDishes.find { c -> c.DishId == productId }
//            if (matchingElement != null) {
//                mShuffledDishes.remove(matchingElement)
//                mFiveDishes  = mShuffledDishes.take(5)
//            } else {
//                Utility.createDialog(this,"Dish Detail", "Matching element is null in loop" )
//            }
//        }

        return mFiveDishes
    }


    override fun updateCount() {
        Utility.manageCheckoutButton(
            ShoppingCart.getCartItems(),
            floating_proced_lay_dish,
            null
        )
    }

    override fun onDishRowClick(dishInfo: HomeFeed.Dish) {
        this.setControls(dishInfo)
        mDishInfo=dishInfo
        add_remove_btton.configDishItem(mDishInfo!!, this)
        nestedScrollView2.post(Runnable { nestedScrollView2.scrollTo(0,0) })
    }


    private fun Soumen(dishInfo: HomeFeed.Dish?) {
        // Start : Soumen temp Code

        // Reduce Image size
        //val reduceImg: Bitmap = Utility.resizeBitmap(this, picturePath!!)
        //val ingSize: String = Utility.getImageSizeInString(reduceImg.getByteCount())
        //img_customer_image_reg.setImageBitmap(reduceImg)
        Picasso.with(this).load(dishInfo!!.DishImageUrl).into(img_dish_Image_main)
        val bitmap: Bitmap = (img_dish_Image_main.drawable as BitmapDrawable).bitmap
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)

        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        val uri: Uri = Uri.fromFile(file)


        //val dirPath= file.absolutePath
        ///val bitmap1:Bitmap=Picasso.with(this).load(dishInfo!!.DishImageUrl).get()
        //  var file1 = wrapper.getDir("images", Context.MODE_PRIVATE)

        // End : Soumen temp Code
    }

    //    fun imageDownload(ctx:Context, url:String) {
//        Picasso.with(ctx)
//            .load("http://blog.concretesolutions.com.br/wp-content/uploads/2015/04/Android1.png").into(getTarget(url))
//    }
//    //target to save
//    private fun getTarget(url:String): com.squareup.picasso.Target {
//        val target = object: com.squareup.picasso.Target {
//           override fun onBitmapLoaded(bitmap:Bitmap, from:Picasso.LoadedFrom) {
//                Thread(object:Runnable {
//                    public override fun run() {
//                        val file = File(Environment.getExternalStorageDirectory().getPath() + "/" + url)
//                        try
//                        {
//                            file.createNewFile()
//                            val ostream = FileOutputStream(file)
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
//                            ostream.flush()
//                            ostream.close()
//                        }
//                        catch (e:IOException) {
//                            Log.e("IOException", e.getLocalizedMessage())
//                        }
//                    }
//                }).start()
//            }
//
//            override  fun onBitmapFailed(errorDrawable: Drawable) {
//            }
//            override fun onPrepareLoad(placeHolderDrawable:Drawable) {
//            }
//
//        }
//        return target
//    }



    private fun openWhatsApp() {
        val smsNumber = "7****" // E164 format without '+' sign
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        sendIntent.putExtra("jid", "$smsNumber@s.whatsapp.net") //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp")
        if (intent.resolveActivity(this.getPackageManager()) == null) {
            Utility.createCustomDialog(this,  "WhatsApp not found",true)
            return
        }
        startActivity(sendIntent)
    }




//    fun onShareClick1(v:View) {
//        val resources = getResources()
//        val emailIntent = Intent()
//        emailIntent.setAction(Intent.ACTION_SEND)
//        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
//        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_native)))
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject))
//        emailIntent.setType("message/rfc822")
//        val pm = getPackageManager()
//        val sendIntent = Intent(Intent.ACTION_SEND)
//        sendIntent.setType("text/plain")
//        val openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text))
//        val resInfo = pm.queryIntentActivities(sendIntent, 0)
//        val intentList = ArrayList<LabeledIntent>()
//        for (i in 0 until resInfo.size())
//        {
//            // Extract the label, append it, and repackage it in a LabeledIntent
//            val ri = resInfo.get(i)
//            val packageName = ri.activityInfo.packageName
//            if (packageName.contains("android.email"))
//            {
//                emailIntent.setPackage(packageName)
//            }
//            else if (packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm"))
//            {
//                val intent = Intent()
//                intent.setComponent(ComponentName(packageName, ri.activityInfo.name))
//                intent.setAction(Intent.ACTION_SEND)
//                intent.setType("text/plain")
//                if (packageName.contains("twitter"))
//                {
//                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_twitter))
//                }
//                else if (packageName.contains("facebook"))
//                {
//                    // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
//                    // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
//                    // will show the <meta content ="..."> text from that page with our link in Facebook.
//                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_facebook))
//                }
//                else if (packageName.contains("mms"))
//                {
//                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_sms))
//                }
//                else if (packageName.contains("android.gm"))
//                { // If Gmail shows up twice, try removing this else-if clause and the reference to "android.gm" above
//                    intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_gmail)))
//                    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject))
//                    intent.setType("message/rfc822")
//                }
//                intentList.add(LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon))
//            }
//        }
//        // convert intentList to array
//        val extraIntents = intentList.toArray(arrayOfNulls<LabeledIntent>(intentList.size()))
//        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents)
//        startActivity(openInChooser)
//    }

    fun onShareClick() {
        val targetShareIntents = ArrayList<Intent>()
        val shareIntent = Intent()
        shareIntent.setAction(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        val resInfos = getPackageManager().queryIntentActivities(shareIntent, 0)
        if (!resInfos.isEmpty())
        {
            println("Have package")
            for (resInfo in resInfos)
            {
                val packageName = resInfo.activityInfo.packageName
                Log.i("Package Name", packageName)
                if (packageName.contains("com.whatsapp") || packageName.contains("com.facebook.katana") || packageName.contains("com.kakao.story"))
                {
                    val intent = Intent()
                    intent.setComponent(ComponentName(packageName, resInfo.activityInfo.name))
                    intent.setAction(Intent.ACTION_SEND)
                    intent.setType("text/plain")
                    intent.putExtra(Intent.EXTRA_TEXT, "Text")
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                    intent.setPackage(packageName)
                    targetShareIntents.add(intent)
                }
            }
            if (!targetShareIntents.isEmpty())
            {
                println("Have Intent")
                // val chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share")

                val chooserIntent = Intent.createChooser(targetShareIntents.removeAt(0), "Choose app to share")

                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(arrayOf<Parcelable>()))
                startActivity(chooserIntent)
            }

        }
    }

}



