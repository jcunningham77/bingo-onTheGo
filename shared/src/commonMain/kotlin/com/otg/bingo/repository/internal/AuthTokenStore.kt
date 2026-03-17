package com.otg.bingo.repository.internal

interface AuthTokenStore {
    suspend fun saveSession(session: PersistedSession)

    fun loadSession(): PersistedSession?

    suspend fun clear()

    // TODO implement a retry if null?
    fun getAuthToken(): String?
}
