package com.food.nextdoor.model.post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish_views")
data class PostViewed(

    var ChefId: Int,
    var DishId: Int,
    var UserId: Int,
    var ViewedEndTime: String,
    var ViewedStartTime: String
){
    @PrimaryKey (autoGenerate = true)
    var ID:Int=0

}