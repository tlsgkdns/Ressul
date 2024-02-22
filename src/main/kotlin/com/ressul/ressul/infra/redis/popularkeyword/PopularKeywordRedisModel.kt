package com.ressul.ressul.infra.redis.popularkeyword

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "popularkeyword", timeToLive = 60)
data class PopularKeywordRedisModel(
	@Id
	val keyword: String,
	val dataIdList: String
)