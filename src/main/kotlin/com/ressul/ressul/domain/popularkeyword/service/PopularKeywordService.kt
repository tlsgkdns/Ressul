package com.ressul.ressul.domain.popularkeyword.service

import com.ressul.ressul.domain.popularkeyword.model.PopularKeywordEntity
import com.ressul.ressul.domain.popularkeyword.repository.PopularKeywordRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PopularKeywordService(
	private val popularKeywordRepository: PopularKeywordRepository
) {
	private fun findByKeyword(keyword: String) =
		popularKeywordRepository.findByKeyword(keyword) ?: addKeyword(keyword)

	private fun addKeyword(keyword: String) =
		popularKeywordRepository.save(PopularKeywordEntity(keyword))

	@Transactional
	fun incrementKeywordCount(keyword: String) =
		findByKeyword(keyword).let { it.count++ }

	fun getPopularKeywordList() =
		popularKeywordRepository.findTop10ByOrderByCountDesc()
}