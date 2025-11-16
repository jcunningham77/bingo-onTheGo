package com.otg.bingo.repository

import com.otg.bingo.model.GameTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BingoRepositoryImpl : BingoRepository {
    override fun getGameThemes(): Flow<List<GameTheme>> {

        val themes = listOf(
            GameTheme("Carnival", "https://i.imgur.com/fBTMd2w.jpg"),
            GameTheme("Beach", "https://i.imgur.com/jrqTR37.jpg"),
            GameTheme("Happy Hour", "https://i.imgur.com/AS8Dg5e.jpg"),
            GameTheme("Boardwalk", "https://i.imgur.com/aNaLeCt.jpg")
        )

        return flowOf(themes)
    }
}