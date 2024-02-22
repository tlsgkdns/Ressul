package com.ressul.ressul.domain.resume.repository

import com.querydsl.core.BooleanBuilder
import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.model.QResumeEntity
import com.ressul.ressul.domain.resume.model.ResumeEntity
import com.ressul.ressul.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class ResumeQueryDslRepository : QueryDslSupport() {

	private val resume = QResumeEntity.resumeEntity

	private fun getTableRow() =
		queryFactory.selectFrom(resume).orderBy(resume.id.desc()).limit(1).fetch()?.get(0)?.id


	fun searchAndPaginationBy(dto: SearchResumeRequest, keyword: String, page: Int, size: Int): List<ResumeEntity> =
		queryFactory
			.selectFrom(resume)
			.where(
				BooleanBuilder()
					.or(dto.title?.let { containsTitle(keyword) })
					.or(dto.education?.let { containsEducation(keyword) })
					.or(dto.introduction?.let { containsIntroduction(keyword) })
					.or(dto.certification?.let { containsCertification(keyword) })
					.and(getTableRow()?.let { getGt(it) })
			)
			.orderBy(resume.id.desc())
			.limit(size.toLong())
			.offset(((page - 1) * size).toLong())
			.fetch()

	private fun containsEducation(keyword: String) =
		resume.education.contains(keyword)

	private fun containsCertification(keyword: String) =
		resume.certification.contains(keyword)

	private fun containsTitle(keyword: String) =
		resume.title.contains(keyword)

	private fun containsIntroduction(keyword: String) =
		resume.introduction.contains(keyword)

	fun getGt(tableRow: Long) =
		takeIf { tableRow > 100000L }
			?.let { resume.id.gt(tableRow - 100000L) }
}