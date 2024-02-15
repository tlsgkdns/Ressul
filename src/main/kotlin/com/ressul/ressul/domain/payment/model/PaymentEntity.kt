package com.ressul.ressul.domain.payment.model

import com.ressul.ressul.global.entity.BaseEntity
import com.ressul.ressul.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "payment")
class PaymentEntity(
    @Column(name = "member_id")
    val memberId:Long,

    @Column(name = "price")
    val price : Long,

    @Column(name = "platform")
    val platform :PaymentPlatform,
) :BaseEntity(){
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    val id :Long? = null;

    @Column(name = "status")
    val status : PaymentStatus = PaymentStatus.ING
}