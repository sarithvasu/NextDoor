package com.food.nextdoor.model.post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shared_dish")
data class SharedDish(
    var ChefId: Int,
    var DishId: Int,
    var SharedVia: String,
    var UserId: Int
){
   @PrimaryKey (autoGenerate = true)
    var Id:Int=0
}