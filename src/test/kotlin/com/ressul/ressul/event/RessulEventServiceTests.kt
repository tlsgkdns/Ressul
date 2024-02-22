package com.ressul.ressul.event

import com.ressul.ressul.common.type.OAuth2Provider
import com.ressul.ressul.domain.events.exception.AlreadyParticipatedEvent
import com.ressul.ressul.domain.events.exception.EventIsClosedException
import com.ressul.ressul.domain.events.model.EventEntity
import com.ressul.ressul.domain.events.repository.EventRepository
import com.ressul.ressul.domain.events.repository.ParticipantRepository
import com.ressul.ressul.domain.events.repository.RedisLockRepository
import com.ressul.ressul.domain.events.service.EventServiceImpl
import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.domain.member.repository.MemberRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.random.Random

@ActiveProfiles("test")
@SpringBootTest
class RessulEventServiceTests @Autowired constructor(
    private val redisLockRepository: RedisLockRepository,
    private val participantRepository: ParticipantRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository
) : DescribeSpec(
    {
        extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

        describe("여러 개의 Thread가")
        {
            context("하나의 Event에 거의 동시에 참여하면")
            {
                it("정확한 인원이 참여한다.")
                {
                    val threadCount = 100
                    addRandomMember(memberRepository, threadCount)
                    val eventService = EventServiceImpl(eventRepository = eventRepository, memberRepository = memberRepository,
                        redisLockRepository = redisLockRepository, participantRepository = participantRepository)
                    val eventId = eventRepository.save(EventEntity("This is tutor", 100)).id!!
                    val executorService = Executors.newFixedThreadPool(20)
                    val countDownLatch = CountDownLatch(threadCount)
                    repeat(threadCount)
                    {
                        executorService.submit{
                            try{
                                eventService.participateEvent(eventId, loginMember = LoginMember(it.toLong() + 1, "aaa"))
                            } finally {
                                countDownLatch.countDown()
                            }
                        }
                    }
                    countDownLatch.await()
                    eventRepository.findByIdOrNull(eventId)!!.participantCount shouldBe threadCount
                }
            }
        }

        describe("참여자가 이벤트에 참가를 할 때,")
        {
            context("이벤트 인원이 넘치면")
            {
                it("EventIsClosedException이 발생한다.")
                {
                    val participantsCount = 3;
                    addRandomMember(memberRepository, participantsCount)
                    val eventService = EventServiceImpl(eventRepository = eventRepository, memberRepository = memberRepository,
                        redisLockRepository = redisLockRepository, participantRepository = participantRepository)
                    val eventId = eventRepository.save(EventEntity("This is tutor", participantsCount)).id!!
                    repeat(participantsCount)
                    {
                        eventService.participateEvent(eventId, LoginMember(it.toLong() + 1, "aaa"))
                    }
                    shouldThrow<EventIsClosedException> {
                        eventService.participateEvent(eventId, LoginMember(participantsCount + 1L, "aaa"))
                    }
                    shouldThrow<EventIsClosedException> {
                        eventService.participateEvent(eventId, LoginMember(participantsCount + 2L, "aaa"))
                    }
                }
            }
        }
        describe("이벤트에 참여할 때")
        {
            context("이미 참여한 이벤트라면")
            {
                it("Already Participated Event Exception이 발생한다.")
                {
                    addRandomMember(memberRepository, 1)
                    val eventService = EventServiceImpl(eventRepository = eventRepository, memberRepository = memberRepository,
                        redisLockRepository = redisLockRepository, participantRepository = participantRepository)
                    val eventId = eventRepository.save(EventEntity("This is tutor", 100)).id!!
                    eventService.participateEvent(eventId, LoginMember(1L, "aaa"))
                    shouldThrow<AlreadyParticipatedEvent> {
                        eventService.participateEvent(eventId, LoginMember(1L, "aaa"))
                    }
                }
            }
        }

    }

)
fun addRandomMember(memberRepository: MemberRepository, memberCount: Int)
{
    repeat(memberCount)
    {
        memberRepository.save(MemberEntity(
            Random.nextInt().toString(),
            Random.nextInt().toString(),
            Random.nextInt().toString(),
            Random.nextInt().toString(),
            OAuth2Provider.KAKAO,
            Random.nextInt().toString()
        ))
    }
}