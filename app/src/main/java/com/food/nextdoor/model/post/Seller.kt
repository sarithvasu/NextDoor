package com.food.nextdoor.model.post

data class Seller(
    val AboutChef: String,
    val AddressProofUrl: String,
    val ChefFollower: Int,
    val ChefRating: Int,
    val ChefReview: Int,
    val DateInsertion: String,
    val DateRevision: String,
    val IdProofUrl: String,
    val IsSpecializedInBoth: Boolean,
    val IsSpecializedInNonVeg: Boolean,
    val IsSpecializedInVeg: Boolean,
    val ProfileImageUrl: String,
    val UserId: Int
)