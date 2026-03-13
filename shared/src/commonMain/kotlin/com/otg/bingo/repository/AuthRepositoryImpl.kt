package com.otg.bingo.repository

import com.otg.bingo.model.UserProfile
import com.otg.bingo.repository.internal.AuthTokenStore
import com.otg.bingo.repository.internal.IdTokenGrantRequest
import com.otg.bingo.repository.internal.OAuthData
import com.otg.bingo.repository.internal.PersistedSession
import com.otg.bingo.repository.internal.RefreshTokenRequest
import com.otg.bingo.repository.internal.SUPABASE_HOST
import com.otg.bingo.repository.internal.SupabaseSession
import com.otg.bingo.repository.internal.UserProfileStore
import com.otg.bingo.repository.internal.toPersistedSession
import com.otg.bingo.util.loggi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AuthRepositoryImpl(
    val httpClient: HttpClient,
    val authTokenStore: AuthTokenStore,
    val userProfileStore: UserProfileStore,
    scope: CoroutineScope,
) : AuthRepository {

    init {
        scope.launch {
            _currentUser.value = userProfileStore.loadUserProfile()
        }
    }

    override suspend fun signInWithOauthToken(oAuthData: OAuthData): Result<Unit> {
        loggi("signInWithOauthToken = ${oAuthData.token}")
        val url = "${SUPABASE_HOST}/auth/v1/token?grant_type=id_token"

        val response = httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(
                IdTokenGrantRequest(
                    provider = oAuthData.provider.apiValue,
                    idToken = oAuthData.token
                )
            )
        }

        if (response.status.isSuccess()) {
            val supabaseSession = response.body<SupabaseSession>()
            loggi("persisting user id as = ${supabaseSession.user.userId}")
            authTokenStore.saveSession(response.body<SupabaseSession>().toPersistedSession())
            val userProfile = UserProfile(
                supabaseSession.user.userMetadata.name,
                supabaseSession.user.userMetadata.avatarUrl,
                supabaseSession.user.userId
            )
            setCurrentUser(userProfile)
            return Result.success(Unit)
        } else {
            loggi(" error signing into Supabase ${response.status}")
            return Result.failure(Exception("error signing into Supabase ${response.status}"))
        }
    }

    
    override suspend fun tryRestoreSession(): Boolean {
        val session = authTokenStore.loadSession() ?: return false
        val now = Clock.System.now().epochSeconds
        val isExpired = now >= (session.obtainedAtEpochSeconds + session.expiresInSeconds)
        if (!isExpired) return true
        val refresh = session.refreshToken ?: return false
        return runCatching { signInWithRefreshToken(refresh) }.isSuccess
    }

    private suspend fun signInWithRefreshToken(refreshToken: String) {
        loggi("signInWithRefreshToken: $refreshToken")
        val url = "${SUPABASE_HOST}/auth/v1/token?grant_type=id_token"
        val response = httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(
                RefreshTokenRequest(refreshToken = refreshToken)
            )
        }

        if (response.status.isSuccess()) {
            authTokenStore.saveSession(response.body<PersistedSession>())
        } else {
            throw Exception("refresh token failed")
        }
    }

    private val _currentUser = MutableStateFlow<UserProfile?>(null)

    // TODO does this need to be suspend?
    private suspend fun setCurrentUser(userProfile: UserProfile) {
        loggi("setUser: $userProfile")
        userProfileStore.setUserProfile(userProfile)
        _currentUser.value = userProfile
    }

    override fun currentUser(): Flow<UserProfile?> {
        return _currentUser.asStateFlow()
    }

    override suspend fun signOut(): Result<Unit> {
        loggi("sign out")
        _currentUser.value = null
        userProfileStore.setUserProfile(null)
        authTokenStore.clear()
        return Result.success(Unit)
    }
}
