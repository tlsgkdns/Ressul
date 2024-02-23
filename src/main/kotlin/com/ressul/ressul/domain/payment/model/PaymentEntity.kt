package com.ressul.ressul.domain.payment.model

import com.ressul.ressul.global.entity.BaseEntity
import com.ressul.ressul.global.entity.BaseTimeEntity
import jakarta.persistence.*
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "payment")
class PaymentEntity(
    @Column(name = "member_id")
    val memberId:Long,

    @Column(name = "price")
    val price : Long,

    @Column(name = "toss_order_id")
    val tossOrderId : String,

    @Column(name = "toss_payment_key")
    val tossPaymentKey : String,

    @Column(name = "status"
    )
    var status : PaymentStatus,

    @Column(name = "comment")
    var comment : String

) :BaseEntity(){
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    val id :Long? = null;

    fun updatePayment(paymentStatus: PaymentStatus, comment: String){
        this.status = paymentStatus
        this.comment = comment
    }
}