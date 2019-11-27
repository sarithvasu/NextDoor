package com.food.nextdoor.model.post

data class NewAppartment(

    var Address: String,
    val ApartmentName: String,
    val Area: String,
    val PinCode: String,
    val RequesterFullName: String,
    val RequesterPhoneNumber: String
)