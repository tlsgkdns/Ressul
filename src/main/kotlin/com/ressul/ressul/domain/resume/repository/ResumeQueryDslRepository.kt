package com.ressul.ressul.domain.resume.repository

import com.querydsl.core.BooleanBuilder
import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.model.QResumeEntity
import com.ressul.ressul.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class ResumeQueryDslRepository : QueryDslSupport() {

	private val resume = QResumeEntity.resumeEntity


	fun searchAndPaginationBy(dto: SearchResumeRequest, keyword: String, page: Int, size: Int) =
		queryFactory
			.selectFrom(resume)
			.where(
				BooleanBuilder()
					.or(dto.title?.let { resume.title.contains(keyword) })
					.or(dto.education?.let { resume.education.contains(keyword) })
					.or(dto.introduction?.let { resume.introduction.contains(keyword) })
					.or(dto.certification?.let { resume.certification.contains(keyword) })
			)
			.orderBy(resume.id.desc())
			.limit(size.toLong())
			.offset(((page - 1) * size).toLong())
			.fetch()


}