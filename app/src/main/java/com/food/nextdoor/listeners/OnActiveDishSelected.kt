package com.food.nextdoor.listeners

import com.food.nextdoor.model.HomeFeed

interface OnActiveDishSelected {
    fun updateDishesByChef(dishes:List<HomeFeed.Dish>)
}