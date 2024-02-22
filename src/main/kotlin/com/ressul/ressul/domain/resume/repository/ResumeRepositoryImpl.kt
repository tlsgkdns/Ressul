package com.ressul.ressul.domain.resume.repository

import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.model.ResumeEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ResumeRepositoryImpl(
	private val jpaResumeRepository: JpaResumeRepository,
	private val resumeQueryDslRepository: ResumeQueryDslRepository
) : IResumeRepository {
	override fun findByIdOrNull(id: Long) =
		jpaResumeRepository.findByIdOrNull(id)

	override fun save(entity: ResumeEntity) =
		jpaResumeRepository.save(entity)

	override fun delete(entity: ResumeEntity) =
		jpaResumeRepository.delete(entity)

	override fun searchBy(dto: SearchResumeRequest, keyword: String, page: Int, size: Int) =
		resumeQueryDslRepository.searchAndPaginationBy(dto, keyword, page, size)


	override fun findAllById(idList: List<Long>) =
		jpaResumeRepository.findAllById(idList).sortedByDescending { it.id }
}