package com.ressul.ressul.client.oauth2.kakao.dto

import com.ressul.ressul.client.oauth2.OAuth2LoginUserInfo
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.ressul.ressul.common.type.OAuth2Provider

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class KakaoUserInfoResponse(
    id: Long,
    kakaoAccount: KakaoAccountResponse
) : OAuth2LoginUserInfo(
    provider = OAuth2Provider.KAKAO,
    id = id.toString(),
    email = kakaoAccount.email,
    nickname = kakaoAccount.profile.nickname,
    profileImageUrl = kakaoAccount.profile.profileImageUrl,
    thumbnailImageUrl = kakaoAccount.profile.thumbnailImageUrl
)