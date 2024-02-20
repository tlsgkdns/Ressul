package com.ressul.ressul.api.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateMemberProfileRequest(
    @field: NotBlank(message = "내용을 입력해주세요")
    val nickname: String,
    @field: NotNull(message = "내용을 입력해주세요")
    val mainResumeId: Long
)