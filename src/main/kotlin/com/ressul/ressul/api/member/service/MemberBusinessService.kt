package com.ressul.ressul.api.member.service

import com.ressul.ressul.api.member.dto.ProfileResponse
import com.ressul.ressul.api.member.dto.UpdateMemberProfileRequest
import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.member.repository.MemberRepository
import com.ressul.ressul.domain.resume.respository.ResumeRepository
import com.ressul.ressul.global.exception.ErrorCode
import com.ressul.ressul.global.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberBusinessService(
    private val memberRepository: MemberRepository,
    private val resumeRepository: ResumeRepository
) {
    @Transactional
    fun getMemberProfile(loginMember: LoginMember): ProfileResponse {
        val findMember = retrieveMemberOrThrow(loginMember)

        val findResumeByIdOrNull =
            findMember.mainResumeId?.let { resumeRepository.findByIdOrNull(findMember.mainResumeId) }

        return ProfileResponse.from(findMember, findResumeByIdOrNull)
    }

    @Transactional
    fun updateMemberProfile(loginMember: LoginMember, request: UpdateMemberProfileRequest) {
        retrieveMemberOrThrow(loginMember).update(request.nickname, request.mainResumeId)
    }

    @Transactional
    fun deleteMember(loginMember: LoginMember) {
        val findMember = retrieveMemberOrThrow(loginMember)

        memberRepository.delete(findMember)
    }

    private fun retrieveMemberOrThrow(loginMember: LoginMember): MemberEntity =
        memberRepository.findByIdOrNull(loginMember.id) ?: throw ModelNotFoundException(
            "해당 Member Model을 조회할 수 없습니다.",
            ErrorCode.MODEL_NOT_FOUND
        )

}