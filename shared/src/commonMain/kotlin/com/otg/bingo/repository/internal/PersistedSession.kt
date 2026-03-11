package com.otg.bingo.repository.internal

import kotlinx.serialization.Serializable

@Serializable
data class PersistedSession(
    val accessToken: String,
    val tokenType: String,
    val expiresInSeconds: Long,
    val refreshToken: String? = null,
    val obtainedAtEpochSeconds: Long,
    val userId:String,
)