package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ApartmentNameFeed (val apartments : List<Apartment>) : Parcelable

@Parcelize
data class Apartment (
    val apartmentId : Int,
    val apartmentName : String,
    val address : String,
    val pinCode : Int,
    val activeUsers : Int,
    val activeChefs : Int
): Parcelable{
    override fun toString(): String {
        return apartmentName
    }
}

