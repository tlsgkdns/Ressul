package com.ressul.ressul.infra.redis.popularkeyword

import org.springframework.stereotype.Service

@Service
class PopularKeywordRedisCacheService(
	private val popularKeywordRedisRepository: PopularKeywordRedisRepository,
) {
	fun cacheData(data: PopularKeywordRedisModel) =
		popularKeywordRedisRepository.save(data)

	fun getCachedData(keyword: String, page: Int) =
		popularKeywordRedisRepository.findByKeywordAndPage(keyword, page)

}
