package com.ressul.ressul.client.tosspayment.dto


data class TossPaymentResponse(
    val approvedAt:String,
    val status:String,
    val orderId:String,
    val paymentKey:String,
    val totalAmount:Long,
    ){

}

