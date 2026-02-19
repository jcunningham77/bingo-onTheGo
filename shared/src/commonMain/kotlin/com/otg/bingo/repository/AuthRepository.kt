package com.otg.bingo.repository

import com.otg.bingo.repository.internal.OAuthData
import com.otg.bingo.repository.internal.SupabaseSession

interface AuthRepository {


    suspend fun signInWithOauthToken(oAuthData: OAuthData): SupabaseSession

    fun getOauthData(): OAuthData
}