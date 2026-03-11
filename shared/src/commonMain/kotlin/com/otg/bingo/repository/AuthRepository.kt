package com.otg.bingo.repository

import com.otg.bingo.model.UserProfile
import com.otg.bingo.repository.internal.OAuthData
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signInWithOauthToken(oAuthData: OAuthData): Result<Unit>

    suspend fun tryRestoreSession(): Boolean

    suspend fun setCurrentUser(userProfile: UserProfile)

    suspend fun signOut(): Result<Unit>

    fun currentUser(): Flow<UserProfile?>
}