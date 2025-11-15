package com.otg.bingo.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BingoRepositoryImpl: BingoRepository {
    override fun getGameThemes(): Flow<List<String>> {
        val themes = listOf("Zoo","Animals", "Movies", "Sports", "Food","Pizza")
        return flowOf(themes)
    }
}