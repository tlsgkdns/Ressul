package com.ressul.ressul.domain.events.controller

import com.ressul.ressul.api.oauth2login.Login
import com.ressul.ressul.domain.events.dto.EventCreateRequest
import com.ressul.ressul.domain.events.dto.EventResponse
import com.ressul.ressul.domain.events.dto.ParticipantsResponse
import com.ressul.ressul.domain.events.service.EventService
import com.ressul.ressul.domain.member.dto.LoginMember
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/events")
class EventController(
    private val eventService: EventService
) {

    @GetMapping("/{eventId}")
    fun getEvent(@PathVariable eventId: Long): ResponseEntity<EventResponse>
    {
        return ResponseEntity.status(HttpStatus.OK)
            .body(eventService.getEvent(eventId))
    }

    @PostMapping("/{eventId}")
    fun participateEvent(@PathVariable eventId: Long, @Login loginMember: LoginMember): ResponseEntity<ParticipantsResponse>
    {
        return ResponseEntity.status(HttpStatus.OK)
            .body(eventService.participateEvent(eventId, loginMember))
    }

    @PostMapping()
    fun createEvent(@RequestBody eventCreateDTO: EventCreateRequest): ResponseEntity<EventResponse>
    {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(eventService.createEvent(eventCreateDTO))
    }

    @DeleteMapping("/{eventId}")
    fun deleteEvent(@PathVariable eventId: Long): ResponseEntity<UInt>
    {
        eventService.deleteEvent(eventId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}