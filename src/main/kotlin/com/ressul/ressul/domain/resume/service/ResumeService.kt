package com.ressul.ressul.domain.resume.service

import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.popularkeyword.service.PopularKeywordService
import com.ressul.ressul.domain.resume.dto.CreateResumeRequest
import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.dto.UpdateResumeRequest
import com.ressul.ressul.domain.resume.model.ResumeEntity
import com.ressul.ressul.domain.resume.repository.IResumeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ResumeService(
	private val resumeRepository: IResumeRepository,
	private val popularKeywordService: PopularKeywordService
) {

	private fun findById(id: Long) =
		resumeRepository.findByIdOrNull(id) ?: throw TODO("못찾음")

	fun createResume(dto: CreateResumeRequest, memberEntity: MemberEntity) =
		ResumeEntity.of(dto, memberEntity)
			.let { resumeRepository.save(it) }.toResponse()

	fun deleteResume(resumeId: Long) =
		findById(resumeId)
			.let { resumeRepository.delete(it) }

	@Transactional
	fun updateResume(resumeId: Long, dto: UpdateResumeRequest) =
		findById(resumeId).also { entity ->
			dto.introduction?.let { entity.introduction = it }
			dto.certification?.let { entity.certification = it }
			dto.education?.let { entity.education = it }
		}.toResponse()

	fun searchResumeList(dto: SearchResumeRequest, keyword: String): List<ResumeEntity> {
		popularKeywordService.incrementKeywordCount(keyword)
		return resumeRepository.searchBy(dto, keyword)
	}

	fun getResumeById(resumeId: Long) =
		findById(resumeId).toResponse()

	fun getResumeListByIdList(idList: List<Long>) =
		resumeRepository.findAllById(idList).map { it.toResponse() }
}