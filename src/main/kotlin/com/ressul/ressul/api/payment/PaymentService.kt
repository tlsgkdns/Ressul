package com.ressul.ressul.api.payment

import com.ressul.ressul.api.payment.dto.PaymentResponse
import com.ressul.ressul.api.payment.dto.SignPaymentRequest

interface PaymentService {
    fun signPayment(signPaymentRequest: SignPaymentRequest):PaymentResponse

    fun getPaymentReceipt(paymentId:Long):PaymentResponse

}