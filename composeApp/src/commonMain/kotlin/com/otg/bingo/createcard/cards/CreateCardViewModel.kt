package com.otg.bingo.createcard.cards

import com.otg.bingo.model.CardTile
import com.otg.bingo.repository.BingoRepositoryImpl
import kotlinx.coroutines.flow.Flow

class CreateCardViewModel(val repository: BingoRepositoryImpl = BingoRepositoryImpl()) {

    fun cardTiles(gameThemeId: Int): Flow<Result<List<CardTile>>> =
        repository.getCardTiles(gameThemeId)

}