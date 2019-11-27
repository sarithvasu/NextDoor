package com.food.nextdoor.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
class PackingDispatchPaymentReviewFeed(val packingTypes: List<PackingType>, var deliveryTypes: List<DeliveryType>,
                val paymentModes: List<PaymentMode>, val reviewTags: List<ReviewTag>): Parcelable



@Parcelize
@Entity(tableName = "packing_type")
class PackingType(
    @PrimaryKey var packingTypeId: Int = -1,
    var packingDescription: String = ""
) : Parcelable


@Parcelize
@Entity(tableName ="delivery_type")
class DeliveryType(
    @PrimaryKey var deliveryTypeId: Int = -1,
    var deliveryDescription: String = ""
) : Parcelable


@Parcelize
@Entity(tableName = "payment_mode")
class PaymentMode(
    @PrimaryKey var paymentModeId: Int = -1,
    var paymentModeDescription: String = ""
) : Parcelable


@Parcelize
@Entity(tableName = "review_tag")
class ReviewTag(
    @PrimaryKey var reviewTagId: Int = -1,
    var tagName: String = ""
) : Parcelable{
    override fun toString(): String {
        return tagName
    }
}


