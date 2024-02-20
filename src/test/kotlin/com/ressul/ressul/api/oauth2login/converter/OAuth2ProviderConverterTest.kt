package com.ressul.ressul.api.oauth2login.converter

import com.ressul.ressul.common.type.OAuth2Provider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OAuth2ProviderConverterTest {

    private val oAuth2ProviderConverter = OAuth2ProviderConverter()

    @Test
    fun `소문자로 들어왔을 때 잘 컨버팅되는지 확인`() {
        //given
        val source = "kakao"

        //when
        val result = oAuth2ProviderConverter.convert(source)

        //then
        result shouldBe OAuth2Provider.KAKAO
    }

    @Test
    fun `대문자로 들어왔을 때 잘 컨버팅되는지 확인`() {
        //given
        val source = "KAKAO"

        //when
        val result = oAuth2ProviderConverter.convert(source)

        //then
        result shouldBe OAuth2Provider.KAKAO
    }

    @Test
    fun `잘못된 문자열이 들어오면 에러를 잘 내는지도 확인`() {
        //given
        val source = "wrong"

        //when & then
        shouldThrow<IllegalArgumentException> {
            oAuth2ProviderConverter.convert(source)
        }
    }
}