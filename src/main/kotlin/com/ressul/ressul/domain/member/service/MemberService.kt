package com.ressul.ressul.domain.member.service

import com.ressul.ressul.client.oauth2.OAuth2LoginUserInfo
import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun registerIfAbsent(userInfo: OAuth2LoginUserInfo): MemberEntity {
        return if (!memberRepository.existsByProviderAndProviderId(userInfo.provider, userInfo.id)) {
            val member = MemberEntity(
                email = userInfo.email,
                provider = userInfo.provider,
                providerId = userInfo.id,
                nickname = userInfo.nickname,
                profileImageUrl = userInfo.profileImageUrl,
                thumbnailImageUrl = userInfo.thumbnailImageUrl,
            )
            memberRepository.save(member)
        } else {
            memberRepository.findByProviderAndProviderId(userInfo.provider, userInfo.id)
        }
    }
}