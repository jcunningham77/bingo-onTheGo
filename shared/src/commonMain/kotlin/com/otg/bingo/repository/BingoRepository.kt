package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import kotlinx.coroutines.flow.Flow

interface BingoRepository {
    fun getGameThemes(): Flow<Result<List<GameTheme>>>

    fun getCardTiles(gameThemeId:Int): Flow<Result<List<CardTile>>>
}