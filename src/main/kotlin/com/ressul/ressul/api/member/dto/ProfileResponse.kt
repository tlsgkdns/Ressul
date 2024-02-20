package com.ressul.ressul.api.member.dto

import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.resume.model.ResumeEntity

data class ProfileResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String,
    val thumbnailImageUrl: String,
    val mainResume: ResumeDto?,
) {
    companion object {
        fun from(memberEntity: MemberEntity, resumeEntity: ResumeEntity?): ProfileResponse = ProfileResponse(
            id = memberEntity.id!!,
            email = memberEntity.email,
            nickname = memberEntity.nickname,
            profileImageUrl = memberEntity.profileImageUrl,
            thumbnailImageUrl = memberEntity.thumbnailImageUrl,
            mainResume = resumeEntity?.let { ResumeDto.from(it) },
        )
    }
}