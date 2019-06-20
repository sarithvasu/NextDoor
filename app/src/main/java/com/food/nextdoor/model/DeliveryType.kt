package com.food.nextdoor.model

class DeliveryType() {
    //var deliveryTypeId: Int? = null
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