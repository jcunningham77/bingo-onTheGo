package com.otg.bingo.repository

import com.otg.bingo.model.GameTheme
import kotlinx.coroutines.flow.Flow

interface BingoRepository {
    fun getGameThemes(): Flow<List<GameTheme>>
}