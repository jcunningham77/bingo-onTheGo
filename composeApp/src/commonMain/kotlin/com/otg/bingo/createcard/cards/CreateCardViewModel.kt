package com.otg.bingo.createcard.cards

import com.otg.bingo.model.CardTile
import com.otg.bingo.repository.BingoRepository
import kotlinx.coroutines.flow.Flow

class CreateCardViewModel(val repository: BingoRepository) {

    fun cardTiles(gameThemeId: Int): Flow<Result<List<CardTile>>> =
        repository.getCardTiles(gameThemeId)

}