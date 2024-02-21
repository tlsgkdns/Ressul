package com.ressul.ressul.resume.api.service

import com.ressul.ressul.domain.resume.dto.ResumeResponse
import com.ressul.ressul.domain.resume.service.ResumeService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@ExtendWith(MockKExtension::class)
class ResumeApiServiceRedisTest(
) : DescribeSpec({
	extensions(SpringExtension)

	describe("RedisService 테스트") {
		val resumeService = mockk<ResumeService>()
//		val resumeApiServiceV1 = ResumeApiServiceV1(resumeService, redisService)

		context("조회수 ") {
			val searched = (1 .. 20).map {
				ResumeResponse(
					id = (1 .. 2500).random().toLong(),
					education = "",
					certification = "",
					nickname = "",
					views = 0,
					title = ""
				)
			}

			it("") {

			}
		}

		context("Redis 조회수 Hash에 넣기") {
			it("")
		}
	}

})