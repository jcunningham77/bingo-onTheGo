package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import com.otg.bingo.model.SavedCard
import kotlinx.coroutines.flow.Flow

interface BingoRepository {
    suspend fun getGameThemes(): Result<List<GameTheme>>

    suspend fun getCardTiles(gameThemeId: Int): Result<List<CardTile>>

    suspend fun playCard(gameThemeId: Int): Result<Unit>

    fun myCards(): Flow<Result<List<SavedCard>>>
}
