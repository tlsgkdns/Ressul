package com.ressul.ressul.api.payment

import com.ressul.ressul.api.payment.dto.PaymentResponse
import com.ressul.ressul.api.payment.dto.SignPaymentRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PostMapping()
    fun signPayment(@RequestBody signPaymentRequest: SignPaymentRequest):ResponseEntity<PaymentResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.signPayment(signPaymentRequest))

    }
    @GetMapping("/{paymentId}")
    fun getPaymentReceiptByOrderId(@PathVariable paymentId:Long):ResponseEntity<PaymentResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentReceipt(paymentId))
    }


}