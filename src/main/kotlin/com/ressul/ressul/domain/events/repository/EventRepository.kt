package com.ressul.ressul.domain.events.repository

import com.ressul.ressul.domain.events.model.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<EventEntity, Long>{
}