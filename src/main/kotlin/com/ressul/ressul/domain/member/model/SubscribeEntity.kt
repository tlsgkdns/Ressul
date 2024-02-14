package com.ressul.ressul.domain.member.model

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "subscribe")
@SQLDelete(sql = "UPDATE subscribe SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class SubscribeEntity(

) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null
}