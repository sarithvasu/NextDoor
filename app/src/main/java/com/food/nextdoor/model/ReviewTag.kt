package com.food.nextdoor.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_tag")
class ReviewTag1 {

    @PrimaryKey
    var reviewTagId: Int = -1
    var tagName: String? = ""
}



