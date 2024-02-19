package com.ressul.ressul.domain.events.dto

import com.ressul.ressul.domain.events.model.Participant

data class ParticipantsResponse(
    val participantId: Long,
    val memberId: Long?,
    val eventId: Long?
)
{
    companion object
    {
        fun from(participant: Participant): ParticipantsResponse
        {
            return participant.let { ParticipantsResponse(it.id!!, it.member?.id, it.event?.id)}
        }
    }
}
