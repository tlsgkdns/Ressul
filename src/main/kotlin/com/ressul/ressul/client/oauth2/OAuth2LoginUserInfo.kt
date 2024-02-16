package com.ressul.ressul.client.oauth2

import com.ressul.ressul.common.type.OAuth2Provider

open class OAuth2LoginUserInfo(
    val id: String,
    val email: String,
    val nickname: String,
    val profileImageUrl: String,
    val thumbnailImageUrl: String,
    val provider: OAuth2Provider,
)