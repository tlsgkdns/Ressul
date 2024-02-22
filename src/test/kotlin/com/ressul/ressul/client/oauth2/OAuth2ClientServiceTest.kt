package com.ressul.ressul.client.oauth2

import com.ressul.ressul.client.oauth2.kakao.KakaoOAuth2Client
import com.ressul.ressul.common.type.OAuth2Provider
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class OAuth2ClientServiceTest {

    @Test
    fun `generateLoginPageUrl - 올바른 Client 선택후 정상동작 확인`() {
        // GIVEN
        val kakaoOAuth2Client = mockk<KakaoOAuth2Client>()
        val oAuth2ClientService = OAuth2ClientService(clients = listOf(kakaoOAuth2Client))

        every { kakaoOAuth2Client.supports(any()) } returns false
        every { kakaoOAuth2Client.supports(OAuth2Provider.KAKAO) } returns true

        every { kakaoOAuth2Client.generateLoginPageUrl() } returns "KAKAO REDIRECT URL"

        // WHEN
        val url = oAuth2ClientService.generateLoginPageUrl(OAuth2Provider.KAKAO)

        // THEN
        url shouldBe "KAKAO REDIRECT URL"
    }
}