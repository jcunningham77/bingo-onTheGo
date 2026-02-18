package com.otg.bingo.repository

import kotlinx.serialization.Serializable

interface AuthTokenStore {
    suspend fun saveSession(session: PersistedSession)
    suspend fun loadSession(): PersistedSession?
    suspend fun clear()
}

@Serializable
data class PersistedSession(
    val accessToken: String,
    val tokenType: String,
    val expiresInSeconds: Long,
    val refreshToken: String? = null,
    val obtainedAtEpochSeconds: Long
)