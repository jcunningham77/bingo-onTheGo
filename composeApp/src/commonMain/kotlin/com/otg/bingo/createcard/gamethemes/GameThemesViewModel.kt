package com.otg.bingo.createcard.gamethemes

import com.otg.bingo.model.GameTheme
import com.otg.bingo.repository.BingoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

// TODO - this is lifecycle unaware to keep it pure KMP
class GameThemesViewModel(
    repository: BingoRepository
) {
    val gameThemes: Flow<Result<List<GameTheme>>> = repository.getGameThemes().flowOn(Dispatchers.IO)
}
