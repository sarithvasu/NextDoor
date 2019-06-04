package com.food.nextdoor.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.food.nextdoor.R


class ImageSliderAdapter(private val context: Context) : PagerAdapter() {

    private var layoutInflater: LayoutInflater?= null
    private val arrImage =  arrayOf(R.drawable.kadai_panner, R.drawable.egg_biryani,R.drawable.roti_new, R.drawable.tandoori_new)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    override fun getCount(): Int {
        return  arrImage.size
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        var thiView = layoutInflater!!.inflate(R.layout.slider, null)
        val sliderImage = thiView.findViewById<View>(R.id.img_dish_Image_main) as ImageView
        sliderImage.setImageResource(arrImage[position])

        val viewPager = container as ViewPager
        viewPager.addView(thiView,0)
        return thiView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}