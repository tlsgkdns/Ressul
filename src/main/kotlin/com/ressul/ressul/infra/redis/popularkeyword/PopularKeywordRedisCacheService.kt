package com.ressul.ressul.infra.redis.popularkeyword

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PopularKeywordRedisCacheService(
	private val popularKeywordRedisRepository: PopularKeywordRedisRepository,
) {
	fun cacheData(data: PopularKeywordRedisModel) =
		popularKeywordRedisRepository.save(data)

	fun getCachedData(keyword: String) =
		popularKeywordRedisRepository.findByIdOrNull(keyword)
			?.let { model -> Regex("\\d+").findAll(model.dataIdList).map { it.value.toLong() }.toList() }
}

