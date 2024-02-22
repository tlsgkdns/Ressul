package com.ressul.ressul.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: Long, val httpStatus: HttpStatus, val message: String) {
	OAUTH_PROVIDER_NOT_SUPPORTED(1001, HttpStatus.BAD_REQUEST, "지원하지 않는 OAuth Provider 입니다!"),
	INVALID_ACCESSTOKEN(1002, HttpStatus.BAD_REQUEST, "AccessToken 조회 실패입니다!"),
	USERINFO_NOT_FOUND(1003, HttpStatus.BAD_REQUEST, "UserInfo 조회 실패입니다!"),
	JWT_VERIFICATION_FAILED(1004, HttpStatus.BAD_REQUEST, "JWT verification failed"),
	MEMBER_MISMATCH_AUTHOR(1005, HttpStatus.FORBIDDEN, "작성자가 아닙니다."),

	EVENT_NOT_FOUND(4001, HttpStatus.NOT_FOUND, "해당 Event가 없습니다."),
	EVENT_IS_CLOSED(4002, HttpStatus.BAD_REQUEST, "해당 Event는 종료되었습니다."),
	ALREADY_PARTICIPATED_EVENT(4003, HttpStatus.BAD_REQUEST, "이미 참여하신 Event입니다."),

	PAYMENT_CLIENT_OBJECT_NOTFOUND(5001, HttpStatus.NOT_FOUND,"TOSS Payment 객체가 조회되지 않습니다."),
	PAYMENT_CLIENT_BAD_REQUEST(5002, HttpStatus.NOT_FOUND,"TOSS Payment에 잘 못된 요청을 하였습니다."),
	PAYMENT_CLIENT_RETRY(5003, HttpStatus.NOT_FOUND,"TOSS Payment에 잠시 후 다시 요청해 주세요."),

	PAYMENT_CLIENT_NOT_MATCH(5101, HttpStatus.NOT_FOUND,"요청 금액이 맞지 않습니다."),
	PAYMENT_CLIENT_NOT_FOUNT(5102, HttpStatus.BAD_REQUEST,"요청하신 결제번호가 존재하지 않습니다."),

	VALIDATION(10001, HttpStatus.BAD_REQUEST, "Validation을 통과하지 못했습니다."),
	MODEL_NOT_FOUND(10002, HttpStatus.BAD_REQUEST, "해당 Model을 찾지 못했습니다."),
}