package com.otg.bingo.repository.internal

import com.otg.bingo.model.UserProfile
import kotlinx.serialization.json.Json

class UserProfileStoreImpl(
    private val secureSettings: SecureSettings = getSecureSettings(),
    private val json: Json = Json { ignoreUnknownKeys = true; explicitNulls = false }
): UserProfileStore {

    private val key = "user.profile"

    override suspend fun setUserProfile(userProfile: UserProfile?) {
        userProfile?.let {
            secureSettings.putString(key, json.encodeToString(UserProfile.serializer(),it))
        }?:run{
            secureSettings.remove(key)
        }

    }

    override suspend fun loadUserProfile(): UserProfile? {
        val raw = secureSettings.getStringOrNull(key) ?: return null
        return runCatching { json.decodeFromString(UserProfile.serializer(), raw) }.getOrNull()
    }
}