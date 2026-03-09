package com.otg.bingo.model

import kotlinx.datetime.Instant

data class SavedCard(
    val gameTheme: GameTheme,
    val percentageCompletion: Float = 0f,
    val isComplete: Boolean = false,
    val createdAt: Instant
)