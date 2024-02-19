package com.ressul.ressul.domain.events.model

import com.ressul.ressul.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "event")
@SQLRestriction(value = "is_deleted = false")
@SQLDelete(sql = "UPDATE event SET is_deleted = true WHERE id = ?")
data class EventEntity(
    @Column(name = "tutor")
    val tutor:String,
    @Column(name = "capacity")
    val capacity: Int,
    @Enumerated(EnumType.STRING)
    var type: EventStatus = EventStatus.PROCEEDING
): BaseEntity()
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "participantsCount")
    var participantCount = 0
    fun addParticipant(): Boolean
    {
        if(type == EventStatus.CLOSED) return false
        ++participantCount
        if(participantCount == capacity) type = EventStatus.CLOSED
        return true
    }
}