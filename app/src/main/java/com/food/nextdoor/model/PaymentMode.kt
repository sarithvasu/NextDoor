package com.food.nextdoor.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_mode")
class PaymentMode1 {

    @PrimaryKey
    var paymentModeId: Int = -1
    var paymentModeDescription: String? = ""
}
