package com.ressul.ressul.domain.popularkeyword.model

import com.ressul.ressul.domain.popularkeyword.dto.PopularKeywordDTO
import com.ressul.ressul.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "popularkeyword")
class PopularKeywordEntity(
	@Column(unique = true, nullable = false)
	val keyword: String,
) : BaseTimeEntity() {
	fun toResponse() =
		PopularKeywordDTO(keyword, count)

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null

	var count: Long = 0
}