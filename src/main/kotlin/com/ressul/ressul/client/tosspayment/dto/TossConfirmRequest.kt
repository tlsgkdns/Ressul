package com.ressul.ressul.client.tosspayment.dto

data class TossConfirmRequest(
    val orderId: String,
    val paymentKey: String,
    val amount: Long,
    ){

    }