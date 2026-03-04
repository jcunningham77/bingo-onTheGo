package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import kotlinx.coroutines.flow.Flow

interface BingoRepository {
    suspend fun getGameThemes(): Result<List<GameTheme>>

    fun getCardTiles(gameThemeId:Int): Flow<Result<List<CardTile>>>

    suspend fun playCard(gameThemeId:Int): Result<Unit>
}