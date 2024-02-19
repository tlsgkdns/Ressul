package com.ressul.ressul.api.oauth2login.controller

import com.ressul.ressul.api.oauth2login.service.OAuth2LoginService
import com.ressul.ressul.client.oauth2.OAuth2ClientService
import com.ressul.ressul.common.type.OAuth2Provider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1")
@RestController
class OAuth2LoginController(
    private val oAuth2LoginService: OAuth2LoginService,
    private val oAuth2ClientService: OAuth2ClientService
) {

    @GetMapping("/oauth2/login/{provider}")
    fun redirectLoginPage(
        @PathVariable provider: OAuth2Provider,
        response: HttpServletResponse
    ) {
        val loginPageUrl = oAuth2ClientService.generateLoginPageUrl(provider)
        response.sendRedirect(loginPageUrl)
    }

    @GetMapping("/oauth2/callback/{provider}")
    fun callback(
        @PathVariable provider: OAuth2Provider,
        @RequestParam(name = "code")
        authorizationCode: String,
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok().header("AccessToken", oAuth2LoginService.login(provider, authorizationCode)).build()
    }
}