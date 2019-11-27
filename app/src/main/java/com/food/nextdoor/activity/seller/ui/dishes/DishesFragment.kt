package com.food.nextdoor.activity.seller.ui.dishes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.food.nextdoor.R
import com.food.nextdoor.activity.seller.SellerEditDish
import com.food.nextdoor.activity.seller.SellerUpload
import com.food.nextdoor.activity.seller.SellerUploadSecondary
import com.food.nextdoor.model.ChefDishFeed
import com.food.nextdoor.model.DishFeed
import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_dishes.*
import retrofit2.Call
import retrofit2.Response
import system.DishState
import system.Preference
import system.Utility


class DishesFragment : Fragment() {

    private lateinit var dishesViewModel: DishesViewModel
    private var dishState: DishState = DishState.ACTIVE


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dishesViewModel =
            ViewModelProviders.of(this).get(DishesViewModel::class.java)
        val root = inflater.inflate(R.layout.seller_dish_list, container, false)
        // val tv_add_new_dish_slr_dish_list :TextView=  root.findViewById(R.id.tv_add_new_dish_slr_dish_list)
        val rv_chef_dish_feed: RecyclerView = root.findViewById(R.id.rv_chef_dish_feed)
        val tv_active_dish_seller_dish_list: TextView =
            root.findViewById(R.id.tv_active_dish_seller_dish_list)
        val tv_all_dishes_seller_dish_list: TextView =
            root.findViewById(R.id.tv_all_dishes_seller_dish_list)
        val tv_signature_dish_seller_dish_list: TextView =
            root.findViewById(R.id.tv_signature_dish_seller_dish_list)
        val linearLayout22:LinearLayout=root.findViewById(R.id.linearLayout22)
        Utility.changeColorOfChidSeller(linearLayout22,tv_active_dish_seller_dish_list)
        tv_active_dish_seller_dish_list.setOnClickListener {
            dishState = DishState.ACTIVE
            Utility.changeColorOfChidSeller(linearLayout22,it)
            callChefFeed()
            //callFromassetFeed()
        }
        tv_all_dishes_seller_dish_list.setOnClickListener {
            dishState = DishState.IN_ACTIVE
            Utility.changeColorOfChidSeller(linearLayout22,it)
            callChefFeed()
            //callFromassetFeed()
        }
        tv_signature_dish_seller_dish_list.setOnClickListener {
            dishState = DishState.SIGNATURE
            Utility.changeColorOfChidSeller(linearLayout22,it)
             callChefFeed()
            //callFromassetFeed()
        }
        rv_chef_dish_feed.apply {
            layoutManager =  androidx.recyclerview.widget.LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            // callChefFeed()
          //  callFromassetFeed()
        }

       // Handler().postDelayed(Runnable {  callFromassetFeed() },500)

        val floatingActionButton: FloatingActionButton =
            root.findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            startActivity(Intent(activity, SellerUpload::class.java))
        }
        /*  val textView: TextView = root.findViewById(R.id.text_dashboard)
          dishesViewModel.text.observe(this, Observer {
              textView.text = it
          })*/
        return root
    }

    override fun onResume() {
        super.onResume()
        callChefFeed()
    }
    private fun callFromassetFeed() {
        val jsonstring = activity?.application?.assets?.open("chef_feed.json")?.bufferedReader().use { it?.readText() }
        val gson = GsonBuilder().serializeNulls().create()
         val chefDishFeed = gson.fromJson(jsonstring, ChefDishFeed::class.java)
        when (dishState) {
            DishState.ACTIVE -> {
                if(chefDishFeed!=null) {
                    rv_chef_dish_feed.apply {
                        adapter = DishesAdapter(
                            activity as Context,
                            chefDishFeed.ActiveDishFeed as ArrayList<DishFeed>,DishState.ACTIVE
                        )
                    }
                }
            }
            DishState.IN_ACTIVE->{
                if(chefDishFeed!=null) {
                    rv_chef_dish_feed.apply {
                        adapter = DishesAdapter(
                            activity as Context,
                            chefDishFeed.InactiveDishFeed as ArrayList<DishFeed>,DishState.IN_ACTIVE
                        )
                    }
                }
            }
            DishState.SIGNATURE->{
                if(chefDishFeed!=null) {
                    rv_chef_dish_feed.apply {
                        adapter =null
                    }
                }
            }
        }
    }

    private fun callChefFeed() {

        val cheffeedService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        cheffeedService?.getChefDishFeedByChefId(
            "application/json",
            "application/json", Preference(activity as Context).getUserChefId()
        ).also {
            it?.enqueue(object : retrofit2.Callback<ChefDishFeed> {
                override fun onFailure(call: Call<ChefDishFeed>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ChefDishFeed>,
                    response: Response<ChefDishFeed>
                ) {
                    if (response.isSuccessful) {
                        when (dishState) {
                            DishState.ACTIVE -> {
                            if(response.body()!=null) {
                                rv_chef_dish_feed.apply {
                                    adapter = DishesAdapter(
                                        activity as Context,
                                        response.body()!!.ActiveDishFeed as ArrayList<DishFeed>,DishState.ACTIVE
                                    )
                                }
                            }
                        }
                        DishState.IN_ACTIVE->{
                            if(response.body()!=null) {
                                rv_chef_dish_feed.apply {
                                    adapter = DishesAdapter(
                                        activity as Context,
                                        response.body()!!.InactiveDishFeed as ArrayList<DishFeed>,DishState.IN_ACTIVE
                                    )
                                }
                            }
                        }
                            DishState.SIGNATURE->{
                                if(response.body()!=null) {
                                    rv_chef_dish_feed.apply {
                                        adapter =null
                                    }
                                }
                            }
                        }
                    }
                }

            })
        }
    }
}