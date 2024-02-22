package com.ressul.ressul.api.member.controller

import com.ressul.ressul.api.member.dto.ProfileResponse
import com.ressul.ressul.api.member.dto.UpdateMemberProfileRequest
import com.ressul.ressul.api.member.service.MemberBusinessService
import com.ressul.ressul.api.oauth2login.Login
import com.ressul.ressul.domain.member.dto.LoginMember
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1")
@RestController
class MemberController(
    private val memberBusinessService: MemberBusinessService
) {
    @GetMapping("/my-profile")
    fun getMemberProfile(
        @Login loginMember: LoginMember
    ): ResponseEntity<ProfileResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberBusinessService.getMemberProfile(loginMember))
    }

    @PutMapping("/my-profile")
    fun updateMemberProfile(
        @Login loginMember: LoginMember,
        @Valid @RequestBody updateMemberProfileRequest: UpdateMemberProfileRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberBusinessService.updateMemberProfile(loginMember, updateMemberProfileRequest))
    }

    @DeleteMapping("/members")
    fun deleteMember(
        @Login loginMember: LoginMember
    ): ResponseEntity<Unit> {
        memberBusinessService.deleteMember(loginMember)
        return ResponseEntity.noContent().build()
    }
}