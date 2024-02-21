package com.ressul.ressul.domain.events.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisLockRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private fun makeKey(name: String, id: Long): String
    {
        return name + "_" + id.toString()
    }
    fun lock(name: String, id:Long) : Boolean
    {
        return redisTemplate.opsForValue()
            .setIfAbsent(makeKey(name, id), "lock", Duration.ofMillis(3_000)) ?: false
    }
    fun unlock(name: String, id: Long): Boolean
    {
        return redisTemplate.delete(makeKey(name, id))
    }
}