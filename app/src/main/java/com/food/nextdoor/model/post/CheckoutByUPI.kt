package com.food.nextdoor.model.post

data class CheckoutByUPI(
    val Order: List<CheckoutByCODOrder>,
    val Payment: CheckoutByUPIPayment
)


data class CheckoutByUPIPayment(
    var ApprovalReferenceNumberBeneficiary: String ="",
    var CurrencyCode: String="",
    var ResponseCode: String="",
    var TransactionAmount: Int=0,
    var TransactionId: String="",
    var TransactionNote: String="",
    var TransactionReferenceId: String="",
    var TransactionStatus: String=""
)