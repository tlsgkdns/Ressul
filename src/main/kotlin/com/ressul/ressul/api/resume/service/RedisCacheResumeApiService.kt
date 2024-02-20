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
import com.ressul.ressul.infra.redis.resume.ResumeRedisService
import org.springframework.data.repository.findByIdOrNull

class RedisCacheResumeApiService(
	private val resumeService: ResumeService,
	private val resumeRedisService: ResumeRedisService,
	private val memberRepository: MemberRepository,
	private val popularKeywordService: PopularKeywordService,
	private val popularKeywordRedisCacheService: PopularKeywordRedisCacheService
) : ResumeApiServiceV2 {

	override fun getResumeById(resumeId: Long) =
		resumeService.getResumeById(resumeId)
			.also { resumeRedisService.addView(it.id); }

	override fun getPopularResume() =
		resumeRedisService.getPopularResume()
			.let { resumeService.getResumeListByIdList(it) }

	override fun searchResume(dto: SearchResumeRequest, keyword: String, page: Int) =
		popularKeywordService.getPopularKeywordList()
			.takeIf { it.find { entity -> entity.keyword == keyword } != null } // 인기 검색 키워드인가 확인
			?.run { // 인기 검색 키워드일 경우
				popularKeywordRedisCacheService.getCachedData(keyword, page) // 캐시된 데이터가 있나 없나 확인.
					?.also { popularKeywordService.incrementKeywordCount(keyword) } // 있다면 카운트 올리기
					?.let { resumeService.getResumeListByIdList(it.dataIdList) } // 캐싱되있는 ID들을 토대로 값 가져오기
					?: let { // 없을 경우
						popularKeywordService.incrementKeywordCount(keyword) // 카운트를 올리고
						resumeService.searchResumeList(dto, keyword, page).also { resList -> // 서비스를 호출 한 후
							resList.map { res -> res.id }.let {
								popularKeywordRedisCacheService.cacheData( // 캐싱을 시키고 반환한다. 호출 한 데이터를 반환 한다.
									PopularKeywordRedisModel(
										keyword,
										page,
										it
									)
								)
							}
						}
					}
			}
			?: let { // 인기 검색 키워드일 경우
				popularKeywordService.incrementKeywordCount(keyword) // 카운트를 올리고
				resumeService.searchResumeList(dto, keyword, page) // 서비스를 호출한다.
			}


	override fun deleteResume(resumeId: Long, loginMember: LoginMember) =
		resumeService.deleteResume(resumeId, loginMember)

	override fun updateResume(resumeId: Long, dto: UpdateResumeRequest, loginMember: LoginMember) =
		resumeService.updateResume(resumeId, dto, loginMember)

	override fun createResume(dto: CreateResumeRequest, loginMember: LoginMember) =
		memberRepository.findByIdOrNull(loginMember.id)
			.let { resumeService.createResume(dto, it!!) }
}