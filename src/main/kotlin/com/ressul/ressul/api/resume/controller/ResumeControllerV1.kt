package com.ressul.ressul.api.resume.controller

import com.ressul.ressul.api.oauth2login.Login
import com.ressul.ressul.api.resume.service.ResumeApiServiceV1
import com.ressul.ressul.api.util.responseEntity
import com.ressul.ressul.domain.member.dto.LoginMember
import com.ressul.ressul.domain.resume.dto.CreateResumeRequest
import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.dto.UpdateResumeRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/resumes")
class ResumeControllerV1(
	private val resumeApiServiceV1: ResumeApiServiceV1
) {

	@GetMapping("/{id}")
	fun getResumeById(@PathVariable id: Long) = responseEntity(HttpStatus.OK) {
		resumeApiServiceV1.getResumeById(id)
	}

	@GetMapping("/popular")
	fun getPopularResumeList() = responseEntity(HttpStatus.OK) {
		resumeApiServiceV1.getPopularResume()
	}
	@PostMapping
	fun createResume(@RequestBody dto: CreateResumeRequest, @Login loginMember: LoginMember) = responseEntity(HttpStatus.CREATED) {
		resumeApiServiceV1.createResume(dto, loginMember)
	}

	@PatchMapping("/{id}")
	fun updateResume(@PathVariable id: Long, @RequestBody dto: UpdateResumeRequest, @Login loginMember: LoginMember) = responseEntity(HttpStatus.OK) {
		resumeApiServiceV1.updateResume(id, dto, loginMember)
	}

	@DeleteMapping("/{id}")
	fun deleteResume(@PathVariable id: Long, @Login loginMember: LoginMember) = responseEntity(HttpStatus.NO_CONTENT) {
		resumeApiServiceV1.deleteResume(id, loginMember)
	}

	@GetMapping("/search/{keyword}/{page}")
	fun searchResume(@RequestBody dto: SearchResumeRequest, @PathVariable keyword: String, @PathVariable page: Int) = responseEntity(HttpStatus.OK) {
		resumeApiServiceV1.searchResume(dto, keyword, page)
	}
}