package com.otg.bingo.createcard.gamethemes

import com.otg.bingo.model.GameTheme
import com.otg.bingo.repository.BingoRepository

// TODO - this is lifecycle unaware to keep it pure KMP
class GameThemesViewModel(
    val repository: BingoRepository
) {
    suspend fun gameThemes(): Result<List<GameTheme>> = repository.getGameThemes()
}
