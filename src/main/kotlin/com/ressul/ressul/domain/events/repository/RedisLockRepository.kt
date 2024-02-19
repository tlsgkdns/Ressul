package com.ressul.ressul.domain.events.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisLockRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun lock(key:Long) : Boolean?
    {
        return redisTemplate.opsForValue()
            .setIfAbsent(key.toString(), "lock", Duration.ofMillis(3_000))
    }
    fun unlock(key: Long): Boolean
    {
        return redisTemplate.delete(key.toString())
    }
}