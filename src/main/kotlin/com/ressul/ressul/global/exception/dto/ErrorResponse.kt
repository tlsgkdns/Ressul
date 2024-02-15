package com.ressul.ressul.global.exception.dto

data class ErrorResponse(
	val errorCode: Long,
	val message: String,
	val payload: Any? = null
)