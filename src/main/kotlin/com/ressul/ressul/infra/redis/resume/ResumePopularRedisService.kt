package com.ressul.ressul.infra.redis.resume

import com.ressul.ressul.domain.resume.repository.JpaResumeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled

open class ResumePopularRedisService(
	private val viewsTemplate: RedisTemplate<String, Map<Long, Long>>,
	private val redisLongTemplate: RedisTemplate<String, Long>,
	private val resumeRepository: JpaResumeRepository
) {
	private val popularResumeKey = "PopularResume"
	private val recentlyViewsKey = "RecentlyResumeViews"
	private val updateViewsKey = "ResumeUpdatedViews"
	private val requestCountKey = "ResumeUpdateCount"
	private val requestMaxCount = 100
	private val popularResumeMaxNumber = 10L


	private fun shouldViewsUpdate() =
		redisLongTemplate.opsForValue().run {
			takeIf { get(requestCountKey)!! > requestMaxCount }
				?.let { set(requestCountKey, 0); true }
				?: false
		}

	private fun updateViews() {
		viewsTemplate.opsForHash<Long, Long>().run {
			val recentlyViews = entries(recentlyViewsKey)
			val updatedViews = entries(updateViewsKey)

			if (recentlyViews.isNotEmpty()) {
				val updateMap = HashMap<Long, Long>()
				recentlyViews.keys.forEach { key ->
					takeIf { updatedViews[key] != null }
						?.let { updateMap[key] = recentlyViews[key]?.minus(updatedViews[key]!!)!! }
						?: let { updateMap[key] = recentlyViews[key]!! }
				}
				entries(updateViewsKey).replaceAll { key, value -> updateMap[key] }
				resumeRepository.findAllById(updateMap.keys)
					.onEach { entity -> entity.views = updateMap[entity.id!!]!! }
					.let { resumeRepository.saveAllAndFlush(it) }
			}
		}
	}

	private fun getUpdatedViews() =
		viewsTemplate.opsForHash<Long, Long>().entries(updateViewsKey)

	private fun getRecentlyViews() =
		viewsTemplate.opsForHash<Long, Long>().entries(recentlyViewsKey)

	@Scheduled(fixedRate = 10 * 60 * 1000, initialDelay = 10 * 60 * 1000)
	private fun setPopularResume() {
		getRecentlyViews()
			.also { viewsTemplate.delete(recentlyViewsKey) }
			.also { redisLongTemplate.delete(popularResumeKey) }
			.also { recentlyViews ->
				recentlyViews.toList()
					.run {
						sortedBy { it.second }.takeIf { it.size > 10 }
							?.subList(0, popularResumeMaxNumber.toInt())
							?: this
					}
					.run { map { it.first } }
					.also { if (it.isNotEmpty()) redisLongTemplate.opsForList().rightPushAll(popularResumeKey, it) }
			}
			.run { updateViewsWith(this) }
	}

	private fun updateViewsWith(recentlyViews: Map<Long, Long>) {
		val updatedViews = getUpdatedViews()
		if (recentlyViews.isNotEmpty()) {
			recentlyViews.keys.map { it }
				.let { resumeRepository.findAllById(it) }
				.run {
					takeIf { updatedViews.isNotEmpty() }
						?.run { onEach { it.views += recentlyViews[it.id!!]?.minus(updatedViews[it.id!!]!!)!! } }
				}?.let { resumeRepository.saveAllAndFlush(it) }
			viewsTemplate.opsForHash<Long,Long>().entries(updateViewsKey)
		}
	}

	fun addView(resumeId: Long) =
		viewsTemplate.opsForHash<Long, Long>().run {
			get(recentlyViewsKey, resumeId.toString())
				?.let { put(recentlyViewsKey, resumeId, it + 1) }
				?: put(recentlyViewsKey, resumeId, 1)

			redisLongTemplate.opsForValue().increment(requestCountKey)

			if (shouldViewsUpdate())
				runBlocking(Dispatchers.IO) { async { updateViews() } }
		}

	fun getPopularResume() =
		redisLongTemplate.opsForList().range(popularResumeKey, 0, popularResumeMaxNumber)
			?: emptyList()
}