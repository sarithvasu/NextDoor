package com.food.nextdoor.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packing_type")
class PackingType1() {
    @PrimaryKey
    var packingTypeId: Int = -1
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