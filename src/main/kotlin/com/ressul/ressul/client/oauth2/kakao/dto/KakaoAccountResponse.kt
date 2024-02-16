package com.ressul.ressul.client.oauth2.kakao.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class KakaoAccountResponse(
    val profileNicknameNeedsAgreement: Boolean,
    val profileImageNeedsAgreement: Boolean,
    val profile: KakaoUserProfileResponse,
    val hasEmail: Boolean,
    val emailNeedsAgreement: Boolean,
    val isEmailValid: Boolean,
    val isEmailVerified: Boolean,
    val email: String,
)