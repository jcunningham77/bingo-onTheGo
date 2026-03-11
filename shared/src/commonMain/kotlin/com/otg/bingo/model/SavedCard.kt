package com.otg.bingo.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedCard(
    @SerialName("id") val id:Int,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("GameTheme") val gameTheme: GameTheme,
    val percentageCompletion: Float = 0f,
    val isComplete: Boolean = false,
    )