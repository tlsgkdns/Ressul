package com.ressul.ressul.api.member.dto

import com.ressul.ressul.domain.resume.model.ResumeEntity

data class ResumeDto(
    val id: Long,
    val introduction: String,
    val certification: String,
    val education: String,
    val views: Long,
) {
    companion object {
        fun from(resumeEntity: ResumeEntity): ResumeDto = ResumeDto(
            id = resumeEntity.id!!,
            introduction = resumeEntity.introduction,
            certification = resumeEntity.certification,
            education = resumeEntity.education,
            views = resumeEntity.views,
        )
    }
}