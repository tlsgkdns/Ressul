package com.ressul.ressul.domain.events.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class EventCreateRequest(
    @field:NotEmpty(message = "튜터는 반드시 존재해야합니다.")
    val tutor: String,
    @field:Min(value = 1, message = "이벤트는 최소 한 명이 참여 가능해야합니다.")
    val capacity: Int
)
