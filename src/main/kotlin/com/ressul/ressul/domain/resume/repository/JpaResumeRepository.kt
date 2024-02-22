package com.ressul.ressul.domain.resume.repository

import com.ressul.ressul.domain.resume.model.ResumeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface JpaResumeRepository : JpaRepository<ResumeEntity, Long>{
}