package com.ressul.ressul.domain.events.exception

import com.ressul.ressul.global.exception.CustomException
import com.ressul.ressul.global.exception.ErrorCode

class EventIsClosedException: CustomException {
    constructor(errorCode: ErrorCode) : super(errorCode = errorCode)

    constructor(message: String, errorCode: ErrorCode) : super(message = message, errorCode = errorCode)

    constructor(payload: Any, errorCode: ErrorCode) : super(payload = payload, errorCode = errorCode)

    constructor(message: String, payload: Any, errorCode: ErrorCode) : super(
        message,
        errorCode,
        payload
    )
}