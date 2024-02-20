package com.ressul.ressul.infra.redis.popularkeyword

class PopularKeywordRedisCacheService(
	private val popularKeywordRedisRepository: PopularKeywordRedisRepository,
) {
	fun cacheData(data: PopularKeywordRedisModel) =
		popularKeywordRedisRepository.save(data)

	fun getCachedData(keyword: String, page: Int) =
		popularKeywordRedisRepository.findByKeywordAndPage(keyword, page)

}
