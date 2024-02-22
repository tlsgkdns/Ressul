package com.ressul.ressul.infra.redis.resume.config

import com.ressul.ressul.domain.resume.repository.JpaResumeRepository
import com.ressul.ressul.infra.redis.resume.ResumePopularRedisService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class ResumeRedisConfig(
	private val redisConnectionFactory: RedisConnectionFactory
) {

	fun resumeViewsTemplate() =
		RedisTemplate<String, Map<Long, Long>>().apply {
			connectionFactory = redisConnectionFactory

			keySerializer = StringRedisSerializer()
			valueSerializer = Jackson2JsonRedisSerializer(Map::class.java)
			hashKeySerializer = GenericToStringSerializer(Long::class.java)
			hashValueSerializer = GenericToStringSerializer(Long::class.java)

			afterPropertiesSet()
		}

	fun resumeRequestCountTemplate() =
		RedisTemplate<String, Long>().apply {
			connectionFactory = redisConnectionFactory

			keySerializer = StringRedisSerializer()
			valueSerializer = GenericToStringSerializer(Long::class.java)

			afterPropertiesSet()
		}

	@Bean
	fun resumeRedisServiceConfiguration1(
		jpaResumeRepository: JpaResumeRepository,
		redisConnectionFactory: RedisConnectionFactory
	) =
		ResumePopularRedisService(
			resumeViewsTemplate(),
			resumeRequestCountTemplate(),
			jpaResumeRepository
		)
}