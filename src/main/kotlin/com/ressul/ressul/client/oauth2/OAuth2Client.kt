package com.ressul.ressul.client.oauth2

import com.ressul.ressul.common.type.OAuth2Provider

interface OAuth2Client {

    fun generateLoginPageUrl(): String
    fun getAccessToken(authorizationCode: String): String
    fun retrieveUserInfo(accessToken: String): OAuth2LoginUserInfo
    fun supports(provider: OAuth2Provider): Boolean
}