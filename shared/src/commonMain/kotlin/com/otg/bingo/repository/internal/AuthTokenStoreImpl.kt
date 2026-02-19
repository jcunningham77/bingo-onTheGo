package com.otg.bingo.repository.internal

import com.otg.bingo.util.loggi
import kotlinx.serialization.json.Json

class AuthTokenStoreImpl(
    private val secureSettings: SecureSettings = getSecureSettings(),
    private val json: Json = Json { ignoreUnknownKeys = true; explicitNulls = false }
) : AuthTokenStore {

    private val key = "supabase.session.v1"

    override suspend fun saveSession(session: PersistedSession) {
        loggi(" saving session $session")
        secureSettings.putString(key, json.encodeToString(PersistedSession.serializer(), session))
    }

    override suspend fun loadSession(): PersistedSession? {
        val raw = secureSettings.getStringOrNull(key) ?: return null
        return runCatching { json.decodeFromString(PersistedSession.serializer(), raw) }.getOrNull()
    }

    override suspend fun clear() {
        secureSettings.remove(key)
    }
}