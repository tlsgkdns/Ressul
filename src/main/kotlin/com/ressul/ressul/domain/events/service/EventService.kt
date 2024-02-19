package com.ressul.ressul.domain.events.service

import com.ressul.ressul.domain.events.dto.EventCreateRequest
import com.ressul.ressul.domain.events.dto.EventResponse
import com.ressul.ressul.domain.events.dto.ParticipantsResponse
import com.ressul.ressul.domain.member.dto.LoginMember

interface EventService {
    fun getEvent(eventId: Long): EventResponse
    fun createEvent(eventCreateRequest: EventCreateRequest): EventResponse
    fun participateEvent(eventId: Long, loginMember: LoginMember): ParticipantsResponse
    fun deleteEvent(eventId: Long)
}