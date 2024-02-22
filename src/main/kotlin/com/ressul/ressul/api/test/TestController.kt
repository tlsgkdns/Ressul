package com.ressul.ressul.api.test

import com.ressul.ressul.domain.events.dto.ParticipantsResponse
import com.ressul.ressul.domain.member.dto.LoginMember
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/test")
class TestController(
    private val eventService: TestService
) {

    @GetMapping
    fun test(
        @RequestParam(name = "count") count: Long,
    ): ResponseEntity<Unit> {
        println("count: $count")
        return ResponseEntity.ok().build()
    }

    @PostMapping("/events/{eventId}")
    fun participateEvent(@PathVariable eventId: Long, @RequestBody loginMember: LoginMember): ResponseEntity<ParticipantsResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(eventService.participateEvent(eventId, loginMember))
    }
}