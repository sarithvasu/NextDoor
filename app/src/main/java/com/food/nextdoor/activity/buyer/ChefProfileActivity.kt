package com.food.nextdoor.activity.buyer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.ChefProfileAdopter
import com.food.nextdoor.adapter.buyer.DishDetailAdapter
import com.food.nextdoor.adapter.buyer.TestimonialAdapter

import com.food.nextdoor.model.HomeFeed

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chef_profile.*
import kotlinx.android.synthetic.main.dish_detail.*
import kotlinx.android.synthetic.main.home_row.view.*
import system.Utility

class ChefProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chef_profile)

        val chefId = intent.getIntExtra(Utility.CHEF_ID_KEY,0)
        supportActionBar?.title = chefId.toString()


        val  homeFeed = Utility.DataHolder.homeFeedInstance
        configureOngoingRecycleView(homeFeed, chefId)
        configureTestimonialRecycleView(homeFeed,chefId)


        setControls(homeFeed, chefId)




    }




    private fun configureOngoingRecycleView(homeFeed: HomeFeed?, chefId: Int) {
        recyclerView_signature_dishes.layoutManager = LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)
        val dishes: List<HomeFeed.Dish> = homeFeed!!.dishes.filter { s-> chefId == s.chef_id}
        recyclerView_signature_dishes.adapter = ChefProfileAdopter(dishes)
     }


    private fun configureTestimonialRecycleView(homeFeed: HomeFeed?, chefId: Int ) {
        val chefinfo: HomeFeed.Chef = homeFeed!!.chefs.filter { s-> chefId == s.chef_id}.single()
        val testimonials : List<HomeFeed.Testimonial> = chefinfo.testimonials
        rv_testimonial.layoutManager = LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)
        rv_testimonial.adapter= TestimonialAdapter(testimonials as ArrayList<HomeFeed.Testimonial>,this@ChefProfileActivity)
    }


    private fun setControls(homeFeed: HomeFeed?, chefId: Int){
        // Bind Profile Image
        val chefinfo: HomeFeed.Chef = homeFeed!!.chefs.filter { s-> chefId == s.chef_id}.single()
        Picasso.with(this).load(chefinfo.chef_profile_image_url).into(img_chef_profile_detail)

//
        tv_chef_name_detail_profile.text = chefinfo.chef_name
        tv_flat_no_detail_profile.text = chefinfo.chef_flat_number
        tv_about_chef_detail_profile.text = chefinfo.about_chef

        tv_chef_gende_detail_profile.text = chefinfo.chef_gender

        // Bind chef_specility
          val builder = StringBuilder()
       if (chefinfo.specialized_in_veg) {
           builder.append("Veg")
       }
        if (chefinfo.is_specialized_in_non_veg) {

            if (builder.length > 0){
                builder.append(" and ")
            }

            builder.append("Non Veg")
        }
        tv_chef_specility_detail_profile.text = builder.toString()








    }


    fun loadCheckOut(view: View){
        startActivity(Intent(this, CheckoutActivity::class.java))
    }
}
