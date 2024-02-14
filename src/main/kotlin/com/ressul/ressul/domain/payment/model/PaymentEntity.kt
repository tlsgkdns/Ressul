package com.ressul.ressul.domain.payment.model

import com.ressul.ressul.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "payment")
class PaymentEntity(

) :BaseTimeEntity(){

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    val id :Long? = null;
}