package com.ressul.ressul.global.exception

import com.ressul.ressul.global.exception.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(CustomException::class)
	fun customExceptionHandler(e: CustomException) =
		e.getErrorResponse()
			.let { ResponseEntity.status(e.errorCode.httpStatus).body(it) }

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun dtoValidateError(error: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
		val errorMap = mutableMapOf<String, String>()

		error.bindingResult.fieldErrors.forEach {
			errorMap[it.field] = it.defaultMessage ?: "정의되지 않은 에러"
		}

		val errorObject = ErrorResponse(ErrorCode.VALIDATION.code, ErrorCode.VALIDATION.message, payload = errorMap)

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObject)
	}
}