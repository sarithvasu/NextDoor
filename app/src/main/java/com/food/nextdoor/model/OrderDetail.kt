package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Order(
    var chefId : Int? = null,
    var sellerUserId : Int? = null,
    var sellerPayeeId : Int? = null,
    var buyerId : Int? = null,
    var orderDateTime : String? = null,
    var paymentMode : String? = null,
    var dishes : ArrayList<DishItem> = arrayListOf(),
    var transactionDetail : TransactionDetail? = null
)


@Parcelize
class DishItem (
    var dishId :  Int = -1,
    var quantity:  Int = -1,
    var deliveryStartTime: String? = null,
    var deliveryEndTime:String? = null,
    var packingTypeId: Int? = null,
    var deliveryTypeId: Int? = null
) : Parcelable


data class TransactionDetail (
    var transactionId : String? = null,
    var transactionRefId : String? = null,
    var responseCode : String? = null,
    var transactionStatus :String? = null,
    var transactionNote : String? = null,
    var transactionAmount : Double? = null,
    var currencyCode : String? = null,
    var approvarRefNoBeneficiary :String? = null,
    var transactionDateTime : String? = null
)



















