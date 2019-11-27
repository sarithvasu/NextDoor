package com.food.nextdoor.model.post

data class CheckoutByCOD(
    val Order: List<CheckoutByCODOrder>,
    val Payment: CheckoutByUPIPayment?
)

data class CheckoutByCODOrder(
    val BuyerId: Int,
    val ChefId: Int,
    val DeliverEndTime: String?,
    val DeliverStartTime: String?,
    val DeliveryCharges: Int,
    val DeliveryDescription: String?,
    val Discount: Int,
    val DishId: Int,
    val ItemTotal: Int,
    val OrderTotal: Int,
    val PackingCharges: Int,
    val PackingDescription: String?,
    val PaymentMode: String,
    val Quantity: Int,
    val TotalDeliveryCharges: Int,
    val TotalPackingCharges: Int
)