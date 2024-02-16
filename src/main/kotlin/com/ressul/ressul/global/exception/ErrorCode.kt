package com.ressul.ressul.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: Long, val httpStatus: HttpStatus, val message: String) {
	OAUTH_PROVIDER_NOT_SUPPORTED(1001, HttpStatus.BAD_REQUEST, "지원하지 않는 OAuth Provider 입니다!"),
	INVALID_ACCESSTOKEN(1002, HttpStatus.BAD_REQUEST, "AccessToken 조회 실패입니다!"),
	USERINFO_NOT_FOUND(1003, HttpStatus.BAD_REQUEST, "UserInfo 조회 실패입니다!"),

	VALIDATION(10001, HttpStatus.BAD_REQUEST, "Validation을 통과하지 못했습니다."),
}