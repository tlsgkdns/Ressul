package com.ressul.ressul.infra.redis.popularkeyword

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "popularkeyword", timeToLive = 600)
data class PopularKeywordRedisModel(
	@Id
	val keyword: String,

	@Indexed
	val page: Int,

	val dataIdList: List<Long>
)