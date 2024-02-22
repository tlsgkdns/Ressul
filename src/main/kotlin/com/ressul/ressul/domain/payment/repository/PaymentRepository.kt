package com.ressul.ressul.domain.payment.repository

import com.ressul.ressul.domain.payment.model.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PaymentRepository : JpaRepository<PaymentEntity, Long> {
}