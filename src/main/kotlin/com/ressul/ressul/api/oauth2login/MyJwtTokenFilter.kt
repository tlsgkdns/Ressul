package com.ressul.ressul.api.oauth2login

import com.fasterxml.jackson.databind.ObjectMapper
import com.ressul.ressul.common.JwtHelper
import com.ressul.ressul.domain.member.dto.Member
import com.ressul.ressul.global.exception.ErrorCode
import com.ressul.ressul.global.exception.dto.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.util.PatternMatchUtils

class MyJwtTokenFilter(
    private val jwtHelper: JwtHelper
) : Filter {
    companion object {
        private val BEARER_PATTERN = Regex("^Bearer (.+?)$")
        private val whitelist = arrayOf(
            "/api/v1/oauth2/login/kakao", "/api/v1/oauth2/callback/kakao"
        )
    }

    private val logger = KotlinLogging.logger {}
    override fun init(filterConfig: FilterConfig?) {
        logger.info { "JwtTokenFilter Init" }
        super.init(filterConfig)
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse
        logger.info { "요청 URL: ${request.requestURL}" }
        logger.info { "response.status: ${response.status}" }

        if (isJwtCheckPath(request.requestURI)) {
            val jwt = request.getBearerToken()
            if (jwt != null) {
                jwtHelper.validateToken(jwt)
                    .onSuccess {
                        val userId = it.payload.subject.toLong()
                        val email = it.payload.get("email", String::class.java)

                        val member = Member(userId, email)

                        request.setAttribute("loginMember", member)
                    }
                    .onFailure {
                        val errorCode = ErrorCode.JWT_VERIFICATION_FAILED
                        response.status = errorCode.httpStatus.value()
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        val json = ObjectMapper().writeValueAsString(
                            ErrorResponse(
                                message = errorCode.message,
                                errorCode = errorCode.code
                            )
                        )
                        response.writer.write(json)
                    }
            }
        }

        if (response.status == 200) {
            filterChain.doFilter(request, response)
        }
    }

    private fun isJwtCheckPath(requestURI: String): Boolean {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI)
    }

    private fun HttpServletRequest.getBearerToken(): String? {
        val headerValue = this.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return BEARER_PATTERN.find(headerValue)?.groupValues?.get(1)
    }
}