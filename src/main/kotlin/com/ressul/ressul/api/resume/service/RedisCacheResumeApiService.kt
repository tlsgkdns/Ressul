package com.ressul.ressul.api.resume.service

import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.member.repository.MemberRepository
import com.ressul.ressul.domain.popularkeyword.service.PopularKeywordService
import com.ressul.ressul.domain.resume.dto.CreateResumeRequest
import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.dto.UpdateResumeRequest
import com.ressul.ressul.domain.resume.service.ResumeService
import com.ressul.ressul.infra.redis.popularkeyword.PopularKeywordRedisCacheService
import com.ressul.ressul.infra.redis.popularkeyword.PopularKeywordRedisModel
import com.ressul.ressul.infra.redis.resume.ResumePopularRedisService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RedisCacheResumeApiService(
	private val resumeService: ResumeService,
	private val resumePopularRedisService: ResumePopularRedisService,
	private val memberRepository: MemberRepository,
	private val popularKeywordService: PopularKeywordService,
	private val popularKeywordRedisCacheService: PopularKeywordRedisCacheService
) {

	fun getResumeById(resumeId: Long) =
		resumeService.getResumeById(resumeId)
			.also { resumePopularRedisService.addView(it.id); }

	fun getPopularResume() =
		resumePopularRedisService.getPopularResume()
			.let { resumeService.getResumeListByIdList(it) }

	fun deleteResume(resumeId: Long, loginMember: LoginMember) =
		resumeService.deleteResume(resumeId, loginMember)

	fun updateResume(resumeId: Long, dto: UpdateResumeRequest, loginMember: LoginMember) =
		resumeService.updateResume(resumeId, dto, loginMember)

	fun createResume(dto: CreateResumeRequest, loginMember: LoginMember) =
		memberRepository.findByIdOrNull(loginMember.id)
			.let { resumeService.createResume(dto, it!!) }

	fun searchResume(dto: SearchResumeRequest, keyword: String, page: Int) = run {
		if (page == 1) {
			popularKeywordService.incrementKeywordCount(keyword)
			popularKeywordService.getPopularKeywordList()
				.takeIf { it.find { entity -> entity.keyword == keyword } != null }
				?.run { callServiceIfAbsentCachedData(keyword, page, dto) }
		} else resumeService.searchResumeList(dto, keyword, page)
	}

	private fun callServiceIfAbsentCachedData(keyword: String, page: Int, dto: SearchResumeRequest) =
		popularKeywordRedisCacheService.getCachedData(keyword)
			?.let { resumeService.getResumeListByIdList(it) }
			?: callServiceAndCacheData(keyword, page, dto)

	private fun callServiceAndCacheData(keyword: String, page: Int, dto: SearchResumeRequest) =
		resumeService.searchResumeList(dto, keyword, page).also { resList ->
			resList.map { it.id }
				.let {
					popularKeywordRedisCacheService.cacheData(PopularKeywordRedisModel(keyword, it.toString()))
				}
		}


}