package com.ressul.ressul.infra.redis.popularkeyword

import org.springframework.data.repository.CrudRepository

interface PopularKeywordRedisRepository : CrudRepository<PopularKeywordRedisModel, String> {
}