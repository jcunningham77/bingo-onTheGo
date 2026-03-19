package com.otg.bingo.cards

import com.otg.bingo.model.MyCard
import com.otg.bingo.repository.BingoRepository
import kotlinx.coroutines.flow.Flow

class MyCardsViewModel(
    val repository: BingoRepository,
) {
    fun myCards(): Flow<Result<List<MyCard>>> = repository.myCards()
}
