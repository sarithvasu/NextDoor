package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.food.nextdoor.R
import kotlinx.android.synthetic.main.order_confirmation.*
import system.ShoppingCart

class OrderConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.order_confirmation)

        tv_track_order_order_confirmation.setOnClickListener {
            val intent =
                Intent(tv_track_order_order_confirmation.context, MyOrderActivity::class.java)
            tv_track_order_order_confirmation.context.startActivity(intent)

            tv_return_home_order_confirmation.setOnClickListener {
                val intent =
                    Intent(tv_return_home_order_confirmation.context, HomeActivity::class.java)
                tv_return_home_order_confirmation.context.startActivity(intent)
            }
        }
        ShoppingCart.clearCart()

    }
}
