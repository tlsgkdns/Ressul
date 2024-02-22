package com.ressul.ressul.api.oauth2login

import com.ressul.ressul.domain.member.dto.LoginMember
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer


class LoginMemberArgumentResolver : HandlerMethodArgumentResolver {
    private val logger = KotlinLogging.logger {}
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        logger.info { "supportsParameter 실행" }

        val hasLoginAnnotation: Boolean = parameter.hasParameterAnnotation(Login::class.java)
        val hasMemberType: Boolean = LoginMember::class.java.isAssignableFrom(parameter.parameterType)

        return hasLoginAnnotation && hasMemberType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        logger.info { "resolveArgument 실행" }
        val request = webRequest.nativeRequest as HttpServletRequest
        return request.getAttribute("loginMember") as LoginMember
    }
}