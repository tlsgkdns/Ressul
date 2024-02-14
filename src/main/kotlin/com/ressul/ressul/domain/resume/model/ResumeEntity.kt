package com.ressul.ressul.domain.resume.model

import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.global.entity.BaseEntity
import jakarta.persistence.*

@Table(name = "resume")
@Entity
class ResumeEntity(
	val introduction: String,

	val certification: String,

	val education: String,

	val views: Long,

	@ManyToOne(fetch = FetchType.LAZY)
	val member: MemberEntity
) : BaseEntity() {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null
}