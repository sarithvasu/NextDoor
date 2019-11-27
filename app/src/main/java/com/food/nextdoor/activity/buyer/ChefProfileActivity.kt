package com.food.nextdoor.activity.buyer
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.view.View
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.ChefDishLibraryAdapter
import com.food.nextdoor.adapter.buyer.ChefProfileAdopter
import com.food.nextdoor.adapter.buyer.TestimonialAdapter
import com.food.nextdoor.dropbox.DropboxClientFactory
import com.food.nextdoor.dropbox.ImagePiccassoRequestHadler
import com.food.nextdoor.dropbox.PicassoClient
import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.model.post.Following
import com.food.nextdoor.model.post.PostUserAction
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chef_profile.*
import kotlinx.android.synthetic.main.home_row.view.*
import retrofit2.Call
import retrofit2.Response
import system.DataHolder
import system.Preference
import system.Utility
import kotlin.random.Random

class ChefProfileActivity : AppCompatActivity() {


private var chefId =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chef_profile)

         chefId = intent.getIntExtra(Utility.CHEF_ID_KEY,0)
        val sellerUserId = intent.getIntExtra(Utility.SELLER_USER_ID_KEY,0)
        supportActionBar?.title = chefId.toString()


        //val  homeFeed = DataHolder.homeFeedInstance
        configureOngoingRecycleView(DataHolder.homeFeedInstance, chefId)
        configureTestimonialRecycleView(DataHolder.homeFeedInstance,chefId)
        configureDishGalleryRecycleView(DataHolder.homeFeedInstance)
        setControls(DataHolder.homeFeedInstance, chefId)


        //manju setonclicklistner without onclick event in XML
        tv_view_ongoing_dishes_chef_profile.setOnClickListener {
            val intent = Intent(tv_view_ongoing_dishes_chef_profile.context,HomeActivity :: class.java)
            tv_view_ongoing_dishes_chef_profile.context.startActivity(intent)
        }
        tv_reviews_profile.setOnClickListener {
            val intent = Intent(tv_reviews_profile.context,ReadReviewActivity::class.java)
            tv_reviews_profile.context.startActivity(intent)
        }
        tv_num_of_reviews_profile.setOnClickListener {
            val intent = Intent(tv_num_of_reviews_profile.context, ReadReviewActivity::class.java)
            tv_num_of_reviews_profile.context.startActivity(intent)
        }

        btn_follow_profile.setOnClickListener {
           val followUnfollow = PostUserAction.instance.followUnfollow
            followUnfollow.sellerId =sellerUserId

            followUnfollow.date = Utility.currentDate(Utility.DD_MM_YYYY)
            followUnfollow.time =Utility.currentDate(Utility.HH_MM_SS)

            if (btn_follow_profile.text.toString() == "Follow +"){
                btn_follow_profile.text = "Following"
                callAddUnFollowing()
                followUnfollow.action = "Follow"

            } else if (btn_follow_profile.text.toString() == "Following") {
                btn_follow_profile.text = "Follow +"
                callAddFollowing()
                followUnfollow.action = "Un-Follow"
            }
        }
    }

    private fun callAddUnFollowing() {
        val addUnFollwingService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        addUnFollwingService?.putUnFollowChef(    "application/json",
            "application/json", Preference(this).getUserId(),chefId).also {
            it?.enqueue(object: retrofit2.Callback<ResponseCode>{
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {

                }

            })
        }
    }

    private fun callAddFollowing() {
        var follwings:ArrayList<Following> = ArrayList()
        follwings.add(Following( Preference(this).getUserId(),chefId))
        val addUnFollwingService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        addUnFollwingService?.postAddNewFollowing(    "application/json",
            "application/json",follwings).also {
            it?.enqueue(object: retrofit2.Callback<ResponseCode>{
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {

                }

            })
        }
    }


    private fun configureOngoingRecycleView(homeFeed: HomeFeed, chefId: Int) {
        recyclerView_signature_dishes.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.RecyclerView.HORIZONTAL,
            false
        )



        val chefInfo = homeFeed.chefs.filter { s-> chefId == s.ChefId}.single()
        val signatureDishes : List<HomeFeed.SignatureDish> = chefInfo.signatureDishes
        recyclerView_signature_dishes.adapter = ChefProfileAdopter(this,signatureDishes, homeFeed)


     }


    private fun configureTestimonialRecycleView(homeFeed: HomeFeed, chefId: Int ) {
        val chefinfo: HomeFeed.Chef = homeFeed.chefs.filter { s-> chefId == s.ChefId}.single()
        val testimonials : List<HomeFeed.Testimonial> = chefinfo.testimonials
        rv_testimonial.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.RecyclerView.HORIZONTAL,
            false
        )
        rv_testimonial.adapter= TestimonialAdapter(testimonials as ArrayList<HomeFeed.Testimonial>,this@ChefProfileActivity)
    }
    fun setTint(d: Drawable?, color: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(d!!)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }
    private fun configureDishGalleryRecycleView(homeFeed: HomeFeed ) {
        val dishes: List<HomeFeed.Dish> = homeFeed.dishes
        //rv_testimonial.layoutManager = LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)
        // rv_testimonial.adapter= ChefDishLibraryAdapter(this@ChefProfileActivity, signatureDishes)


        val layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(
            2,
            androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
        )
        rv_dish_gallery.layoutManager = layoutManager
        rv_dish_gallery.apply {
            rv_dish_gallery.adapter = ChefDishLibraryAdapter(this@ChefProfileActivity, dishes)
        }

    }

        private fun setControls(homeFeed: HomeFeed, chefId: Int){
        // Bind Profile Image
        val chefinfo: HomeFeed.Chef = homeFeed.chefs.filter { s-> chefId == s.ChefId}.single()

        val d = ContextCompat.getDrawable(this, R.drawable.profile_p_holder)
        val currentStrokeColor = Color.argb(255, Random.nextInt(200), Random.nextInt(200), Random.nextInt(200))
        val drawableNew1: Drawable = setTint(d, currentStrokeColor)
        if (chefinfo.ProfileImageUrl.isEmpty())
            img_chef_profile_detail.setBackground(drawableNew1)
        else
        {
            PicassoClient.init(this, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
            PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(Utility.SLASH +chefinfo.ProfileImageUrl))!!
                .placeholder(R.drawable.dish_3)
                .error(R.drawable.dish_3)
                .into(img_chef_profile_detail)
        }

//
        tv_chef_name_detail_profile.text = chefinfo.FullName
        tv_flat_no_detail_profile.text = chefinfo.FlatNumber
        tv_about_chef_detail_profile.text = chefinfo.AboutChef
        tv_num_of_reviews_profile.text = chefinfo.ChefTotalDishReview.toString()
        tv_chef_ratings_profile.text = chefinfo.ChefAverageRating.toString()
        tv_chef_followers_profile.text = chefinfo.ChefFollower.toString()
        tv_chef_gende_detail_profile.text = chefinfo.Gender

        // Bind chef_specility
          val builder = StringBuilder()
       if (chefinfo.IsSpecializedInVeg) {
           builder.append("Veg")
       }
        if (chefinfo.IsSpecializedInNonVeg) {

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
