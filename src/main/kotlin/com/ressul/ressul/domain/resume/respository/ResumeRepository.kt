package com.ressul.ressul.domain.resume.respository

import com.ressul.ressul.domain.resume.model.ResumeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ResumeRepository: JpaRepository<ResumeEntity, Long>