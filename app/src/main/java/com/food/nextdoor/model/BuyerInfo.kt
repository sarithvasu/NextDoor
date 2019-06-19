package com.food.nextdoor.model

class BuyerInfo () {
    var userid: Int? = null
    var apartmentId: Int? = null
    var userName: String? = null
    var flatNumber: String? = null
    var mobileNumber: String? = null
    var email: String? = null
    var gender: String? = null
    var profileImageUrl: String? = null
    var isActive: Boolean = true
    var dateInsertion: String? = null
    var dateRevision: String? = null

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
        dateRevision: String
    ) : this() {
        this.userid = userid
        this.apartmentId = apartmentId
        this.userName = userName
        this.flatNumber = flatNumber
        this.mobileNumber = mobileNumber
        this.email = email
        this.gender = gender
        this.profileImageUrl = profileImageUrl
        this.isActive = isActive
        this.dateInsertion = dateInsertion
        this.dateRevision = dateRevision
    }
}