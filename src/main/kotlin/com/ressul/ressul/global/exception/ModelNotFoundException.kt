package com.ressul.ressul.global.exception

import com.ressul.ressul.global.exception.dto.ErrorResponse

class ModelNotFoundException : CustomException {


	constructor(errorCode: ErrorCode) : super(errorCode = errorCode)

	constructor(message: String, errorCode: ErrorCode) : super(message = message, errorCode = errorCode)

	constructor(payload: Any, errorCode: ErrorCode) : super(payload = payload, errorCode = errorCode)

	constructor(message: String, payload: Any, errorCode: ErrorCode) : super(
		message,
		errorCode,
		payload
	)

	override fun log() {
		super.logger.error("대상 Entity를 찾지 못하였습니다.")
		message?.let { super.logger.info(it) }
		super.logger.info("payload = $payload")
	}

}