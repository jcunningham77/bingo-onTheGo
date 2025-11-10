package com.otg.bingo.repository

import kotlinx.coroutines.flow.Flow

interface BingoRepository {
    fun getGameThemes(): Flow<List<String>>
}