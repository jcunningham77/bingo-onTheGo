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
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AuthRepositoryImpl(
    val httpClient: HttpClient,
    val authTokenStore: AuthTokenStore,
    val userProfileStore: UserProfileStore,
    // FIXME inject a scope
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) : AuthRepository {

    init {
        scope.launch {
            _currentUser.value = userProfileStore.loadUserProfile()
        }
    }

    override suspend fun signInWithOauthToken(oAuthData: OAuthData): SupabaseSession {
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

        if (response.status == HttpStatusCode.OK) {
            authTokenStore.saveSession(response.body<SupabaseSession>().toPersistedSession())
        } else {
            loggi(" error signing into Supabase ${response.status}")
        }
        // TODO implement error handling
        return response.body()
    }

    @OptIn(ExperimentalTime::class)
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

        if (response.status == HttpStatusCode.OK) {
            authTokenStore.saveSession(response.body<PersistedSession>())
        } else {
            throw Exception("refresh token failed")
        }
    }

    private val _currentUser = MutableStateFlow<UserProfile?>(null)

    override suspend fun setCurrentUser(userProfile: UserProfile) {
        loggi("setUser: $userProfile")
        userProfileStore.setUserProfile(userProfile)
        _currentUser.value = userProfile
    }

    override suspend fun signOut() = flow {
        loggi("sign out")
        _currentUser.value = null
        userProfileStore.setUserProfile(null)
        authTokenStore.clear()
        emit(Result.success(Unit))
    }

    override fun currentUser(): Flow<UserProfile?> {
        return _currentUser.asStateFlow()
    }
}
