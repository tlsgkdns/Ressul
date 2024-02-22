package com.ressul.ressul.api.payment.dto

import com.ressul.ressul.client.tosspayment.TossPaymentClient
import com.ressul.ressul.client.tosspayment.dto.TossConfirmRequest

data class SignPaymentRequest(
    val amount: Long,
    val orderId: String,
    val paymentKey: String,
    val memberId: Long,
) {
    fun toClientDto(): TossConfirmRequest {
        return TossConfirmRequest(
            orderId = this.orderId,
            paymentKey = this.paymentKey,
            amount = this.amount,
        )
    }
}
