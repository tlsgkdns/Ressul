package com.ressul.ressul.domain.popularkeyword.repository

import com.ressul.ressul.domain.popularkeyword.model.PopularKeywordEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PopularKeywordRepository : CrudRepository<PopularKeywordEntity, Long> {
	fun findByKeyword(keyword: String): PopularKeywordEntity?

	fun findTop10ByOrderByCountDesc(): List<PopularKeywordEntity>
}