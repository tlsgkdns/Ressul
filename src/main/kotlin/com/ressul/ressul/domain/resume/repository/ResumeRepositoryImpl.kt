package com.ressul.ressul.domain.resume.repository

import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.model.ResumeEntity
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ResumeRepositoryImpl(
	private val entityManager: EntityManager,
	private val jpaResumeRepository: JpaResumeRepository
) : IResumeRepository {
	override fun findByIdOrNull(id: Long) =
		jpaResumeRepository.findByIdOrNull(id)

	override fun save(entity: ResumeEntity) =
		jpaResumeRepository.save(entity)

	override fun delete(entity: ResumeEntity) =
		jpaResumeRepository.delete(entity)

	override fun searchBy(dto: SearchResumeRequest, keyword: String): List<ResumeEntity> {
		return 1 as List<ResumeEntity> // QueryDsl 적용 전 임시
	}

	override fun findAllById(idList: List<Long>) =
		jpaResumeRepository.findAllById(idList).toList()
}