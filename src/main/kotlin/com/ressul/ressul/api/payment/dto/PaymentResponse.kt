package com.ressul.ressul.api.payment.dto

import com.ressul.ressul.domain.payment.model.PaymentEntity

data class PaymentResponse(
    val paymentId:Long,
    val price:Long,
    val createdAt:String,
    val comment:String,
    val status:String,
){
    companion object{
        fun toResponse(paymentEntity: PaymentEntity):PaymentResponse{
            return PaymentResponse(
                paymentId = paymentEntity.id!!,
                price = paymentEntity.price,
                createdAt = paymentEntity.createdAt.toString(),
                comment = paymentEntity.comment,
                status = paymentEntity.status.toString(),

            )
        }
    }
}
