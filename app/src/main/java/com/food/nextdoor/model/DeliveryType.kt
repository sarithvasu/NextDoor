package com.food.nextdoor.model

class DeliveryType() {
    var deliveryTypeId: Int? = null
    var deliveryDescription: String? = null

    // :this() pointing to empty constractor
    constructor (
        deliveryTypeId: Int,
        deliveryDescription: String

    ) : this() {
        this.deliveryTypeId = deliveryTypeId
        this.deliveryDescription = deliveryDescription
    }
}