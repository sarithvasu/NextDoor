package com.food.nextdoor.activity.buyer


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.food.nextdoor.R
import com.food.nextdoor.activity.EditProfileActivity
import com.food.nextdoor.activity.seller.SellerUpload
import com.food.nextdoor.adapter.buyer.HomeAdapter
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.listeners.OnActiveDishSelected
import com.food.nextdoor.listeners.OnItemCountListener
import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.model.post.DishItem
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.busy_bar_layout.*
import kotlinx.android.synthetic.main.checkout_btn_layout.view.*
import kotlinx.android.synthetic.main.filter_layout.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.home_nav.*
import okhttp3.*
import system.*
import java.io.IOException


class HomeActivity : AppCompatActivity(), OnItemCountListener,
    NavigationView.OnNavigationItemSelectedListener,
    OnActiveDishSelected {

    enum class Filter {
        VEG, NON_VEG, ALL, BREAKFAST, SNACKS, DINNER, LUNCH, CHEF
    }

    override fun updateDishesByChef(dishes: List<HomeFeed.Dish>) {
        val chefDishes = HomeFeed(
            DataHolder.homeFeedInstance.chefs,
            dishes,
            DataHolder.homeFeedInstance.custommeals
        )

        this.recyclerView_home_buyer.adapter = HomeAdapter(this@HomeActivity, chefDishes, 0)
        SAVE_ROTATION = 0


    }

    private var filterEnum = Filter.ALL
    private var mCartItems: ArrayList<CartItem> = arrayListOf()

    // cache the ConstraintLayout
    //var mOld = true
    companion object {
        const val VIEWTYPE_CHEF: Int = 100
        const val VIEWTYPE_HOME: Int = 0
        var SAVE_ROTATION: Int = 0

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_nav)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /*
        * please change below
        * */

        // main_view.visibility = GONE
        // main_busy_bar.visibility = VISIBLE


        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout =
            findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        recyclerView_home_buyer.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)
        filterEnum = Filter.ALL
        if (SAVE_ROTATION == 100) {
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, DataHolder.homeFeedInstance, VIEWTYPE_CHEF)
            checkEmpty()
            filterEnum = Filter.CHEF
            SAVE_ROTATION = 100
        }

        // Draw line Divider between two rows in recyclerview
