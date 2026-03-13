package com.otg.bingo.repository.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class SupabaseSession(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("user") val user:User
)

@Serializable
data class User(
    @SerialName("id") val userId: String,
    @SerialName("user_metadata") val userMetadata: UserMetadata,
)

@Serializable
data class UserMetadata(
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("name") val name: String,
)

fun SupabaseSession.toPersistedSession(): PersistedSession{
    return PersistedSession(
        this.accessToken,
        this.tokenType,
        this.expiresIn,
        this.refreshToken,
        Clock.System.now().epochSeconds,
        this.user.userId
    )
}