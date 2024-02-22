package com.ressul.ressul.domain.resume.model

import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.resume.dto.CreateResumeRequest
import com.ressul.ressul.domain.resume.dto.ResumeResponse
import com.ressul.ressul.global.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.SoftDelete

@Table(name = "resume")
@Entity
@SoftDelete(columnName = "is_deleted")
@DynamicUpdate
class ResumeEntity(
	var introduction: String,

	var certification: String,

	var education: String,

	var title: String,

	@ManyToOne(fetch = FetchType.LAZY)
	val member: MemberEntity

) : BaseTimeEntity() {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null

	var views: Long = 0

	companion object {
		fun of(dto: CreateResumeRequest, memberEntity: MemberEntity) =
			ResumeEntity(
				introduction = dto.introduction,
				certification = dto.certification,
				education = dto.education,
				title = dto.title,
				member = memberEntity,
			)
	}

	fun toResponse() =
		ResumeResponse(
			id = id!!,
			nickname = member.nickname,
			certification = certification,
			education = education,
			views = views,
			title = title,
			introduction = introduction
		)
}