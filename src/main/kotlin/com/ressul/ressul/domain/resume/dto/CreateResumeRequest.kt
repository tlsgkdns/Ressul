package com.ressul.ressul.domain.resume.dto

data class CreateResumeRequest(
	val introduction: String,

	val certification: String,

	val education: String,

	val title: String
)