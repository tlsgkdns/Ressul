package com.ressul.ressul.client.exception

import com.ressul.ressul.global.exception.CustomException
import com.ressul.ressul.global.exception.ErrorCode

class TossPaymentException :CustomException{
    constructor(errorCode: ErrorCode) : super(errorCode = errorCode)


    constructor(message: String, errorCode: ErrorCode) : super(message = message, errorCode = errorCode)

    constructor(payload: Any, errorCode: ErrorCode) : super(payload = payload, errorCode = errorCode)

    constructor(message: String, payload: Any, errorCode: ErrorCode) : super(
        message,
        errorCode,
        payload
    )
}