package com.ressul.ressul.resume.repository

import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.resume.model.ResumeEntity
import com.ressul.ressul.global.entity.BaseTimeEntity
import com.ressul.ressul.util.flushIt
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import jakarta.transaction.Transactional
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull


interface TestJpaMemberRepository : CrudRepository<MemberEntity, Long>

interface TestJpaResumeRepository : JpaRepository<ResumeEntity, Long>


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = [BaseTimeEntity::class])
@EnableJpaAuditing
class ResumeRepositoryTest(
	private val resumeRepository: TestJpaResumeRepository,
	private val entityManager: TestEntityManager
) : DescribeSpec({

	extensions(SpringExtension)

	describe("Redis에서 조회수를 가져와 업데이트를 할 때") {
		val idList = mutableListOf<Long>(1, 3, 2) // keys로 뽑아온다.
		val views = listOf(200, 300, 400) // 원래는 가져온 idList에 맞춰 id에 맞춰 hash.get으로 가져온다.

		context("정렬을 시키고") {
			idList.sort()
			val foundList = resumeRepository.findAllById(idList)

			flushIt("해당하는 id 들의 조회수를 증가시킨다.", entityManager) {
				foundList.run {
					forEachIndexed { cnt, item ->
						idList[cnt] shouldBe item.id
					}

					views.forEachIndexed { index, item ->
						this[index].views += item
					}

					resumeRepository.save(this[0])
					this[0].views += 100
					resumeRepository.saveAll(this)


					this[0].views shouldBe resumeRepository.findByIdOrNull(1)!!.views
				}
			}
		}
	}

	describe("query문에 Where문 걸었을 때") {
		context("where 절에 들어갈 목록과 결과는") {
			val cretifi = RandomStringUtils.random(3, true, true).lowercase()
			val intro = RandomStringUtils.random(10, true, true).lowercase()

			flushIt("정상적으로 가져와야 한다.", entityManager) {
				val em = entityManager.entityManager
				val entity = em.createQuery(
					"select r from ResumeEntity r" +
							" where r.certification like CONCAT('%', :cretifi ,'%') " +
							" and r.introduction like CONCAT('%', :intro ,'%') " +
							"and r.id > 5390000",
					ResumeEntity::class.java
				)
					.setParameter("cretifi", cretifi)
					.setParameter("intro", intro)
					.resultList

				takeIf { entity.size > 0 }
					?.let {
						entity[0].certification.lowercase().contains(cretifi) shouldBe true
						entity[0].introduction.lowercase().contains(intro) shouldBe true
					}
			}
		}
	}

})


//	beforeSpec {
//		runBlocking {
//			val test1 = async(Dispatchers.IO) {
//				memberRepository.findByIdOrNull(1)!!
//					.let {
//						(1 .. 200000).map { cnt ->
//							ResumeEntity(
//								introduction = RandomStringUtils.random(1800, true, true),
//								certification = "${
//									RandomStringUtils.random(
//										2,
//										true,
//										true
//									)
//								}, ${RandomStringUtils.random(2, true, true)}, ${
//									RandomStringUtils.random(
//										8,
//										true,
//										true
//									)
//								}",
//								education = RandomStringUtils.random(12, true, true),
//								title = RandomStringUtils.random(11, true, true),
//								member = it,
//							)
//						}.let { resumeRepository.saveAll(it) }
//					}
//			}
//
//			val test2 = async {
//				memberRepository.findByIdOrNull(2)!!
//					.let {
//						(1 .. 200000).map { cnt ->
//							ResumeEntity(
//								introduction = RandomStringUtils.random(3400, true, true),
//								certification = "${
//									RandomStringUtils.random(
//										5,
//										true,
//										true
//									)
//								}, ${RandomStringUtils.random(3, true, true)}, ${
//									RandomStringUtils.random(
//										7,
//										true,
//										true
//									)
//								}",
//								education = RandomStringUtils.random(8, true, true),
//								title = RandomStringUtils.random(24, true, true),
//								member = it,
//							)
//						}.let { resumeRepository.saveAll(it) }
//					}
//			}
//
//			awaitAll(test1, test2)
//		}


//		MemberEntity(
//			name = "test2",
//			email = "test2",
//			mainResumeId = 1,
//			rawPassword = "test2"
//		)
//			.let {
//				it.password = "test2"
//				memberRepository.save(it)
//			}
//			.let {
//				(100000 .. 110000).map { cnt ->
//					ResumeEntity(
//						introduction = "test$cnt",
//						certification = "test$cnt",
//						education = "test$cnt",
//						member = it
//					)
//				}.let { resumeRepository.saveAll(it) }
//			}
//	}

//		val member = MemberEntity(
//			name = "test43211",
//			email = "test43211",
//			mainResumeId = 1,
//			rawPassword = "test43211"
//		)
//			.let {
//				it.password = "test3124"
//				memberRepository.save(it)
//			}
//
//		val resume = ResumeEntity(
//			title = "",
//			certification = "",
//			member = member,
//			introduction = "",
//			education = ""
//		)
//		resumeRepository.save(resume)