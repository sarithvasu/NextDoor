
package com.food.nextdoor.activity.buyer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.food.nextdoor.R

class CustomMealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_custom_meal)
    }
}
