package com.otg.bingo.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BingoRepositoryImpl: BingoRepository {
    override fun getGameThemes(): Flow<List<String>> {
        val themes = listOf("Animals", "Movies", "Sports", "Food","pizza")
        return flowOf(themes)
    }
}