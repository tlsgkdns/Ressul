package com.ressul.ressul.api.test

import com.ressul.ressul.domain.events.dto.ParticipantsResponse
import com.ressul.ressul.domain.events.exception.AlreadyParticipatedEvent
import com.ressul.ressul.domain.events.exception.EventIsClosedException
import com.ressul.ressul.domain.events.model.EventEntity
import com.ressul.ressul.domain.events.model.Participant
import com.ressul.ressul.domain.events.repository.EventRepository
import com.ressul.ressul.domain.events.repository.ParticipantRepository
import com.ressul.ressul.domain.events.repository.RedisLockRepository
import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.member.repository.MemberRepository
import com.ressul.ressul.global.exception.ErrorCode
import com.ressul.ressul.global.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TestService(
    private val eventRepository: EventRepository,
    private val redisLockRepository: RedisLockRepository,
    private val participantRepository: ParticipantRepository,
    private val memberRepository: MemberRepository
) {
    fun getExistEvent(eventId: Long): EventEntity {
        return eventRepository.findByIdOrNull(eventId) ?: throw ModelNotFoundException(ErrorCode.EVENT_NOT_FOUND)
    }

    @Transactional
    fun participateEvent(eventId: Long, loginMember: LoginMember): ParticipantsResponse {
        while (!redisLockRepository.lock("Event", eventId)) {
            Thread.sleep(100)
        }
        try {
            getExistEvent(eventId).also {
                if(participantRepository.existsParticipantByEventIdAndMemberId(eventId, loginMember.id))
                    throw AlreadyParticipatedEvent(ErrorCode.ALREADY_PARTICIPATED_EVENT)
                check(it.addParticipant()) { throw EventIsClosedException(ErrorCode.EVENT_IS_CLOSED) }
                eventRepository.save(it)
            }.let { eventEntity ->
                val dummyMember = memberRepository.findByIdOrNull(loginMember.id)
                return participantRepository.save(Participant(member = dummyMember, event = eventEntity))
                    .let { ParticipantsResponse.from(it) }
            }
        } finally {
            redisLockRepository.unlock("Event", eventId)
        }
    }
}