//        this.drawRowDivider()

        //fetchJsonFromServer()
        //this.readFromAsset("home_feed.json")
        swipe_me.setOnRefreshListener {
            fetchJsonFromServerUsingRefrofit()
            //readFromAsset("home_feed.json")

        }
        //readFromAsset("home_feed.json")
        fetchJsonFromServerUsingRefrofit()


        // Get User Info from local database and save it to manager instance
        this.loadUserInfoFromDB()


        Utility.manageCheckoutButton(ShoppingCart.getCartItems(), proced_lay, swipe_me)


        Utility.changeColorOfChid(layout_filter, tv_filter_all)
        tv_custom_meals.setOnClickListener { start() }
        ll_tomorow.setOnClickListener { onClickTomorow() }


        tv_filter_chef.setOnClickListener {
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, DataHolder.homeFeedInstance, VIEWTYPE_CHEF)
            checkEmpty()
            SAVE_ROTATION = 100
            filterEnum = Filter.CHEF
            Utility.changeColorOfChid(layout_filter, it)
        }
        tv_filter_all.setOnClickListener {
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, DataHolder.homeFeedInstance, VIEWTYPE_HOME)
            checkEmpty()
            SAVE_ROTATION = 0
            filterEnum = Filter.ALL
            Utility.changeColorOfChid(layout_filter, it)
        }
        tv_filter_veg.setOnClickListener {
            val dishes = (DataHolder.homeFeedInstance.dishes.filter { d -> d.DishType == "Veg" })
            val chefDishes =
                HomeFeed(
                    DataHolder.homeFeedInstance.chefs,
                    dishes,
                    DataHolder.homeFeedInstance?.custommeals
                )
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, chefDishes, VIEWTYPE_HOME)
            checkEmpty()
            SAVE_ROTATION = 0
            filterEnum = Filter.VEG
            Utility.changeColorOfChid(layout_filter, it)
        }
        tv_filter_non_veg.setOnClickListener {
            val dishes =
                (DataHolder.homeFeedInstance.dishes.filter { d -> d.DishType == "Non-Veg" })
            val chefDishes =
                HomeFeed(
                    DataHolder?.homeFeedInstance?.chefs,
                    dishes,
                    DataHolder.homeFeedInstance?.custommeals
                )
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, chefDishes, VIEWTYPE_HOME)
            checkEmpty()
            SAVE_ROTATION = 0
            filterEnum = Filter.NON_VEG
            Utility.changeColorOfChid(layout_filter, it)
        }
        tv_dish_count_tomorow.setOnClickListener {
            val intent = Intent(tv_dish_count_tomorow.context, SellerUpload::class.java)
            tv_dish_count_tomorow.context.startActivity(intent)
        }
        tv_filter_breakfast.setOnClickListener {
            val dishes =
                (DataHolder.homeFeedInstance.dishes.filter { d -> d.MealType == "Breakfast" })
            val updatedHomefeed = HomeFeed(
                DataHolder.homeFeedInstance.chefs,
                dishes,
                DataHolder.homeFeedInstance.custommeals
            )
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, updatedHomefeed, VIEWTYPE_HOME)
            checkEmpty()
            filterEnum = Filter.BREAKFAST
            SAVE_ROTATION = 0
            Utility.changeColorOfChid(layout_filter, it)
        }
        tv_filter_lunch.setOnClickListener {
            val dishes = (DataHolder.homeFeedInstance.dishes.filter { d -> d.MealType == "Lunch" })
            val updatedHomefeed = HomeFeed(
                DataHolder.homeFeedInstance.chefs,
                dishes,
                DataHolder.homeFeedInstance.custommeals
            )
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, updatedHomefeed, VIEWTYPE_HOME)
            checkEmpty()
            filterEnum = Filter.LUNCH
            SAVE_ROTATION = 0
            Utility.changeColorOfChid(layout_filter, it)
        }
        tv_filter_dinner.setOnClickListener {
            val dishes = (DataHolder.homeFeedInstance.dishes.filter { d -> d.MealType == "Dinner" })
            val updatedHomefeed = HomeFeed(
                DataHolder.homeFeedInstance.chefs,
                dishes,
                DataHolder.homeFeedInstance.custommeals
            )
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, updatedHomefeed, VIEWTYPE_HOME)
            checkEmpty()
            filterEnum = Filter.DINNER
            SAVE_ROTATION = 0
            Utility.changeColorOfChid(layout_filter, it)
        }
        tv_filter_snacks.setOnClickListener {
            val dishes = (DataHolder.homeFeedInstance.dishes.filter { d -> d.MealType == "Others" })
            val updatedHomefeed = HomeFeed(
                DataHolder.homeFeedInstance.chefs,
                dishes,
                DataHolder.homeFeedInstance.custommeals
            )
            recyclerView_home_buyer.adapter =
                HomeAdapter(this@HomeActivity, updatedHomefeed, VIEWTYPE_HOME)
            checkEmpty()
            SAVE_ROTATION = 0
            filterEnum = Filter.SNACKS
            Utility.changeColorOfChid(layout_filter, it)

        }


        //SendNotificationUsingChanel()
        //configureF

    }

    private fun checkEmpty() {
        if (recyclerView_home_buyer.adapter != null) {
            if ((recyclerView_home_buyer.adapter!! as HomeAdapter).itemCount == 0) {
                swipe_me_empty.visibility = VISIBLE
            } else {
                swipe_me_empty.visibility = GONE
            }
        }
    }


