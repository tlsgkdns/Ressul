package com.ressul.ressul.resume.api.service

import com.ressul.ressul.api.resume.service.ResumeApiService
import com.ressul.ressul.domain.resume.dto.ResumeResponse
import com.ressul.ressul.domain.resume.service.ResumeService
import com.ressul.ressul.infra.redis.resume.ResumeRedisService
import com.ressul.ressul.infra.redis.resume.config.RedisConfiguration
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component




@SpringBootTest
@Import(value = [RedisConfiguration::class])
class ResumeApiServiceRedisTest(
	private val redisService: ResumeRedisService,
) : DescribeSpec({
	extensions(SpringExtension)

	describe("RedisService 테스트") {
		val resumeService = mockk<ResumeService>()
		val resumeApiService = ResumeApiService(resumeService, redisService)

		context("조회수 ") {
			val searched = (1 .. 20).map {
				ResumeResponse(
					id = (1 .. 2500).random().toLong(),
					education = "",
					certification = "",
					nickname = "",
					views = 0,
				)
			}
			every { resumeService.searchResumeList(any()) } returns searched

			it("") {

			}
		}

		context("Redis 조회수 Hash에 넣기") {
			it("")
		}
	}

})

/**
 * NOTE: Redis 인기 게시글 로직
 *  1. 게시글 조회가 들어올 때 Redis에 있는 최근 조회수 Hash key에 있는 resume id를 key로 가지고 있는 곳에 value를 상승 시킨다.
 *  2. 코루틴 또는 스케쥴러를 사용하여 10분에 한번씩 updating key에 true를 주고 최근 조회수 리스트에서 상위 n개를 인기 게시글 key에 등록 시킨다. 이후 updating을 false로 준다.
 */

/**
 * NOTE: Redis 조회수 카운팅 시킨 후 로직
 *  1. 게시글 조회가 들어올 때 Redis에 있는 최근 조회수 Hash key에 있는 resume id를 key로 가지고 있는 value가 특정 숫자가 되었을 때 조회수를 일괄적으로 업데이트 시킨다.
 */