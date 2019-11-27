package com.food.nextdoor.model.post

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class PostOrder(var order: Order = Order(), var dishes: List<DishItem> = emptyList(), var transactionDetail: TransactionDetail = TransactionDetail()) {
}

data class Order(
    var chefId : Int? = null, // Required because currently we are supporting order only for one chef
    var sellerUserId : Int? = null,
    var sellerPayeeId : Int? = null,
    var buyerId : Int? = null,
    var orderDateTime : String? = null,
    var paymentModeId : Int? = null,
    var dishes : ArrayList<DishItem> = arrayListOf(),
    var transactionDetail : TransactionDetail? = null
)

@Parcelize
class DishItem (
    var dishId :  Int = -1,
    var chefId :  Int = -1, // Keeping chef id for future, so that we can support order from different chef.
    var quantity:  Int = -1,
    var unitPrice: Int = -1,
    var deliveryStartTime: String? = null,
    var deliveryEndTime:String? = null,
    var packingTypeId: Int = -1,
    var deliveryTypeId: Int = -1
) : Parcelable

data class TransactionDetail (
    var transactionId : String? = null,
    var transactionRefId : String? = null,
    var responseCode : String? = null,
    var transactionStatus :String? = null,
    var transactionNote : String? = null,
    var transactionAmount : Double? = null,
    var currencyCode : String? = null,
    var approvalRefNoBeneficiary :String? = null,
    var transactionDateTime : String? = null
)
