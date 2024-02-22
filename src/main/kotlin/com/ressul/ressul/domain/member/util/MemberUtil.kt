package com.ressul.ressul.domain.member.util

import com.ressul.ressul.domain.member.exception.MemberMismatchAuthor
import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.member.repository.MemberRepository
import com.ressul.ressul.global.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class MemberUtil(
	private val memberRepository: MemberRepository
) {
	fun <T> checkPermission(memberId: Long, memberEntity: MemberEntity, func: () -> T): T =
		takeIf { memberId == memberEntity.id }
			?.let { func.invoke() }
			?: throw MemberMismatchAuthor(ErrorCode.MEMBER_MISMATCH_AUTHOR)
}