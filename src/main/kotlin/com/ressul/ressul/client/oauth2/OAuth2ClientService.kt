package com.ressul.ressul.client.oauth2

import com.ressul.ressul.client.exception.OAuthException
import com.ressul.ressul.common.type.OAuth2Provider
import com.ressul.ressul.global.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class OAuth2ClientService(
    private val clients: List<OAuth2Client>
) {

    fun login(provider: OAuth2Provider, authorizationCode: String): OAuth2LoginUserInfo {
        val client = this.selectClient(provider)
        return client.getAccessToken(authorizationCode)
            .let { client.retrieveUserInfo(it) }
    }

    fun generateLoginPageUrl(provider: OAuth2Provider): String {
        val client = this.selectClient(provider)
        return client.generateLoginPageUrl()
    }

    private fun selectClient(provider: OAuth2Provider): OAuth2Client {
        return clients.find { it.supports(provider) }
            ?: throw OAuthException(ErrorCode.OAUTH_PROVIDER_NOT_SUPPORTED)
    }
}