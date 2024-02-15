package com.ressul.ressul.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: Long, val httpStatus: HttpStatus, val message: String) {

	VALIDATION(10001, HttpStatus.BAD_REQUEST, "Validation을 통과하지 못했습니다."),
}