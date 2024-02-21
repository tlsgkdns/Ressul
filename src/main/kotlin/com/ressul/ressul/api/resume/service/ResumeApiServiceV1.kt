package com.ressul.ressul.api.resume.service

import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.member.repository.MemberRepository
import com.ressul.ressul.domain.resume.dto.CreateResumeRequest
import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.dto.UpdateResumeRequest
import com.ressul.ressul.domain.resume.service.ResumeService
import com.ressul.ressul.infra.redis.resume.ResumePopularRedisService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ResumeApiServiceV1(
	private val resumeService: ResumeService,
	private val resumePopularRedisService: ResumePopularRedisService,
	private val memberRepository: MemberRepository
) {
	fun getResumeById(resumeId: Long) =
		resumeService.getResumeById(resumeId)
			.also { resumePopularRedisService.addView(it.id); }

	fun getPopularResume() =
		resumePopularRedisService.getPopularResume()
			.let { resumeService.getResumeListByIdList(it) }

	fun searchResume(dto: SearchResumeRequest, keyword: String, page: Int): List<Any> =
		resumeService.searchResumeList(dto, keyword, page)

	fun deleteResume(resumeId: Long, loginMember: LoginMember) =
		resumeService.deleteResume(resumeId, loginMember)

	fun updateResume(resumeId: Long, dto: UpdateResumeRequest, loginMember: LoginMember) =
		resumeService.updateResume(resumeId, dto, loginMember)

	fun createResume(dto: CreateResumeRequest, loginMember: LoginMember) =
		memberRepository.findByIdOrNull(loginMember.id)
			.let { resumeService.createResume(dto, it!!) }
}