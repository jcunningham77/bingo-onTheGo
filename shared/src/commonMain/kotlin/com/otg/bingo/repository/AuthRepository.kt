package com.otg.bingo.repository

import com.otg.bingo.model.UserProfile
import com.otg.bingo.repository.internal.OAuthData
import com.otg.bingo.repository.internal.SupabaseSession

interface AuthRepository {


    suspend fun signInWithOauthToken(oAuthData: OAuthData): SupabaseSession

    suspend fun tryRestoreSession(): Boolean
    fun getOauthData(): OAuthData


    suspend fun setCurrentUser(userProfile: UserProfile)

    suspend fun getCurrentUser(): UserProfile?
}