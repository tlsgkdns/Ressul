package com.ressul.ressul.domain.member.repository

import com.ressul.ressul.common.type.OAuth2Provider
import com.ressul.ressul.domain.member.model.MemberEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : CrudRepository<MemberEntity, Long> {
    fun findByProviderAndProviderId(provider: OAuth2Provider, toString: String): MemberEntity?
}