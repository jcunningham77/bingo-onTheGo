package com.otg.bingo.repository

import com.otg.bingo.model.UserProfile
import com.otg.bingo.repository.internal.OAuthData
import com.otg.bingo.repository.internal.SupabaseSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {


    suspend fun signInWithOauthToken(oAuthData: OAuthData): SupabaseSession

    suspend fun tryRestoreSession(): Boolean



    suspend fun setCurrentUser(userProfile: UserProfile)

    suspend fun signOut():Flow<Result<Unit>>

    fun currentUser(): Flow<UserProfile?>
}