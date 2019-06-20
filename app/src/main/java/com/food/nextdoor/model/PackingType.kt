package com.food.nextdoor.model

class PackingType() {
    //var packingTypeId: Int? = null
    var packingTypeId: Int = -1
    //var packingDescription: String? = null
    var packingDescription: String = ""

    // :this() pointing to empty constractor
    constructor (
        packingTypeId: Int,
        packingDescription: String

    ) : this() {
        this.packingTypeId = packingTypeId
        this.packingDescription = packingDescription
    }
}