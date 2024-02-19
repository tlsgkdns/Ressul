package com.ressul.ressul.infra.redis.resume

import com.ressul.ressul.domain.resume.repository.JpaResumeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled

open class ResumeRedisService(
	private val redisTemplate: RedisTemplate<String, Int>,
	private val resumeRepository: JpaResumeRepository
) {
	private val POPULAR_RESUME_KEY = "PopularResume"
	private val RECENTLY_RESUME_VIEWS_KEY = "RecentlyResumeViews"
	private val UPDATED_VIEWS_KEY = "UpdatedViews"
	private val VIEW_COUNT = 100

	private val popularResumeNumber = 10L

	private fun isViewsUpdate() =
		redisTemplate.opsForValue().get(UPDATED_VIEWS_KEY)!! > VIEW_COUNT

	private fun updateViews() {
		redisTemplate.opsForHash<Long, Int>()
			.let {  }
	}

	private fun getUpdatedViews() =
		redisTemplate.opsForHash<Int?, Int?>().entries(UPDATED_VIEWS_KEY)


	private fun getRecentlyViews() =
		redisTemplate.opsForHash<Int?, Int?>().entries(RECENTLY_RESUME_VIEWS_KEY)

	@Scheduled(fixedRate = 10 * 60 * 1000)
	private fun setPopularResume() {

		getRecentlyViews()
			.also { redisTemplate.delete(RECENTLY_RESUME_VIEWS_KEY) }
			.also { recentlyViews ->
				recentlyViews.toList()
					.run { sortedBy { it.second } }
					.subList(0, popularResumeNumber.toInt())
					.run { map { it.first } }
					.also { redisTemplate.opsForList().rightPushAll(POPULAR_RESUME_KEY, it) }

			}
			.run { updateViewsWith(this) }
	}

	private fun updateViewsWith(recentlyViews: Map<Int, Int>) {
		val updatedViews = getUpdatedViews()
		recentlyViews.run { // TODO : 이미 처리가 끝난 조회수를 뺀 이후 최종적으로 업데이트 시킨다.
			keys.map { it.toLong() }
				.let { resumeRepository.findAllById(it) }
				.onEach { it.views = (((this[it.id!!.toInt()]?.minus(updatedViews[it.id!!.toInt()]!!))?.toLong()!!)) }
				.let { resumeRepository.saveAllAndFlush(it) }
		}
	}


	fun addView(resumeId: Long) =
		redisTemplate.opsForHash<Long?, Int?>().run {
			get(RECENTLY_RESUME_VIEWS_KEY, resumeId)
				?.let { put(RECENTLY_RESUME_VIEWS_KEY, resumeId, it + 1) }
				?: put(RECENTLY_RESUME_VIEWS_KEY, resumeId, 1)

			redisTemplate.opsForValue().increment(UPDATED_VIEWS_KEY)

			if (isViewsUpdate())
				runBlocking(Dispatchers.IO) { async { updateViews() } }
		}

	fun getPopularResume() =
		redisTemplate.opsForList().range(POPULAR_RESUME_KEY, 0, popularResumeNumber)?.toList()
			?: emptyList()
}