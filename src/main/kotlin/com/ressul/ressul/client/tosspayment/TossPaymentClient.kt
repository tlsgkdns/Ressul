package com.ressul.ressul.client.tosspayment

import com.ressul.ressul.client.exception.TossPaymentException
import com.ressul.ressul.client.tosspayment.dto.TossConfirmRequest
import com.ressul.ressul.client.tosspayment.dto.TossPaymentErrorResponse
import com.ressul.ressul.client.tosspayment.dto.TossPaymentResponse
import com.ressul.ressul.global.exception.ErrorCode
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value

import org.springframework.http.MediaType
import org.springframework.stereotype.Component

import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse

@Component
class TossPaymentClient(
    @Value("\${payment.toss_payment.url}") val tossUrl: String,
    @Value("\${payment.toss_payment.toss_secret_key}") val tossSecretKey: String,
    private val restClient: RestClient
) {

    fun paymentConfirmRequest(tossConfirmRequest: TossConfirmRequest): TossPaymentResponse {
        return restClient.post()
            .uri("$tossUrl/confirm")
            .header("Authorization", "Basic ${toBase64(tossSecretKey)}")
            .contentType(MediaType.APPLICATION_JSON)
            .body(tossConfirmRequest)
            .exchange { _, response ->
                if (response.statusCode.is4xxClientError) {
                    throw TossPaymentException(toErrorMessage(response), ErrorCode.PAYMENT_CLIENT_BAD_REQUEST)
                }else if(response.statusCode.is5xxServerError){
                    throw TossPaymentException(toErrorMessage(response), ErrorCode.PAYMENT_CLIENT_RETRY)
                } else {
                    toResponse(response)
                }
            }
    }

    fun byOrderId(orderId: String): TossPaymentResponse {
        return restClient.get()
            .uri("${tossUrl}/orders/${orderId}")
            .header("Authorization", "Basic ${toBase64(tossSecretKey)}")
            .exchange { _, response ->
                if (response.statusCode.is4xxClientError) {
                    throw TossPaymentException(toErrorMessage(response), ErrorCode.PAYMENT_CLIENT_BAD_REQUEST)
                }else if(response.statusCode.is5xxServerError){
                    throw TossPaymentException(toErrorMessage(response), ErrorCode.PAYMENT_CLIENT_RETRY)
                } else {
                    toResponse(response)
                }
            }
    }

     fun byPaymentKey(paymentKey: String): TossPaymentResponse {
        return restClient.get()
            .uri("${tossUrl}/${paymentKey}")
            .header("Authorization", "Basic ${toBase64(tossSecretKey)}")
            .exchange { _, response ->
                if (response.statusCode.is4xxClientError) {
                    throw TossPaymentException(toErrorMessage(response), ErrorCode.PAYMENT_CLIENT_BAD_REQUEST)
                }else if(response.statusCode.is5xxServerError){
                    throw TossPaymentException(toErrorMessage(response), ErrorCode.PAYMENT_CLIENT_RETRY)
                } else {
                    toResponse(response)
                }
            }
    }

    private fun toBase64(value: String): String {
        return Base64.encodeBase64String(("$value:").toByteArray())
    }

    private fun toErrorMessage(response: ConvertibleClientHttpResponse):String{
        return response.bodyTo(TossPaymentErrorResponse::class.java)?.message ?: throw TossPaymentException(ErrorCode.PAYMENT_CLIENT_OBJECT_NOTFOUND)
    }

    private fun toResponse(response: ConvertibleClientHttpResponse):TossPaymentResponse{
        return response.bodyTo(TossPaymentResponse::class.java) ?: throw  TossPaymentException(ErrorCode.PAYMENT_CLIENT_OBJECT_NOTFOUND)
    }
}



