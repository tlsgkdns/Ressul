package com.ressul.ressul.domain.resume.dto

/**
 * NOTE: Type을 Any로 준 이유
 *  Boolean 값을 보내자니 필요없는 데이터도 json에 올라오는 것이 신경쓰여서
 */
data class SearchResumeRequest(
	val introduction: Any?,
	val title: Any? = true,
	val education: Any?,
	val certification: Any?
)