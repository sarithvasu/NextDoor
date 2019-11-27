package com.food.nextdoor.listeners

import com.food.nextdoor.model.HomeFeed

interface YouMayLikeSelectListener {
    fun onDishRowClick(dishInfo: HomeFeed.Dish)
}

