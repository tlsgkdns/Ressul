package com.ressul.ressul.domain.events.repository

import com.ressul.ressul.domain.events.model.Participant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantRepository : JpaRepository<Participant, Long>