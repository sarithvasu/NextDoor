package com.food.nextdoor.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buyer_info")
class BuyerInfo ()  {
    @PrimaryKey
    var userId: Int = -1
    var userTypeId: Int = 0
    var apartmentId: Int = -1
    var fullName: String = ""
    var flatNumber: String? = null
    var mobileNumber: String? = null
    var email: String? = null
    var gender: String? = null
    var profileImageUrl: String? = null
    var isActive: Boolean = true
    var dateInsertion: String? = null
    var dateRevision: String? = null
    var apparmentName : String?=null
    var pinCode : String?=null

    // :this() pointing to empty constractor
    constructor (
        userid: Int,
        apartmentId: Int,
        userName: String,
        flatNumber: String,
        mobileNumber: String,
        email: String,
        gender: String,
        profileImageUrl: String,
        isActive: Boolean,
        dateInsertion: String,
        dateRevision: String,
        apparmentName :String,
        pinCode :String
    ) : this() {
        this.userId = userid
        this.apartmentId = apartmentId
        this.fullName = userName
        this.flatNumber = flatNumber
        this.mobileNumber = mobileNumber
        this.email = email
        this.gender = gender
        this.profileImageUrl = profileImageUrl
        this.isActive = isActive
        this.dateInsertion = dateInsertion
        this.dateRevision = dateRevision
        this.apparmentName = apparmentName
        this.pinCode = pinCode
    }




}