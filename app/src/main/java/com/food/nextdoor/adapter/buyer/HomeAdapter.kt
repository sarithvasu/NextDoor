package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.ChefProfileActivity
import com.food.nextdoor.activity.buyer.DishDetailActivity
import com.food.nextdoor.activity.buyer.HomeActivity
import com.food.nextdoor.dropbox.DropboxClientFactory
import com.food.nextdoor.dropbox.ImagePiccassoRequestHadler
import com.food.nextdoor.dropbox.PicassoClient
import com.food.nextdoor.listeners.OnActiveDishSelected
import com.food.nextdoor.listeners.OnItemCountListener
import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_row.view.*
import kotlinx.android.synthetic.main.home_row_secondary.view.*
import kotlinx.android.synthetic.main.registration.*
import system.*
import kotlin.random.Random


class HomeAdapter(val context: Context, private val homeFeed: HomeFeed, var viewTypeHome: Int) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private lateinit var listener: OnItemCountListener
    private lateinit var dishSelected: OnActiveDishSelected
    private lateinit var m_listOfCartItem: ArrayList<CartItem>



    companion object {
        val REQUEST_CODE: Int = 100
    }


    override fun getItemCount(): Int {
        if (viewTypeHome == HomeActivity.VIEWTYPE_CHEF)
            return homeFeed.chefs.count()

        return homeFeed.dishes.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        this.listener = context as HomeActivity
        this.dishSelected= context as HomeActivity
        if (viewTypeHome == HomeActivity.VIEWTYPE_CHEF) {
            val layoutInflater = LayoutInflater.from(parent.context)
            m_listOfCartItem = ShoppingCart.getCartItems()



            val cellForRow = layoutInflater.inflate(R.layout.home_row_secondary, parent, false)
            return HomeFeedChefViewHolder(cellForRow, homeFeed)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)
            m_listOfCartItem = ShoppingCart.getCartItems()



            val cellForRow = layoutInflater.inflate(R.layout.home_row, parent, false)
            return HomeFeedViewHolder(cellForRow, homeFeed)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        if (holder is HomeFeedViewHolder) {
            setControlHomeFeed(homeFeed, position, holder)
        } else if (holder is HomeFeedChefViewHolder) {

            val chefinfo: HomeFeed.Chef = homeFeed.chefs[position]


            //Bind Chef Image
           /* val d = ContextCompat.getDrawable(context, R.drawable.profile_p_holder)
            val currentStrokeColor = Color.argb(255, Random.nextInt(200), Random.nextInt(200), Random.nextInt(200))
            val drawableNew1: Drawable = setTint(d, currentStrokeColor)*/
            if (chefinfo.ProfileImageUrl.isEmpty()) {
                holder.itemView.img_chef_profile_home_row_sec.setImageResource(R.drawable.profile_p_holder)
                holder.itemView.img_chef_profile_home_row_sec.setColorFilter(Color.argb(255, Random.nextInt(200), Random.nextInt(200), Random.nextInt(200)))

            }
            else
            {
                PicassoClient.init(holder.itemView.context, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
                PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(Utility.SLASH +chefinfo.ProfileImageUrl))!!
                    .placeholder(R.drawable.dish_3)
                    .error(R.drawable.dish_3)
                    .into(holder.itemView.img_chef_profile_home_row_sec)
            }

            holder.itemView.tv_chef_name_home_row_sec.text = chefinfo.FullName
            holder.itemView.tv_chef_address_with_flat_no_home_row_sec.text = chefinfo.FlatNumber
            holder.itemView.tv_about_chef_home_row_sec.text = chefinfo.AboutChef
            holder.itemView.tv_dish_type_veg_home_row_sec.text =  " Veg " + chefinfo.VegCount
            holder.itemView.tv_dish_type_nonveg_home_row_sec.text = " Non-Veg " + chefinfo.NonVegCount
            holder.itemView.tv_active_dishes_home_row_sec.text = " Active Dishes " + chefinfo.ActiveDishCount



            val activeDishes: List<HomeFeed.Dish> = homeFeed.dishes.filter { d-> d.ChefId == chefinfo.ChefId}

            // Set ClickListener for active signatureDishes
            holder.itemView.tv_active_dishes_home_row_sec.setOnClickListener {
                val activeDishes: List<HomeFeed.Dish> = homeFeed.dishes.filter { d-> d.ChefId == chefinfo.ChefId}
              dishSelected.updateDishesByChef(activeDishes)
            }

            // Set ClickListener for non veg signatureDishes
            holder.itemView.tv_dish_type_nonveg_home_row_sec.setOnClickListener {
                val vegDishes = activeDishes.filter { d -> d.DishType == "Non_Veg" }
                dishSelected.updateDishesByChef(vegDishes)

            }

            // Set ClickListener for veg signatureDishes
            holder.itemView.tv_dish_type_veg_home_row_sec.setOnClickListener {
                val vegDishes = activeDishes.filter { d -> d.DishType == "Veg" }
                dishSelected.updateDishesByChef(vegDishes)
            }
        }
    }

    private fun setControlHomeFeed(homeFeed: HomeFeed, position: Int, holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        val chefinfo: HomeFeed.Chef =
            homeFeed!!.chefs.filter { s -> homeFeed.dishes.get(position).ChefId == s.ChefId }.get(0)
        val dishInfo: HomeFeed.Dish = homeFeed!!.dishes.get(position)

        // Bind Dish Image
    //    Picasso.with(holder.itemView.context).load(dishInfo.DishImageUrl).into(holder.itemView.img_dish_image_home)
        /*val d1 = ContextCompat.getDrawable(context, R.drawable.dish_3)
        val currentStrokeColor1 = Color.argb(255, Random.nextInt(200), Random.nextInt(200), Random.nextInt(200))
        val drawableNew2: Drawable = setTint(d1, currentStrokeColor1)*/
        if (dishInfo?.DishImageUrl?.isEmpty()) {
            holder.itemView.img_dish_image_home.setImageResource(R.drawable.dish_3)
            holder.itemView.img_dish_image_home.setColorFilter(Color.argb(255, Random.nextInt(200), Random.nextInt(200), Random.nextInt(200)))

        }
        else {

            PicassoClient.init(holder.itemView.context, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
            PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(Utility.SLASH +dishInfo.DishImageUrl))!!
                .placeholder(R.drawable.dish_3)
                .error(R.drawable.dish_3)
                .into(holder.itemView.img_dish_image_home)
        }
        // set Dish Symbol
        val dishSymbol = Utility.setDishSymbol(dishInfo.DishType, holder.itemView.img_dish_symbol_home.context)
        holder.itemView.img_dish_symbol_home.setImageDrawable(dishSymbol)

        holder.itemView.tv_dish_name_home.text = dishInfo.DishName
        holder.itemView.tv_dish_price_home.text = " Rs. " + dishInfo.UnitPrice.toString()
        holder.itemView.tv_serving_home.text = (dishInfo.ServingsPerPlate.toString())
        holder.itemView.tv_chef_name_with_flat_number_home.text =
            chefinfo.FullName + " | " + chefinfo.FlatNumber
        holder.itemView.tv_dish_available_time_home.text =
            (Utility.standardDateToTimeSlot(dishInfo.DishAvailableStartTime) + " | " + Utility.standardDateToTimeSlot(dishInfo.DishAvailableEndTime))
        //Bind Chef Image
       // val d = ContextCompat.getDrawable(context, R.drawable.profile_p_holder)
        //val drawableNew1: Drawable = setTint(d, currentStrokeColor1)
        if (chefinfo.ProfileImageUrl.isEmpty()) {
            holder.itemView.img_chef_profile_home.setImageResource(R.drawable.profile_p_holder)
            holder.itemView.img_chef_profile_home.setColorFilter(Color.argb(255, Random.nextInt(200), Random.nextInt(200), Random.nextInt(200)))

        }
        else {
            Picasso.with(holder.itemView.context).load(chefinfo.ProfileImageUrl)
                .placeholder(R.drawable.profile_p_holder)
                .fit()
                .into(
                    holder.itemView.img_chef_profile_home
                )
            PicassoClient.init(holder.itemView.context, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
            PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(Utility.SLASH +chefinfo.ProfileImageUrl))!!
                .placeholder(R.drawable.profile_p_holder)
                .error(R.drawable.profile_p_holder)
                .into(holder.itemView.img_chef_profile_home)
        }
        holder.itemView.tv_num_of_dish_reviews_home.text = dishInfo.DishAverageRating.toString()
        holder.itemView.tv_dish_sold_home.text = (dishInfo.DishSold).toString()
        holder.itemView.tv_dish_available_home.text = (dishInfo.AvailableQuantity.toString())
        // Seting the tag for dish Id for detail screen
        holder.itemView.setTag(dishInfo.DishId)


        // Set ClickListener for dish Image
        holder.itemView.img_dish_image_home.setOnClickListener {
            val intent = Intent(holder.itemView.img_dish_image_home.context, DishDetailActivity::class.java)
            intent.putExtra(Utility.DISH_ID_KEY, holder.itemView.getTag() as Int)
            intent.putExtra(Utility.SELLER_USER_ID_KEY, chefinfo.SellerUserId)
            holder.itemView.img_dish_image_home.context.startActivity(intent)
        }


        // Set ClickListener for chef profile Image
        holder.itemView.img_chef_profile_home.setOnClickListener {
            val intent = Intent(holder.itemView.img_chef_profile_home.context, ChefProfileActivity::class.java)
            intent.putExtra(Utility.CHEF_ID_KEY, chefinfo.ChefId as Int)
            intent.putExtra(Utility.SELLER_USER_ID_KEY, chefinfo.SellerUserId as Int)
            holder.itemView.img_chef_profile_home.context.startActivity(intent)
        }

        // Disable buy button and enable (+) and (-) button If the item is already added to the cart



        // Set ClickListener for buy Button
        holder.itemView.btn_add_remove.configDishItem(dishInfo,listener)
    }







}


class HomeFeedViewHolder(val view: View, val homeFeed: HomeFeed?) : RecyclerView.ViewHolder(view)



class HomeFeedChefViewHolder(val view: View, val homeFeed: HomeFeed?) : RecyclerView.ViewHolder(view)






