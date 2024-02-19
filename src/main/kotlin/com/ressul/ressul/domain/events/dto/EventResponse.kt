package com.ressul.ressul.domain.events.dto

import com.ressul.ressul.domain.events.model.EventEntity
import com.ressul.ressul.domain.events.model.EventStatus

data class EventResponse(
    val id: Long,
    val tutor: String,
    val capacity: Int,
    val eventStatus: EventStatus
)
{
    companion object
    {
        fun from(eventEntity: EventEntity): EventResponse
        {
            return eventEntity.let { EventResponse(it.id!!, it.tutor, it.capacity, it.type) }
        }
    }

}
