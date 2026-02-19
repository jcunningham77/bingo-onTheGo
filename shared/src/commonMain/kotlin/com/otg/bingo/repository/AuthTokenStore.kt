package com.otg.bingo.repository

interface AuthTokenStore {
    suspend fun saveSession(session: PersistedSession)
    suspend fun loadSession(): PersistedSession?
    suspend fun clear()
}

