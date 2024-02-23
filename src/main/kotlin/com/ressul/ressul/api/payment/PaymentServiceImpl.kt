package com.ressul.ressul.api.payment

import com.ressul.ressul.api.payment.dto.PaymentResponse
import com.ressul.ressul.api.payment.dto.SignPaymentRequest
import com.ressul.ressul.api.payment.exception.PaymentException
import com.ressul.ressul.client.tosspayment.TossPaymentClient
import com.ressul.ressul.domain.payment.model.PaymentEntity
import com.ressul.ressul.domain.payment.model.PaymentStatus
import com.ressul.ressul.domain.payment.repository.PaymentRepository
import com.ressul.ressul.global.exception.ErrorCode
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository,
    private val tossPaymentClient: TossPaymentClient,
) : PaymentService {
    @Transactional
    override fun signPayment(signPaymentRequest: SignPaymentRequest): PaymentResponse {
        return tossPaymentClient.paymentConfirmRequest(signPaymentRequest.toClientDto()).also {
            if(it.totalAmount != signPaymentRequest.amount) throw PaymentException(ErrorCode.PAYMENT_CLIENT_NOT_MATCH)
            if(it.status != "DONE") throw PaymentException(ErrorCode.PAYMENT_CLIENT_RETRY)
        }.let{
            paymentRepository.save(PaymentEntity(
                memberId = signPaymentRequest.memberId,
                price = signPaymentRequest.amount,
                tossOrderId = it.orderId,
                tossPaymentKey = it.paymentKey,
                status = PaymentStatus.OK,
                comment = "결제가 완료 되었습니다."
            ))
        }.let {
            PaymentResponse.toResponse(it)
        }
    }

    override fun getPaymentReceipt(paymentId: Long): PaymentResponse {
        return paymentRepository.findByIdOrNull(paymentId)?.let {
            PaymentResponse.toResponse(it)
        } ?: throw PaymentException(ErrorCode.PAYMENT_CLIENT_NOT_FOUNT)
    }
}