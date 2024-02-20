package com.ressul.ressul.infra.redis.resume.config

import com.ressul.ressul.domain.resume.repository.JpaResumeRepository
import com.ressul.ressul.infra.redis.resume.ResumeRedisService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
class RedisConfiguration(
	@Value("\${spring.data.redis.host}")
	private val redisHost: String,
	@Value("\${spring.data.redis.port}")
	private val redisPort: Int,
) {

	@Bean
	fun redisConnectionFactory() = LettuceConnectionFactory(redisHost, redisPort)

	fun redisTemplateConfiguration() =
		RedisTemplate<String, Int>().apply {
			connectionFactory = redisConnectionFactory()

			keySerializer = StringRedisSerializer()
			valueSerializer = GenericJackson2JsonRedisSerializer()
			hashKeySerializer = StringRedisSerializer()
			hashValueSerializer = GenericJackson2JsonRedisSerializer()
		}

	@Bean
	fun resumeRedisServiceConfiguration(jpaResumeRepository: JpaResumeRepository) =
		ResumeRedisService(redisTemplateConfiguration(), jpaResumeRepository)
}