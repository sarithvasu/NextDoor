package com.food.nextdoor.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.AddApartment

import com.food.nextdoor.activity.buyer.RegistrationActivity
import com.food.nextdoor.activity.buyer.Verification
import com.food.nextdoor.adapter.buyer.HomeAdapter
import com.food.nextdoor.model.*
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_launch.*
import kotlinx.android.synthetic.main.home.*
import system.Preference
import system.Utility
import system.Validation

import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.InputType
import android.text.method.KeyListener
import android.view.inputmethod.InputMethod
import com.food.nextdoor.database.NextDoorDB
import kotlinx.android.synthetic.main.busy_bar_layout.*
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.home_nav.*
import system.DataHolder
import java.util.regex.Pattern


class LaunchActivity : AppCompatActivity() {

    private lateinit var dataFeeder: LauncherFeed

    private var mApartmentName: String = ""
    private var launcherApartment:LauncherApartment?=null

    //private val mUserInfo = Preference(this).getUserBasicInfo()
    //private val mUserId = mUserInfo[Utility.USER_ID]!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Un-Registered user

            setContentView(R.layout.activity_launch)
            getLauncherFeed()
            /* test call*/

            textView41.visibility=View.GONE
            tv_add_new_apartment_activity_launch.visibility=View.GONE
            val apartmentNameFeed = this.readFromAsset("apartment_name_feed.json")
            searchView.setTag(searchView.getKeyListener());
            apartmentNameFeed.let {


                }

            spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val cityAdapter = ArrayAdapter<City>(this@LaunchActivity, android.R.layout.simple_dropdown_item_1line, dataFeeder.cities.filter { city: City -> (spinner2.selectedItem as State).StateId==city.StateId})
                    spinner3.adapter=cityAdapter
                }

            }
            spinner3.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    searchView.setText("")
                    searchView.setKeyListener(searchView.getTag()as KeyListener);
                    val apartments: List<LauncherApartment> = dataFeeder.apartments.filter { apartment:LauncherApartment ->(spinner3.selectedItem as City).CityId==apartment.CityId }
                    val dataAdapter = ArrayAdapter<LauncherApartment>(this@LaunchActivity, android.R.layout.simple_dropdown_item_1line, apartments)
                    searchView.setAdapter(dataAdapter)
                    searchView.threshold=1
                }

            }

           searchView.setOnItemClickListener { adapterView, view, i, l ->
               tv_proceed_add_apartment_activity_launch.text="PROCEED"
               launcherApartment=adapterView.getItemAtPosition(i) as LauncherApartment

               searchView.setKeyListener(null);
               start()
              /* val view = this.currentFocus
               view?.let { v ->
                   val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                   imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
               }*/
           }

            searchView.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_UP ->{  if(searchView.getCompoundDrawables()[2]!=null&&event.getRawX() >= (searchView.getRight() - searchView.getCompoundDrawables()[2].getBounds().width())) {
                            searchView.setText("")
                            launcherApartment=null

                            searchView.setKeyListener(searchView.getTag()as KeyListener);

                            tv_proceed_add_apartment_activity_launch.text="Add Apartment"
                        }
                        }
                    }

                    return v?.onTouchEvent(event) ?: true
                }
            });
            searchView.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                  /*  if(p0.toString().equals("")){
                        tv_proceed_add_apartment_activity_launch.text="Add Apartment"
                        textView41.visibility = View.GONE
                        tv_add_new_apartment_activity_launch.visibility = View.GONE
                        }*/
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if( p0!!.length>1) {

                        if(!searchView.isPopupShowing ) {
                            textView41.visibility = View.VISIBLE
                            tv_add_new_apartment_activity_launch.visibility = View.VISIBLE
                        }
                        searchView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, R.drawable.remove, 0);
                    }
                    else{
                        textView41.visibility = View.GONE
                        tv_add_new_apartment_activity_launch.visibility = View.GONE
                        searchView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
                    }

                }

            })

            createDummyService()
            // set On ClickListener for Get Started
            tv_proceed_add_apartment_activity_launch.setOnClickListener { start() }
            tv_add_new_apartment_activity_launch.setOnClickListener{
                val intent1=Intent(this, AddApartment::class.java)
                startActivity(intent1)
            }


    }



    private fun start() {

        if(launcherApartment!=null){

            if( Preference(this).getUserId()!=0) {
                val userId = Preference(this).getUserId()
                val buyerInfo = NextDoorDB.invoke(this).daoAccess.getBuyerInfo(userId)
                if(launcherApartment!!.ApartmentId==buyerInfo.apartmentId){
                    Utility.createCustomTwoButtonDialog(this,"You have selected the same Apartment. Are you sure you want to continue?","Yes, I'm Sure","NO",View.OnClickListener{
                        startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
                        finish()
                    })

                }else {
                    val intent1 = Intent(this, RegistrationActivity::class.java)
                    intent1.putExtra("apartment", launcherApartment)
                    intent1.putExtra("phone", buyerInfo.mobileNumber)
                    intent1.putExtra("from_edit", "from_edit")
                    startActivity(intent1)
                }
            }else{
                val intent1=Intent(this, RegistrationActivity::class.java)
                intent1.putExtra("apartment", launcherApartment)
                intent1.putExtra("phone", intent.getStringExtra("phone"))
                startActivity(intent1)
            }




        }
        else{
            val intent2=Intent(this, AddApartment::class.java)

            startActivity(intent2)

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        var s=""
        s="sarith vasu"
    }

    override fun onResume() {
        super.onResume()
        var s=""
        s="sarith vasu"
    }

    override fun onStart() {
        super.onStart()
        var s=""
        s="sarith vasu"

    }

    override fun onPause() {
        super.onPause()
        var s=""
        s="sarith vasu"

    }

    override fun onStop() {
        super.onStop()
        var s=""
        s="sarith vasu"


    }

    override fun onRestart() {
        super.onRestart()
        var s=""
        s="sarith vasu"

    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        var s=""
        s="sarith vasu"


    }



    private fun readFromAsset(fileName: String): ApartmentNameFeed {
        val jsonString = application.assets.open(fileName).bufferedReader().use{
            it.readText()
        }

        val gson = GsonBuilder().serializeNulls().create()
        val feed = gson.fromJson(jsonString, ApartmentNameFeed::class.java)

        // Return
        return feed
    }
    fun getLauncherFeed(){
        val laucherFeedService: RetrofitService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        laucherFeedService.getLaucherFeedData().also {
            it.enqueue(object : retrofit2.Callback<LauncherFeed> {
                override fun onFailure(call: retrofit2.Call<LauncherFeed>, t: Throwable) {
                    createDummyService()
                }

                override fun onResponse(call: retrofit2.Call<LauncherFeed>, response: retrofit2.Response<LauncherFeed>) {
                    if (response.isSuccessful) {
                        Log.e("", "xc';fd" + response.body())
                         dataFeeder = response.body()!!
                       setData()



                    }
                }
            })
        }
    }

    private fun setData() {


        val stateAdapter = ArrayAdapter<State>(this@LaunchActivity, android.R.layout.simple_dropdown_item_1line, dataFeeder.states)
        spinner2.adapter=stateAdapter
        val citiAdapter = ArrayAdapter<City>(this@LaunchActivity, android.R.layout.simple_dropdown_item_1line, dataFeeder.cities.filter { city: City -> (spinner2.selectedItem as State).StateId==city.StateId })
        spinner3.adapter=citiAdapter
        val apartments: List<LauncherApartment> = dataFeeder.apartments.filter { apartment:LauncherApartment ->(spinner3.selectedItem as City).CityId==apartment.CityId }
        val dataAdapter = ArrayAdapter<LauncherApartment>(this@LaunchActivity, android.R.layout.simple_dropdown_item_1line, apartments)
        searchView.setAdapter(dataAdapter)
        searchView.threshold=1
    }

    fun createDummyService(){
        val s="{\"countries\":[{\"CountryId\":1,\"CountryName\":\"India\"},{\"CountryId\":2,\"CountryName\":\"Dubai\"}],\"states\":[{\"CountryId\":1,\"StateId\":1,\"StateName\":\"Karnataka\"},{\"CountryId\":1,\"StateId\":2,\"StateName\":\"West Bengal\"}],\"cities\":[{\"StateId\":1,\"CityId\":1,\"CityName\":\"Bangalore\"},{\"StateId\":2,\"CityId\":2,\"CityName\":\"Chandrakona Town\"}],\"apartments\":[{\"ApartmentId\":1,\"CityId\":1,\"ApartmentName\":\"Provident welworth city\",\"PinCode\":\"561203\",\"ActiveUserCount\":6,\"ActiveChefCount\":1},{\"ApartmentId\":2,\"CityId\":1,\"ApartmentName\":\"Prestige Royale Gardens\",\"PinCode\":\"560064\",\"ActiveUserCount\":0,\"ActiveChefCount\":2}]}"

        val gson: Gson = Gson()
        dataFeeder= gson.fromJson(s,LauncherFeed::class.java)
        setData()
    }

}
