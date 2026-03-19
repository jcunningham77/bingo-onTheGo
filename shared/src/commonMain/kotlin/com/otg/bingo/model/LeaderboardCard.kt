package com.otg.bingo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LeaderboardCard(
    @SerialName("id") val id: Int,
    @SerialName("created_at") @Serializable(with = InstantSerializer::class) val createdAt: Instant,
    @SerialName("username") val username:String,
    @SerialName("avatar_url") val avatarUrl:String,
    @SerialName("GameTheme") val gameTheme: GameTheme,
    val percentageCompletion: Float = 0f,
    val isComplete: Boolean = false,
)
