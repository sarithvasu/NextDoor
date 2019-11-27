package com.food.nextdoor.activity.seller.ui.dishes

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.CheckoutActivity
import com.food.nextdoor.activity.buyer.ChefProfileActivity
import com.food.nextdoor.activity.seller.SellerEditActiveDIsh
import com.food.nextdoor.activity.seller.SellerEditDish
import com.food.nextdoor.activity.seller.SellerUploadSecondary
import com.food.nextdoor.adapter.buyer.OrderDetailViewHolder
import com.food.nextdoor.dropbox.DropboxClientFactory
import com.food.nextdoor.dropbox.ImagePiccassoRequestHadler
import com.food.nextdoor.dropbox.PicassoClient
import com.food.nextdoor.model.ChefDishFeed
import com.food.nextdoor.model.DishFeed
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.checkout_row.view.*
import kotlinx.android.synthetic.main.home_row.view.*
import kotlinx.android.synthetic.main.registration.*
import kotlinx.android.synthetic.main.seller_dish_list_row.view.*
import retrofit2.Call
import retrofit2.Response
import system.DishState
import system.Utility
import system.Utility.Companion.SLASH
import kotlin.random.Random

class DishesAdapter(var contex:Context,var dishFeeds: ArrayList<DishFeed>,val dishState:DishState) : RecyclerView.Adapter<DishesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dishRowView = layoutInflater.inflate(R.layout.seller_dish_list_row, parent, false)
        return DishesViewHolder(dishRowView)
    }

    override fun getItemCount(): Int {
       return dishFeeds.size
    }

    override fun onBindViewHolder(holder: DishesViewHolder, position: Int) {
        val dishFeed=dishFeeds[position]
        holder.view.tv_dish_name_seller_dish_list_row.text = dishFeed.DishName
        holder.view.tv_unit_price_seller_dish_list_row.text="Rs ${dishFeed.UnitPrice}"
        holder.view.tv_other_charges_seller_dish_list_row.text="Rs ${dishFeed.DeliveryCharge+dishFeed.PackingCharge}"
        holder.view.tv_dish_timings_seller_dish_list_row.text = "${Utility.standardDateToTimeSlot(dishFeed.DishAvailableStartTime)} - ${Utility.standardDateToTimeSlot(
            dishFeed.DishAvailableEndTime)}"
        holder.view.tv_meal_type_seller_dish_list_row.text=dishFeed.MealType
        holder.view.tv_servings_per_plate_seller_dish_list_row.text="${dishFeed.ServingsPerPlate} Person/s"
        holder.view.tv_delivery_charges_seller_dish_list_row.text="${dishFeed.DeliveryCharge}"
        holder.view.tv_parcel_charges_seller_dish_list_row.text=dishFeed.PackingCharge.toString()
        holder.view.tv_quantity_preparing_seller_dish_list_row.text=dishFeed.QuantityPreparing.toString()

        if(dishState==DishState.ACTIVE){
            holder.view.tv_activate_seller_dish_list_row.text="De Active"
            holder.view.tv_change_and_activate_seller_dish_list_row.text="Edit"
            holder.view.tv_activate_seller_dish_list_row.setOnClickListener {
                callDeActiveByDishId(dishFeed.DishId)
            }
            holder.view.tv_change_and_activate_seller_dish_list_row.setOnClickListener {
                val intent = Intent(holder.itemView.tv_activate_seller_dish_list_row.context, SellerEditActiveDIsh::class.java)
                intent.putExtra(Utility.DISH_FEED, dishFeed)
                holder.view.tv_change_and_activate_seller_dish_list_row.context.startActivity(intent)
            }

        }
        else if(dishState==DishState.IN_ACTIVE){
            holder.view.tv_activate_seller_dish_list_row.text="Active"
            holder.view.tv_change_and_activate_seller_dish_list_row.text="Change and Active"
            if(dishFeed.IsActivatedBefore){
                holder.view.tv_change_and_activate_seller_dish_list_row.isEnabled=true
                holder.view.tv_change_and_activate_seller_dish_list_row.setOnClickListener {
                    val intent = Intent(holder.itemView.tv_activate_seller_dish_list_row.context, SellerEditDish::class.java)
                    intent.putExtra(Utility.DISH_FEED, dishFeed)
                    holder.view.tv_change_and_activate_seller_dish_list_row.context.startActivity(intent)
                }
                holder.view.tv_activate_seller_dish_list_row.setOnClickListener {
                    callActiveByDishId(dishFeed.DishId)
                }
            }
            else{
                holder.view.tv_activate_seller_dish_list_row.setOnClickListener {
                    val intent = Intent(holder.itemView.tv_activate_seller_dish_list_row.context, SellerUploadSecondary::class.java)
                    intent.putExtra(Utility.DISH_ID_KEY, dishFeed.DishId as Int)
                    holder.view.tv_activate_seller_dish_list_row.context.startActivity(intent)
                }
                holder.view.tv_change_and_activate_seller_dish_list_row.isEnabled=false

            }
        }
        if (dishFeed?.DishImageUrl?.isEmpty()) {
            holder.view.dish_image_seller_dish_list_row.setImageResource(R.drawable.dish_3)
            holder.view.dish_image_seller_dish_list_row.setColorFilter(Color.argb(255, Random.nextInt(200), Random.nextInt(200), Random.nextInt(200)))

        }
        else{
        PicassoClient.init(holder.view.context, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
        PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(SLASH+dishFeed.DishImageUrl))!!
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(holder.view.dish_image_seller_dish_list_row)
            }
    }

    private fun callActiveByDishId(dishId: Int) {
        val activeByDishIdService=RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        activeByDishIdService?.putActivateTheDishByDishId("application/json",
            "application/json",dishId).also {
            it?.enqueue(object : retrofit2.Callback<ResponseCode> {
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
    private fun callDeActiveByDishId(dishId: Int) {
        val activeByDishIdService=RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        activeByDishIdService?.putInActivateTheDishByDishId("application/json",
            "application/json",dishId).also {
            it?.enqueue(object : retrofit2.Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    if(response.isSuccessful)
                    Utility.createCustomDialog(contex,"Deactivated",true)
                }

            })
        }
    }
}
class DishesViewHolder(var view: View) : RecyclerView.ViewHolder(view)
