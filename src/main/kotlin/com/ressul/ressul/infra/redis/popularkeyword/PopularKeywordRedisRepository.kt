package com.ressul.ressul.infra.redis.popularkeyword

import org.springframework.data.repository.CrudRepository

interface PopularKeywordRedisRepository : CrudRepository<PopularKeywordRedisModel, Long> {
	fun findByKeywordAndPage(keyword: String, page: Int): PopularKeywordRedisModel?
}