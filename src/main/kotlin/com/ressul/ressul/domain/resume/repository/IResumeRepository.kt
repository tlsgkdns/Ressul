package com.ressul.ressul.domain.resume.repository

import com.ressul.ressul.domain.resume.dto.SearchResumeRequest
import com.ressul.ressul.domain.resume.model.ResumeEntity

interface IResumeRepository {
	fun findByIdOrNull(id: Long): ResumeEntity?
	fun save(entity: ResumeEntity): ResumeEntity
	fun delete(entity: ResumeEntity)
	fun searchBy(dto: SearchResumeRequest, keyword: String): List<ResumeEntity>
	fun findAllById(idList: List<Long>): List<ResumeEntity>
}