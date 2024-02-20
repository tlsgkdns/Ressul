package com.ressul.ressul.domain.events.model

import com.ressul.ressul.domain.member.model.MemberEntity
import com.ressul.ressul.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "participants")
@SQLRestriction(value = "is_deleted = false")
@SQLDelete(sql = "UPDATE paticipants SET is_deleted = true WHERE id = ?")
data class Participant(
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	var member: MemberEntity? = null,
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	var event: EventEntity? = null
): BaseEntity()
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null
}