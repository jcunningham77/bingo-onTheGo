package com.otg.bingo.createcard.cards

import com.otg.bingo.model.CardTile
import com.otg.bingo.repository.BingoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CreateCardViewModel(val repository: BingoRepository) {

    private val _events = MutableSharedFlow<CreateCardUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<CreateCardUiEvent> = _events.asSharedFlow()

    sealed interface CreateCardUiEvent {
        data class ShowSuccessMessage(val message: String) : CreateCardUiEvent
        data class ShowErrorMessage(val message: String) : CreateCardUiEvent
    }

    // TODO represent the below as [MutableStateFlow] ?
    fun cardTiles(gameThemeId: Int): Flow<Result<List<CardTile>>> =
        repository.getCardTiles(gameThemeId)

    suspend fun playCard(gameThemeId: Int) {
        val result = repository.playCard(gameThemeId)
        if (result.isSuccess) {
            _events.tryEmit(CreateCardUiEvent.ShowSuccessMessage("Card successfully played"))
        } else {
            _events.tryEmit(CreateCardUiEvent.ShowErrorMessage("Technical issue, please try again later"))
        }
    }
}