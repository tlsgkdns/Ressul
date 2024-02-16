package com.ressul.ressul.api.oauth2login.config

import com.ressul.ressul.api.oauth2login.LoginMemberArgumentResolver
import com.ressul.ressul.api.oauth2login.MyJwtTokenFilter
import com.ressul.ressul.api.oauth2login.converter.OAuth2ProviderConverter
import com.ressul.ressul.common.JwtHelper
import jakarta.servlet.Filter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig(
    private val jwtHelper: JwtHelper
) : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(OAuth2ProviderConverter())
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver?>) {
        resolvers.add(LoginMemberArgumentResolver())
    }

    @Bean
    fun loginCheckFilter(): FilterRegistrationBean<*> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = MyJwtTokenFilter(jwtHelper)
        filterRegistrationBean.order = 2
        filterRegistrationBean.addUrlPatterns("/*")
        return filterRegistrationBean
    }
}