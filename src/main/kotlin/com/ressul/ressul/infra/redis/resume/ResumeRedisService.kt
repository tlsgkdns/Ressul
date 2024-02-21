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
	private val UPDATED_VIEWS_KEY = "ResumeUpdatedViews"
	private val UPDATE_COUNT_KEY = "ResumeUpdateCount"
	private val VIEW_COUNT = 100

	private val popularResumeNumber = 10L


	private fun shouldViewsUpdate() =
		redisTemplate.opsForValue().run {
			takeIf { get(UPDATE_COUNT_KEY)!! > VIEW_COUNT }
				?.let {
					redisTemplate.opsForValue().set(UPDATE_COUNT_KEY, 0)
					true
				}
				?: false
		}


	private fun updateViews() {
		redisTemplate.opsForHash<Int, Int>().run {
			val recentlyViews = entries(RECENTLY_RESUME_VIEWS_KEY)
			val updatedViews = entries(UPDATED_VIEWS_KEY)

			recentlyViews.keys.forEach { key ->
				takeIf { updatedViews[key] != null }
					?.let { updatedViews[key] = recentlyViews[key]?.minus(updatedViews[key]!!) }
					?: let { updatedViews[key] = recentlyViews[key] }
			}
		}
	}

	private fun getUpdatedViews() =
		redisTemplate.opsForHash<Int, Int>().entries(UPDATED_VIEWS_KEY)

	private fun getRecentlyViews() =
		redisTemplate.opsForHash<Int, Int>().entries(RECENTLY_RESUME_VIEWS_KEY)

	@Scheduled(fixedRate = 10 * 60 * 1000)
	private fun setPopularResume() {
		getRecentlyViews()
			.also { redisTemplate.delete(RECENTLY_RESUME_VIEWS_KEY) }
			.also { redisTemplate.delete(POPULAR_RESUME_KEY) }
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
		recentlyViews.run { // NOTE : 이미 처리가 끝난 조회수를 뺀 이후 최종적으로 업데이트 시킨다.
			keys.map { it.toLong() }
				.let { resumeRepository.findAllById(it) }
				.onEach {
					it.views +=
						this[it.id!!.toInt()]!!.toLong() - updatedViews[it.id!!.toInt()]!!.toLong()
				}
				.let { resumeRepository.saveAllAndFlush(it) }
		}
	}


	fun addView(resumeId: Long) =
		redisTemplate.opsForHash<Long, Int>().run {
			get(RECENTLY_RESUME_VIEWS_KEY, resumeId)
				?.let { put(RECENTLY_RESUME_VIEWS_KEY, resumeId, it + 1) }
				?: put(RECENTLY_RESUME_VIEWS_KEY, resumeId, 1)

			redisTemplate.opsForValue().increment(UPDATE_COUNT_KEY)

			if (shouldViewsUpdate())
				runBlocking(Dispatchers.IO) { async { updateViews() } }
		}

	fun getPopularResume() =
		redisTemplate.opsForList().range(POPULAR_RESUME_KEY, 0, popularResumeNumber)?.toList()?.map { it.toLong() }
			?: emptyList()
}