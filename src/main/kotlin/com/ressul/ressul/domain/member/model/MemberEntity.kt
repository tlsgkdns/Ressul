package com.ressul.ressul.domain.member.model

import com.ressul.ressul.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "members")
@SQLDelete(sql = "UPDATE members SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class MemberEntity(
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Transient
    var rawPassword: String,

    @Column(name = "name", nullable = false)
    var name: String,

) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "password", nullable = false)
    var password: String? = null

}