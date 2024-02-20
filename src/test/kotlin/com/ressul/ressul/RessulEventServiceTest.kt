package com.ressul.ressul

import com.ressul.ressul.common.type.OAuth2Provider
import com.ressul.ressul.domain.events.exception.EventIsClosedException
import com.ressul.ressul.domain.events.model.EventEntity
import com.ressul.ressul.domain.events.model.EventStatus
import com.ressul.ressul.domain.events.repository.EventRepository
import com.ressul.ressul.domain.events.repository.ParticipantRepository
import com.ressul.ressul.domain.events.repository.RedisLockRepository
import com.ressul.ressul.domain.events.service.EventServiceImpl
import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.member.repository.MemberRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import kotlin.random.Random

@SpringBootTest
class RessulEventServiceTest @Autowired constructor(
    private val redisLockRepository: RedisLockRepository,
    private val participantRepository: ParticipantRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository
) : DescribeSpec(
    {
        extension(SpringExtension)
        describe("참여자가 이벤트에 참가를 할 때,")
        {
            context("이벤트 인원이 넘치면")
            {
                it("EventIsClosedException이 발생한다.")
                {
                    memberRepository.save(MemberEntity(
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        OAuth2Provider.KAKAO,
                        Random.nextInt().toString()
                    ))
                    memberRepository.save(MemberEntity(
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        OAuth2Provider.KAKAO,
                        Random.nextInt().toString()
                    ))
                    memberRepository.save(MemberEntity(
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        OAuth2Provider.KAKAO,
                        Random.nextInt().toString()
                    ))
                    memberRepository.save(MemberEntity(
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        Random.nextInt().toString(),
                        OAuth2Provider.KAKAO,
                        Random.nextInt().toString()
                    ))
                    eventRepository.saveAndFlush(EventEntity("tutors", 3))
                    val eventService = EventServiceImpl(eventRepository = eventRepository, memberRepository = memberRepository,
                        redisLockRepository = redisLockRepository, participantRepository = participantRepository)
                    eventService.participateEvent(1L, LoginMember(1, "aaa"))
                    eventService.participateEvent(1L, LoginMember(2, "aaa"))
                    eventService.participateEvent(1L, LoginMember(3, "aaa"))
                    shouldThrow<EventIsClosedException> {
                        eventService.participateEvent(1L, LoginMember(4, "aaa"))
                    }
                    shouldThrow<EventIsClosedException> {
                        eventService.participateEvent(1L, LoginMember(5, "aaa"))
                    }
                }
            }
        }
    }
)