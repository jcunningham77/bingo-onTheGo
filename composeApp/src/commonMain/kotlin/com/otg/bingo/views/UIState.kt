package com.otg.bingo.views

sealed interface UiState {
    data object Loading : UiState
    data object Error : UiState
    data class Content(val items: List<Any>) : UiState
}

fun Result<List<Any>>.toUIState(): UiState {
    return when {
        this.isFailure -> UiState.Error
        this.getOrNull().isNullOrEmpty() -> UiState.Loading
        else -> UiState.Content(this.getOrNull().orEmpty())
    }
}