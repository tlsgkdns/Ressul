package com.ressul.ressul.client.oauth2.kakao

import com.ressul.ressul.client.exception.OAuthException
import com.ressul.ressul.client.oauth2.OAuth2Client
import com.ressul.ressul.client.oauth2.OAuth2LoginUserInfo
import com.ressul.ressul.client.oauth2.kakao.dto.KakaoTokenResponse
import com.ressul.ressul.client.oauth2.kakao.dto.KakaoUserInfoResponse
import com.ressul.ressul.common.type.OAuth2Provider
import com.ressul.ressul.global.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class KakaoOAuth2Client(
    @Value("\${oauth2.kakao.client_id}") val clientId: String,
    @Value("\${oauth2.kakao.redirect_url}") val redirectUrl: String,
    private val restClient: RestClient
) : OAuth2Client {

    override fun generateLoginPageUrl(): String {
        return StringBuilder(KAKAO_AUTH_BASE_URL)
            .append("/oauth/authorize")
            .append("?response_type=").append("code")
            .append("&client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUrl)
            .toString()
    }

    override fun getAccessToken(authorizationCode: String): String {
        val requestData = mutableMapOf(
            "grant_type" to "authorization_code",
            "client_id" to clientId,
            "code" to authorizationCode
        )
        return restClient.post()
            .uri("$KAKAO_AUTH_BASE_URL/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .body<KakaoTokenResponse>()
            ?.accessToken
            ?: throw OAuthException("Kakao: AccessToken 조회 실패입니다!", ErrorCode.INVALID_ACCESSTOKEN)
    }

    override fun retrieveUserInfo(accessToken: String): OAuth2LoginUserInfo {
        return restClient.get()
            .uri("$KAKAO_API_BASE_URL/v2/user/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body<KakaoUserInfoResponse>()
            ?: throw OAuthException("Kakao: UserInfo 조회 실패입니다!", ErrorCode.USERINFO_NOT_FOUND)
    }

    override fun supports(provider: OAuth2Provider): Boolean {
        return provider == OAuth2Provider.KAKAO
    }

    companion object {
        private const val KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com"
        private const val KAKAO_API_BASE_URL = "https://kapi.kakao.com"
    }
}