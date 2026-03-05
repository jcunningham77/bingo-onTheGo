package com.otg.bingo.auth

import com.otg.bingo.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SettingsViewModel(val repository: AuthRepository) {

    private val _events = MutableSharedFlow<SettingsUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<SettingsUiEvent> = _events.asSharedFlow()

    suspend fun signOut() {
        repository.signOut()
        _events.tryEmit(SettingsUiEvent.SignOutSuccessMessage("Successfully signed out"))
    }

    sealed interface SettingsUiEvent {
        data class SignOutSuccessMessage(val message: String) : SettingsUiEvent
        data class ShowErrorMessage(val message: String) : SettingsUiEvent
    }
}