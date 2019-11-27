package com.food.nextdoor.model.post

data class PostSeller(
    val chef: Chef,
    val documentProof: DocumentProof
)

data class Chef(
    val AboutChef: String,
    val SpecializedOption : Int,
    val UserId: Int
)

data class DocumentProof(
    val AddressProofUrl: String,
    val ApartmentId: Int,
    val FlatNumber: String,
    val FullName: String,
    val IdProofUrl: String
)