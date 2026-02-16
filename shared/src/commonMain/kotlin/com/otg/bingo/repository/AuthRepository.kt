package com.otg.bingo.repository

interface AuthRepository {


    suspend fun signInWithOauthToken(oAuthData: OAuthData): SupabaseSession

    fun getOauthData(): OAuthData
}