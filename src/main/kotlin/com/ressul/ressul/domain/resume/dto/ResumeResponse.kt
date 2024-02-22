package com.ressul.ressul.domain.resume.dto

data class ResumeResponse (
	val id : Long,
	val title: String,
	val nickname : String,
	val certification : String,
	val introduction: String,
	val education : String,
	val views : Long,
)