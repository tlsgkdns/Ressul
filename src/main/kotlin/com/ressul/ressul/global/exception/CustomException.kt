package com.ressul.ressul.global.exception

import com.ressul.ressul.global.exception.dto.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity

/**
 * NOTE : 이 클래스의 정의 이유와 사용 방법.
 *  개발자가 정의한 Exception을 하나의 ExceptionHandler로 처리하고 싶기 때문에 만들게 되었다.
 *  만약 특정 도메인에서만 발생하는 CustomException이 있다면 CustomException을 상속 받아 {DomainName}Exception으로 정의 한다.
 *  이후 그 Domain에서 발생하는 Exception들은 {DomainName}Exception 상속받아 사용하도록 한다.
 *
 *  @property payload Exception이 발생 되었을 때 그에 대한 상세한 내용을 담은 것을 받는 변수이다.
 *  DetailMessage가 될 수도 있고, 객체의 상태가 될 수도 있고, 에러가 난 propery에 대한 상세 내용이 될 수도 있다.
 */
abstract class CustomException(
	override val message: String? = null,
	val errorCode: ErrorCode,
	val payload: Any? = null
) : RuntimeException() {

	open val logger: Logger = LoggerFactory.getLogger(this::class.java)


	open fun log(){}

	/**
	 * Advice에서 호출하게 될 함수이다. 만약 Exception Handling을 다르게 가져가고 싶다면 override 하여 사용하도록 한다.
	 */
	open fun getErrorResponse() =
		ErrorResponse(
			message = message?.let { message } ?: errorCode.message,
			errorCode = errorCode.code,
			payload = payload
		)

}