package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R
import com.food.nextdoor.dropbox.DropboxClientFactory
import com.food.nextdoor.dropbox.ImagePiccassoRequestHadler
import com.food.nextdoor.dropbox.PicassoClient
import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chef_profile_row.view.*
import kotlinx.android.synthetic.main.home_row.view.*
import system.CartItem
import system.ShoppingCart
import system.Utility

//class ChefProfileAdopter (val signatureDishes: List<HomeFeed.Dish>, val signatureDishes : List<HomeFeed.SignatureDish>) : androidx.recyclerview.widget.RecyclerView.Adapter<ChefProfileViewHolder>() {

class ChefProfileAdopter (context: Context, private val signatureDishes : List<HomeFeed.SignatureDish>, private val homeFeed: HomeFeed) : androidx.recyclerview.widget.RecyclerView.Adapter<ChefProfileViewHolder>() {

    private lateinit var m_listOfCartItem: ArrayList<CartItem>


    override fun getItemCount(): Int {
        return signatureDishes.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChefProfileViewHolder {
        m_listOfCartItem = ShoppingCart.getCartItems()

        val layoutInflater = LayoutInflater.from(parent.context)
        val rowObject = layoutInflater.inflate(com.food.nextdoor.R.layout.chef_profile_row, parent, false)
        return ChefProfileViewHolder(rowObject)
    }



    override fun onBindViewHolder(holder: ChefProfileViewHolder, position: Int) {
        // Bind Dish Image
        val signatureDish =  signatureDishes.get(position)
        PicassoClient.init(holder.view.context, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
        PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(Utility.SLASH +signatureDish.DishImageUrl))!!
            .placeholder(R.drawable.dish_3)
            .error(R.drawable.dish_3)
            .into(holder.view.img_signature_dish)
        holder.view.tv_signature_dish_name.text = signatureDish.DishName
        holder.view.tv_signature_dish_price.text = "Rs." + signatureDish.UnitPrice.toString()
        holder.view.tv_signature_dish_rating.text = signatureDish.DishAverageRating.toString()
        holder.view.tv_signature_dish_rating_bar.rating = signatureDish.DishAverageRating.toFloat()
        if (signatureDish.IsActiveNow == false){
            holder.view.btn_signature_buy.text = "Resqust"
        }


        holder.itemView.btn_signature_buy.setOnClickListener {

        }

        //setControlHomeFeed(homeFeed, position, holder)
    }


//    private fun setControlHomeFeed(homeFeed: HomeFeed, position: Int, holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
//       // val chefinfo: HomeFeed.Chef = homeFeed.chefs.filter { s -> homeFeed.dishes.get(position).ChefId == s.ChefId }.single()
//        val dishInfo: HomeFeed.Dish = homeFeed.dishes.get(position)
//
//
//
//        // Disable buy button and enable (+) and (-) button If the item is already added to the cart
//        if (m_listOfCartItem.any { x -> x.dishItem.dishId == dishInfo.DishId }) {
//            // val cartItem: CartItem = m_listOfCartItem.filter { s-> dishInfo.DishId==s.dishItem.dishId}.single()
//            val cartItem: CartItem = m_listOfCartItem.filter { s -> s.dishItem.dishId == dishInfo.DishId }.single()
//
//            if (cartItem.dishItem.quantity > 0) {
//                holder.itemView.btn_lay.visibility = View.VISIBLE
//                holder.itemView.btn_buy_home.visibility = View.GONE
//                holder.itemView.tv_qutity.text = cartItem.dishItem.quantity.toString()
//            }
//        } else {
//            holder.itemView.btn_lay.visibility = View.GONE
//            holder.itemView.btn_buy_home.visibility = View.VISIBLE
//        }
//
//
//        // Set ClickListener for buy Button
//        holder.itemView.btn_buy_home.setOnClickListener {
//
//            if(Utility.isDifferentChef(dishInfo.ChefId)) {
//                Utility.createDialog(
//                    context,
//                    "Different Chef",
//                    "You can’t select dishes from have two different chef. Please place two different orders"
//                )
//            } else {
//                val intent = Intent(holder.itemView.btn_buy_home.context, TimeSlotActivity::class.java)
//                intent.putExtra(Utility.DISH_ID_KEY, dishInfo.DishId as Int)
//                intent.putExtra(Utility.CHEF_ID_KEY, dishInfo.ChefId as Int)
//                (holder.itemView.btn_buy_home.context as HomeActivity).startActivityForResult(intent, REQUEST_CODE)
//            }
//        }
//
//        // Set ClickListener for + Button
//        holder.itemView.tv_plus.setOnClickListener {
//
//
//
//            var count = holder.itemView.tv_qutity.text.toString().toInt()
//
//            if (count < 99) {
//                if (count == 0) {
//                    Utility.AddItem(dishInfo)
//                } else if (count > 0) {
//                    ShoppingCart.updateItemQuantity(dishInfo.DishId, UpdateType.ADD)
//                }
//
//                count++
//                listener.updateCount()
//                holder.itemView.tv_qutity.text = count.toString()
//            }
//        }
//
//        // Set ClickListener for - Button
//        holder.itemView.tv_minus.setOnClickListener {
//            var count = holder.itemView.tv_qutity.text.toString().toInt()
//
//
//
//            if (count > 1) {
//                count--
//                ShoppingCart.updateItemQuantity(dishInfo.DishId, UpdateType.REMOVE)
//                listener.updateCount()
//                holder.itemView.tv_qutity.text = count.toString()
//
//
//
//            } else {
//                Utility.RemoveItem(dishInfo.DishId)
//                listener.updateCount()
//                holder.itemView.btn_lay.visibility = View.GONE
//                holder.itemView.btn_buy_home.visibility = View.VISIBLE
//
//                Utility.resetFlag()
//            }
//        }
//    }





}


class ChefProfileViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

}