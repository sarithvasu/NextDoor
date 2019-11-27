package com.food.nextdoor.model.post

data class Checkout(
    val Order: OrderPost,
    val Payment: Payment
)

data class OrderPost(
    val BuyerId: Int,
    val ChefId: Int,
    val DateInsertion: String,
    val DateRevision: String,
    val DeliverEndTime: String,
    val DeliverStartTime: String,
    val DeliveryCharges: Int,
    val DeliveryDescription: String,
    val Discount: Int,
    val DishId: Int,
    val ItemTotal: Int,
    val OrderDate: String,
    val OrderTotal: Int,
    val PackingAndDeliveryCharges: Int,
    val PackingCharges: Int,
    val PackingDescription: String,
    val PaymentId: Int,
    val PaymentMode: String,
    val Quantity: Int
)

data class Payment(
    val ApprovalReferenceNumberBeneficiary: String,
    val CurrencyCode: String,
    val DateInsertion: String,
    val DateRevision: String,
    val ResponseCode: String,
    val TransactionAmount: Int,
    val TransactionDate: String,
    val TransactionId: String,
    val TransactionNote: String,
    val TransactionReferenceId: String,
    val TransactionStatus: String
)