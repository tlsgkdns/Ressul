package com.ressul.ressul.api.oauth2login.service

import com.ressul.ressul.client.oauth2.OAuth2LoginUserInfo
import com.ressul.ressul.common.BaseRepositoryTest
import com.ressul.ressul.common.type.OAuth2Provider
import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.member.repository.MemberRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MemberServiceTest @Autowired constructor(
    private val memberRepository: MemberRepository
) : BaseRepositoryTest() {

    private val memberService = MemberService(memberRepository)

    @Test
    fun `registerIfAbsent - 사용자 정보 없을 경우 회원가입처리`() {
        // GIVEN
        memberRepository.deleteAll()
        val userInfo = OAuth2LoginUserInfo(
            id = "9999",
            email = "test@test.com",
            nickname = "test",
            profileImageUrl = "test url",
            thumbnailImageUrl = "test url",
            provider = OAuth2Provider.KAKAO
        )

        // WHEN
        val result = memberService.registerIfAbsent(userInfo)

        // THEN
        result.id shouldNotBe null
        result.id shouldBe 1L
        result.provider shouldBe OAuth2Provider.KAKAO
        result.providerId shouldBe "9999"
        result.email shouldBe "test@test.com"
        result.nickname shouldBe "test"
        result.profileImageUrl shouldBe "test url"
        result.thumbnailImageUrl shouldBe "test url"
        memberRepository.findAll().toList().let {
            it.size shouldBe 1
            it[0].id shouldBe 1L
            it[0].provider shouldBe OAuth2Provider.KAKAO
            it[0].providerId shouldBe "9999"
            it[0].email shouldBe "test@test.com"
            it[0].nickname shouldBe "test"
            it[0].profileImageUrl shouldBe "test url"
            it[0].thumbnailImageUrl shouldBe "test url"
        }
    }

    @Test
    fun `registerIfAbsent - 사용자 정보가 이미 존재하는 경우`() {
        // GIVEN
        val 기존사용자정보 = memberRepository.save(
            MemberEntity(
                email = "test@test.com",
                nickname = "ch4njun",
                profileImageUrl = "test url",
                thumbnailImageUrl = "test url",
                provider = OAuth2Provider.KAKAO,
                providerId = "9999",
            )
        )
        val userInfo = OAuth2LoginUserInfo(
            id = "9999",
            email = "test@test.com",
            nickname = "test",
            profileImageUrl = "test url",
            thumbnailImageUrl = "test url",
            provider = OAuth2Provider.KAKAO
        )

        // WHEN
        val result = memberService.registerIfAbsent(userInfo)

        // THEN
        result shouldBe 기존사용자정보
        memberRepository.findAll().toList().size shouldBe 1L
    }
}