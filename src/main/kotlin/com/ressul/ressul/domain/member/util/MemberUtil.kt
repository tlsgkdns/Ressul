package com.ressul.ressul.domain.member.util

import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.member.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class MemberUtil(
	private val memberRepository: MemberRepository
) {
	fun <T> checkPermission(memberId: Long, memberEntity: MemberEntity, func: () -> T): T =
		takeIf { memberId == memberEntity.id }
			?.let { func.invoke() }
			?: TODO("권한 없음")
}