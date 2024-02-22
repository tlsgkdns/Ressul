package com.ressul.ressul.domain.resume.dto

import jakarta.validation.constraints.NotNull

data class UpdateResumeRequest(
	val introduction: String?,
	val certification: String?,
	val education: String?,
	val title: String?,
	@field:NotNull(message = "page를 입력하여 주십시오.")
	val page: Long,
)
