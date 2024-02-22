package com.ressul.ressul.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: Long, val httpStatus: HttpStatus, val message: String) {

	PAYMENT_CLIENT_OBJECT_NOTFOUND(5001, HttpStatus.NOT_FOUND,"TOSS Payment 객체가 조회되지 않습니다."),
	PAYMENT_CLIENT_BAD_REQUEST(5002, HttpStatus.NOT_FOUND,"TOSS Payment에 잘 못된 요청을 하였습니다."),
	PAYMENT_CLIENT_RETRY(5003, HttpStatus.NOT_FOUND,"TOSS Payment에 잠시 후 다시 요청해 주세요."),

	PAYMENT_CLIENT_NOT_MATCH(5101, HttpStatus.NOT_FOUND,"요청 금액이 맞지 않습니다."),
	PAYMENT_CLIENT_NOT_FOUNT(5102, HttpStatus.BAD_REQUEST,"요청하신 결제번호가 존재하지 않습니다."),

	VALIDATION(10001, HttpStatus.BAD_REQUEST, "Validation을 통과하지 못했습니다."),
}