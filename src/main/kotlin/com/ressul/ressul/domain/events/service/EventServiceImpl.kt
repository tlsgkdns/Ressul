package com.ressul.ressul.domain.events.service

import com.ressul.ressul.domain.events.dto.EventCreateRequest
import com.ressul.ressul.domain.events.dto.EventResponse
import com.ressul.ressul.domain.events.dto.ParticipantsResponse
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
class EventServiceImpl(
    private val eventRepository: EventRepository,
    private val redisLockRepository: RedisLockRepository,
    private val participantRepository: ParticipantRepository,
    private val memberRepository: MemberRepository
): EventService{
    fun getExistEvent(eventId: Long): EventEntity
    {
        return eventRepository.findByIdOrNull(eventId) ?: throw ModelNotFoundException(ErrorCode.EVENT_NOT_FOUND)
    }
    override fun getEvent(eventId: Long): EventResponse {
        return getExistEvent(eventId).let { EventResponse.from(it) }
    }

    @Transactional
    override fun createEvent(eventCreateRequest: EventCreateRequest): EventResponse {
        return eventRepository.save(eventCreateRequest.let { EventEntity(it.tutor, it.capacity) })
            .let { EventResponse.from(it) }
    }

    @Transactional
    override fun participateEvent(eventId: Long, loginMember: LoginMember): ParticipantsResponse {
        while (redisLockRepository.lock(eventId) == true)
        {
            Thread.sleep(100)
        }
        try {
            val event = getExistEvent(eventId)
            if(!event.addParticipant()) throw EventIsClosedException(ErrorCode.EVENT_IS_CLOSED)
            eventRepository.save(event)
            return participantRepository.save(Participant(member = memberRepository.findByIdOrNull(loginMember.id), event = event))
                .let { ParticipantsResponse.from(it) }
        } finally {
            redisLockRepository.unlock(eventId)
        }
    }

    @Transactional
    override fun deleteEvent(eventId: Long) {
        val event = getExistEvent(eventId)
        return eventRepository.deleteById(event.id!!)
    }
}