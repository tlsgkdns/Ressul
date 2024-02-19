package com.ressul.ressul.domain.resume.service

import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.member.util.MemberUtil
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
	private val popularKeywordService: PopularKeywordService,
	private val memberUtil: MemberUtil
) {

	private fun findById(id: Long) =
		resumeRepository.findByIdOrNull(id) ?: throw TODO("못찾음")

	fun createResume(dto: CreateResumeRequest, memberEntity: MemberEntity) =
		ResumeEntity.of(dto, memberEntity)
			.let { resumeRepository.save(it) }.toResponse()

	@Transactional
	fun deleteResume(resumeId: Long, loginMember: LoginMember) =
		findById(resumeId).let { entity ->
			memberUtil.checkPermission(loginMember.id, entity.member) {
				resumeRepository.delete(entity)
			}
		}

	@Transactional
	fun updateResume(resumeId: Long, dto: UpdateResumeRequest, loginMember: LoginMember) =
		findById(resumeId).let { entity ->
			memberUtil.checkPermission(loginMember.id, entity.member) {
				dto.introduction?.let { entity.introduction = it }
				dto.certification?.let { entity.certification = it }
				dto.education?.let { entity.education = it }
				entity.toResponse()
			}
		}


	@Transactional
	fun searchResumeList(dto: SearchResumeRequest, keyword: String): List<ResumeEntity> {
		popularKeywordService.incrementKeywordCount(keyword)
		return resumeRepository.searchBy(dto, keyword)
	}

	fun getResumeById(resumeId: Long) =
		findById(resumeId).toResponse()

	fun getResumeListByIdList(idList: List<Long>) =
		resumeRepository.findAllById(idList).map { it.toResponse() }
}