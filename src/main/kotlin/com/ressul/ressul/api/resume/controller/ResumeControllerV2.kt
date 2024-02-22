package com.ressul.ressul.api.resume.controller

import com.ressul.ressul.api.oauth2login.Login
import com.ressul.ressul.api.resume.service.ResumeApiServiceV2
import com.ressul.ressul.api.util.responseEntity
import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.resume.dto.CreateResumeRequest
import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.dto.UpdateResumeRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/resumes")
class ResumeControllerV2(
	private val resumeApiServiceV2: ResumeApiServiceV2
) {
	@GetMapping("/{id}")
	fun getResumeById(@PathVariable id: Long) = responseEntity(HttpStatus.OK) {
		resumeApiServiceV2.getResumeById(id)
	}

	@GetMapping("/popular")
	fun getPopularResumeList() = responseEntity(HttpStatus.OK) {
		resumeApiServiceV2.getPopularResume()
	}
	@PostMapping
	fun createResume(@RequestBody dto: CreateResumeRequest, @Login loginMember: LoginMember) = responseEntity(HttpStatus.CREATED) {
		resumeApiServiceV2.createResume(dto, loginMember)
	}

	@PatchMapping("/{id}")
	fun updateResume(@PathVariable id: Long, @RequestBody dto: UpdateResumeRequest, @Login loginMember: LoginMember) = responseEntity(
		HttpStatus.OK) {
		resumeApiServiceV2.updateResume(id, dto, loginMember)
	}

	@DeleteMapping("/{id}")
	fun deleteResume(@PathVariable id: Long, @Login loginMember: LoginMember) = responseEntity(HttpStatus.NO_CONTENT) {
		resumeApiServiceV2.deleteResume(id, loginMember)
	}

	@PostMapping("/search/{keyword}/{page}")
	fun searchResume(@RequestBody dto: SearchResumeRequest, @PathVariable keyword: String, @PathVariable page: Int) = responseEntity(
		HttpStatus.OK) {
		resumeApiServiceV2.searchResume(dto, keyword, page)
	}
}