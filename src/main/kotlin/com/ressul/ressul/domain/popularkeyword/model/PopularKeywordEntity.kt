package com.ressul.ressul.domain.popularkeyword.model

import com.ressul.ressul.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "popularkeyword")
class PopularKeywordEntity(
	val keyword: String,
) : BaseTimeEntity() {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null

	var count: Long = 0
}