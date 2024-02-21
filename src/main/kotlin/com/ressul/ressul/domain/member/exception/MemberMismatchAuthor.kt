package com.ressul.ressul.domain.member.exception

import com.ressul.ressul.global.exception.CustomException
import com.ressul.ressul.global.exception.ErrorCode

class MemberMismatchAuthor(errorCode: ErrorCode) : CustomException(errorCode = errorCode)