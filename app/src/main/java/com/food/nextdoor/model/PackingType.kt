package com.food.nextdoor.model

class PackingType() {
    var packingTypeId: Int? = null
    var packingDescription: String? = null

    // :this() pointing to empty constractor
    constructor (
        packingTypeId: Int,
        packingDescription: String

    ) : this() {
        this.packingTypeId = packingTypeId
        this.packingDescription = packingDescription
    }
}