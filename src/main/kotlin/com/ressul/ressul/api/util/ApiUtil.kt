package com.ressul.ressul.api.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> responseEntity(httpStatus: HttpStatus, func: () -> T): ResponseEntity<T> =
	func.invoke().let { ResponseEntity.status(httpStatus).body(it) }