package com.food.nextdoor.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="delivery_type")
class DeliveryType1() {
    //var deliveryTypeId: Int? = null
    @PrimaryKey
    var deliveryTypeId: Int = -1
   // var deliveryDescription: String? = null
   var deliveryDescription: String = ""

    // :this() pointing to empty constractor
    constructor (
        deliveryTypeId: Int,
        deliveryDescription: String

    ) : this() {
        this.deliveryTypeId = deliveryTypeId
        this.deliveryDescription = deliveryDescription
    }
}