//    private fun SendNotificationUsingChanel(){
//        val notificationHelper: NotificationHelper?
//        notificationHelper= NotificationHelper(this)
//        val firstChannel = notificationHelper.getNfBuilderFromFirstChannel("First", "Test message from first channel")
//        val firstChanne2 = notificationHelper.getNfBuilderFromFirstChannel("Second", "Test message from second channel")
//        val firstChanne3 = notificationHelper.getNfBuilderFromFirstChannel("Third", "Test message from third channel")
//
//
//        notificationHelper.run {
//            Notify(1, firstChannel!!)
//            Notify(2, firstChanne2!!)
//            Notify(3, firstChanne3!!)
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(text: String?): Boolean = true

                override fun onQueryTextChange(text: String?): Boolean {
                    if (text.equals("")) {
                        fetchJsonFromServerUsingRefrofit()
                        //readFromAsset("home_feed.json")
                    } else {
                        var dishes1: ArrayList<HomeFeed.Dish> =
                            DataHolder.homeFeedInstance.dishes as ArrayList<HomeFeed.Dish>
                        dishes1 = dishes1.filter { dish ->
                            dish.DishName.toLowerCase().contains(text?.toLowerCase().toString())
                        } as ArrayList<HomeFeed.Dish>
                        DataHolder.homeFeedInstance.dishes = dishes1
                        recyclerView_home_buyer.adapter =
                            HomeAdapter(this@HomeActivity, DataHolder.homeFeedInstance, 0)
                        checkEmpty()
                    }

                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun start() {
        setContentView(R.layout.home_row_secondary)
    }

    private fun onClickTomorow() {
//        val homeFeed = Utility.DataHolder.homeFeedInstance
//        val tomorow = "29-06-2019"
//        val homeFeedTomorow = Utility.DataHolder.homeFeedInstance.signatureDishes.filter { d-> d.dish_available_date == tomorow }
    }


// fun OnClickProceed(view: View) {
//    /* val intent = Intent(this, CheckoutActivity::class.java)
//     this.startActivity(intent)*/
// }

    private fun readFromAsset(fileName: String) {
        val jsonstring = application.assets.open(fileName).bufferedReader().use { it.readText() }
        val gson = GsonBuilder().serializeNulls().create()
        val homeFeed = gson.fromJson(jsonstring, HomeFeed::class.java)
        DataHolder.homeFeedInstance = homeFeed

        recyclerView_home_buyer.apply {
            if (SAVE_ROTATION == 100) {
                recyclerView_home_buyer.apply {
                    recyclerView_home_buyer.adapter =
                        HomeAdapter(this@HomeActivity, homeFeed, VIEWTYPE_CHEF)
                    checkEmpty()
                }
            } else if (SAVE_ROTATION == 0) {
                recyclerView_home_buyer.apply {
                    recyclerView_home_buyer.adapter =
                        HomeAdapter(this@HomeActivity, homeFeed, VIEWTYPE_HOME)
                    checkEmpty()
                }
            }
        }
    }

    private fun fetchJsonFromServerUsingRefrofit() {
        val userId = Preference(this).getUserId()
        val buyerInfo = NextDoorDB.invoke(this).daoAccess.getBuyerInfo(userId)
        val buyerHomeScreenService: RetrofitService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        buyerHomeScreenService.getBuyerHomeData(
            "application/json",
            "application/json", buyerInfo.apartmentId
        ).also {
            it.enqueue(object : retrofit2.Callback<HomeFeed> {
                override fun onFailure(call: retrofit2.Call<HomeFeed>, t: Throwable) {
                    val s = "sdadd"
                    swipe_me.isRefreshing = false
                    progressBar.visibility = GONE
                    msg.visibility = VISIBLE

                }

                override fun onResponse(
                    call: retrofit2.Call<HomeFeed>,
                    response: retrofit2.Response<HomeFeed>
                ) {
                    if (response.isSuccessful) {
                        swipe_me.isRefreshing = false
                        main_view.visibility = VISIBLE
                        main_busy_bar.visibility = GONE
                        Log.e("", "xc';fd" + response.body())
                        val dataFeeder: HomeFeed = response.body()!!

                        DataHolder.homeFeedInstance = dataFeeder

                        recyclerView_home_buyer.apply {

                            setHomscreenRecycledView(dataFeeder)

                        }
                    }
                }
            })
        }

    }

    private fun setHomscreenRecycledView(dataFeeder: HomeFeed) {
        if (dataFeeder.dishes != null && dataFeeder.dishes.size > 0) {
            when (filterEnum) {
                Filter.ALL -> {
                    if (SAVE_ROTATION == 100) {

                        recyclerView_home_buyer.apply {
                            recyclerView_home_buyer.adapter =
                                HomeAdapter(this@HomeActivity, dataFeeder, VIEWTYPE_CHEF)
                            checkEmpty()
                        }

                    } else if (SAVE_ROTATION == 0) {

                        recyclerView_home_buyer.apply {
                            recyclerView_home_buyer.adapter =
                                HomeAdapter(this@HomeActivity, dataFeeder, VIEWTYPE_HOME)
                            checkEmpty()
                        }

                    }
                }
                Filter.SNACKS -> {
                    val dishes =
                        (DataHolder.homeFeedInstance.dishes.filter { d -> d.MealType == "Others" })
                    val updatedHomefeed = HomeFeed(
                        DataHolder.homeFeedInstance.chefs,
                        dishes,
                        DataHolder.homeFeedInstance.custommeals
                    )
                    recyclerView_home_buyer.adapter =
                        HomeAdapter(this@HomeActivity, updatedHomefeed, VIEWTYPE_HOME)
                    checkEmpty()

                }
                Filter.DINNER -> {
                    val dishes =
                        (DataHolder.homeFeedInstance.dishes.filter { d -> d.MealType == "Dinner" })
                    val updatedHomefeed = HomeFeed(
                        DataHolder.homeFeedInstance.chefs,
                        dishes,
                        DataHolder.homeFeedInstance.custommeals
                    )
                    recyclerView_home_buyer.adapter =
                        HomeAdapter(this@HomeActivity, updatedHomefeed, VIEWTYPE_HOME)
                    checkEmpty()


                }
                Filter.LUNCH -> {
                    val dishes =
                        (DataHolder.homeFeedInstance.dishes.filter { d -> d.MealType == "Lunch" })
                    val updatedHomefeed = HomeFeed(
                        DataHolder.homeFeedInstance.chefs,
                        dishes,
                        DataHolder.homeFeedInstance.custommeals
                    )
                    recyclerView_home_buyer.adapter =
                        HomeAdapter(this@HomeActivity, updatedHomefeed, VIEWTYPE_HOME)
                    checkEmpty()

                }
                Filter.BREAKFAST -> {
                    val dishes =
                        (DataHolder.homeFeedInstance.dishes.filter { d -> d.MealType == "Breakfast" })
                    val updatedHomefeed = HomeFeed(
                        DataHolder.homeFeedInstance.chefs,
                        dishes,
                        DataHolder.homeFeedInstance.custommeals
                    )
                    recyclerView_home_buyer.adapter =
                        HomeAdapter(this@HomeActivity, updatedHomefeed, VIEWTYPE_HOME)
                    checkEmpty()
                }
                Filter.VEG -> {
                    val dishes =
                        (DataHolder.homeFeedInstance.dishes.filter { d -> d.DishType == "Veg" })
                    val chefDishes =
                        HomeFeed(
                            DataHolder?.homeFeedInstance?.chefs,
                            dishes,
                            DataHolder.homeFeedInstance?.custommeals
                        )
                    recyclerView_home_buyer.adapter =
                        HomeAdapter(this@HomeActivity, chefDishes, VIEWTYPE_HOME)
                    checkEmpty()
                }
                Filter.NON_VEG -> {
                    val dishes =
                        (DataHolder.homeFeedInstance.dishes.filter { d -> d.DishType == "Non-Veg" })
                    val chefDishes =
                        HomeFeed(
                            DataHolder?.homeFeedInstance?.chefs,
                            dishes,
                            DataHolder.homeFeedInstance?.custommeals
                        )
                    recyclerView_home_buyer.adapter =
                        HomeAdapter(this@HomeActivity, chefDishes, VIEWTYPE_HOME)
                    checkEmpty()
                }
            }
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

            }

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println("Successfully executed request")
                //val gson = GsonBuilder().create()

                val gson = GsonBuilder().serializeNulls().create()
                val homeFeed = gson.fromJson(body, HomeFeed::class.java)
            }
        })

        // Soumen: Bring the control back to Ui Thread
    }


    // <editor-fold desc="Helper Method for Cell Divider">
    private fun drawRowDivider() {
        val itemDecorator = androidx.recyclerview.widget.DividerItemDecoration(
            recyclerView_home_buyer.context,
            androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
        )
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                recyclerView_home_buyer.context,
                R.drawable.divider
            )!!
        )
        recyclerView_home_buyer.addItemDecoration(itemDecorator)
    }


    // This is required to visible checkout/ proceed button when we come back from time slots activity.
    override fun onResume() {
        super.onResume()
        home_layout.requestFocus()
        Utility.manageCheckoutButton(ShoppingCart.getCartItems(), proced_lay, swipe_me)
        if (SAVE_ROTATION == 0) {
            if (recyclerView_home_buyer.adapter != null)
                recyclerView_home_buyer.adapter!!.notifyDataSetChanged()
            checkEmpty()
        }
    }

    // This is require because, in the buy button click event from the home adopter we are setting startActivityForResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        home_layout.requestFocus()
        if (requestCode == HomeAdapter.REQUEST_CODE && resultCode == HomeAdapter.REQUEST_CODE) {
            val thisDishItem = data!!.getParcelableExtra<DishItem>(Utility.DISH_ITEM_KEY)

            thisDishItem?.let {
                val thiCartItem = CartItem(thisDishItem)
                ShoppingCart.addToCart(thiCartItem)
                mCartItems = ShoppingCart.getCartItems()
            }
            recyclerView_home_buyer.adapter!!.notifyDataSetChanged()
            checkEmpty()
        }
    }


    override fun updateCount() {

        Utility.manageCheckoutButton(
            ShoppingCart.getCartItems(),
            proced_lay,
            swipe_me
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.order_history -> {
                //Toast.makeText(this,"order history", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MyOrderActivity::class.java))
            }
            R.id.contact_us -> {
                Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show()
            }
            R.id.about_us -> {
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show()
            }
            R.id.invite_neighbor -> {
                val socialMedia = SocialMedia()
                //socialMedia.shareWIthWhatsAppOrChooserResult(this,"Test string","http://youtube.com/")
                socialMedia.shareWIthWhatsAppOrChooser(
                    this,
                    "Soumen Test string",
                    "http://youtube.com/"
                )
            }
            R.id.edit_profile -> {
                startActivity(Intent(this, EditProfileActivity::class.java))
            }

        }
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout =
            this.findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        val drawer =
            this.findViewById(R.id.drawer_layout) as androidx.drawerlayout.widget.DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    private fun loadUserInfoFromDB() = Thread {
        val userId = Preference(this).getUserId()
        val buyerInfo = NextDoorDB.invoke(this).daoAccess.getBuyerInfo(userId)
        Manager.buyerInfo = buyerInfo
        // Soumen need to handel buyerInfo can be null....
    }.start()


//    private fun configureFCM() {
//
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            // Create channel to show notifications.
////            val channelId = getString(R.string.default_notification_channel_id)
////            val channelName = getString(R.string.default_notification_channel_name)
////            val notificationManager = getSystemService(NotificationManager::class.java)
////            notificationManager?.createNotificationChannel(
////                NotificationChannel(channelId,
////                channelName, NotificationManager.IMPORTANCE_LOW)
////            )
////        }
//
//
//        FirebaseMessaging.getInstance().subscribeToTopic("weather")
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    print("getInstanceId failed " + task.exception as Any?)
//                    return@OnCompleteListener
//                }
//
//                // Get new Instance ID token
//                val token = task.result?.token
//                print("new Instance ID token" + task.result?.token)
//                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
//            })
//
//
//
//
//    }


}

