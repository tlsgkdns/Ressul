package com.ressul.ressul.api.oauth2login.service

import com.ressul.ressul.client.oauth2.OAuth2ClientService
import com.ressul.ressul.common.JwtHelper
import com.ressul.ressul.common.type.OAuth2Provider
import org.springframework.stereotype.Service

@Service
class OAuth2LoginService(
    private val oAuth2ClientService: OAuth2ClientService,
    private val memberService: MemberService,
    private val jwtHelper: JwtHelper
) {
    fun login(provider: OAuth2Provider, authorizationCode: String): String {
        return oAuth2ClientService.login(provider, authorizationCode)
            .let { memberService.registerIfAbsent(it) }
            .let { jwtHelper.generateAccessToken(it.id!!.toString(), it.email) }
    }
}