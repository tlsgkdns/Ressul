package com.ressul.ressul.infra.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    lateinit var host: String
    @Value("\${spring.data.redis.port}")
    var port: Int = 0

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory(
        RedisStandaloneConfiguration(host, port)
    )

    @Bean
    fun redisTemplate() =
        RedisTemplate<Any, Any>().apply{
            this.connectionFactory = redisConnectionFactory()
            this.keySerializer = StringRedisSerializer()
            this.valueSerializer = StringRedisSerializer()
        }
}