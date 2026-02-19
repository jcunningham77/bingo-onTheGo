package com.otg.bingo.repository.internal

interface AuthTokenStore {
    suspend fun saveSession(session: PersistedSession)
    suspend fun loadSession(): PersistedSession?
    suspend fun clear()
}

