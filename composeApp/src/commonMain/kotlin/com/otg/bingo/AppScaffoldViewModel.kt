package com.otg.bingo

import com.otg.bingo.model.UserProfile
import com.otg.bingo.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AppScaffoldViewModel(val authRepository: AuthRepository) {

    fun userProfile(): Flow<UserProfile?> {
        return authRepository.currentUser()
    }
}