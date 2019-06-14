package com.food.nextdoor.activity

import com.food.nextdoor.adapter.ImageSliderAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import java.util.*
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.View
import android.graphics.Point
import android.util.Log
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.HomeActivity
import system.Utility


class MainActivity : AppCompatActivity() {

    // Used in SetSliderTimer Method
    var currentPage = 0
    internal lateinit var timer: Timer
    val DELAY_MS: Long = 300//delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 2000 // time in milliseconds between successive task executions.

    // Major variables
    internal lateinit var adapter: PagerAdapter
    internal lateinit var sliderViewPager: ViewPager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SetImagSlideHeight()

        adapter = ImageSliderAdapter(this)
        sliderViewPager.adapter = adapter


        // After setting the adapter use the timer
        SetSliderTimer()
    }


    open fun buyerOnClick(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    // <editor-fold desc="Helper Method for Slider Timer and dynamic Height">

    // Dynamically setting the height of Image slider to 35 %
    private fun SetImagSlideHeight() {
        sliderViewPager = findViewById(R.id.SliderViewPager)
        var constraintLayout: ConstraintLayout


        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Log.e("Width", "" + width)
        Log.e("height", "" + height)

        val lp = sliderViewPager.getLayoutParams() as ConstraintLayout.LayoutParams
        lp.height = (height * 35) / 100
        sliderViewPager.setLayoutParams(lp);
    }

    // Setting the slider scroll speed to 2 seconds
    private fun SetSliderTimer() {
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === Utility.getImageSliderCount()) {
                currentPage = 0
            }
            sliderViewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread
        timer.schedule(object : TimerTask() { // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    //</editor-fold>


}
