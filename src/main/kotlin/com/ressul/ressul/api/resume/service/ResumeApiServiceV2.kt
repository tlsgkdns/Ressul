package com.ressul.ressul.api.resume.service

import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.resume.dto.CreateResumeRequest
import com.ressul.ressul.domain.resume.dto.ResumeResponse
import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.dto.UpdateResumeRequest
import com.ressul.ressul.domain.resume.model.ResumeEntity
import org.springframework.stereotype.Service

@Service
interface ResumeApiServiceV2 {
	fun getResumeById(resumeId: Long): ResumeResponse
	fun getPopularResume(): List<ResumeResponse>
	fun searchResume(dto: SearchResumeRequest, keyword: String, page: Int): List<ResumeResponse>
	fun createResume(dto: CreateResumeRequest, loginMember: LoginMember): ResumeResponse
	fun updateResume(resumeId: Long, dto: UpdateResumeRequest, loginMember: LoginMember): ResumeResponse
	fun deleteResume(resumeId: Long, loginMember: LoginMember)
}