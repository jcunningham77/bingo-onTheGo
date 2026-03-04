package com.otg.bingo.repository.internal

import kotlinx.serialization.SerialName

data class RefreshTokenRequest(
    @SerialName("refresh_token") val refreshToken: String
)