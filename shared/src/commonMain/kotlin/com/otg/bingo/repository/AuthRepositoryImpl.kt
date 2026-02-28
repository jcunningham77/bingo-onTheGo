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
import kotlinx.coroutines.MainScope
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
    private val scope: CoroutineScope = MainScope(),
) : AuthRepository {

    init {
        scope.launch {
            _currentUser.value = userProfileStore.loadUserProfile()
        }
    }

    // TODO this is `suspend`, should it be?
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
        loggi(" signInWithOauthToken, response status = ${response.status}")
        loggi("signInWithOauthToken, response body = ${response.body<SupabaseSession>()}")
        if (response.status == HttpStatusCode.OK) {
            authTokenStore.saveSession(response.body<SupabaseSession>().toPersistedSession())
        } else {
            loggi(" error signing into Supabase ${response.status}")
        }
        // TODO implement error handling
        return response.body()
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun tryRestoreSession(
    ): Boolean {

        loggi(" tryRestoreSession 1")
        val session = authTokenStore.loadSession() ?: return false
        loggi(" tryRestoreSession 2")
        val now = Clock.System.now().epochSeconds
        val isExpired = now >= (session.obtainedAtEpochSeconds + session.expiresInSeconds)
        loggi(" tryRestoreSession 3")
        if (!isExpired) return true
        loggi(" tryRestoreSession 4")
        val refresh = session.refreshToken ?: return false
        return runCatching { signInWithRefreshToken(refresh) }.isSuccess
    }

    private suspend fun signInWithRefreshToken(refreshToken: String) {
        loggi(" signInWithRefreshToken 1")
        val url = "${SUPABASE_HOST}/auth/v1/token?grant_type=id_token"
        loggi(" signInWithRefreshToken 2")
        val response = httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(
                RefreshTokenRequest(refreshToken = refreshToken)
            )
        }
        loggi(" signInWithRefreshToken 3")
        if (response.status == HttpStatusCode.OK) {
            loggi(" signInWithRefreshToken 4")
            authTokenStore.saveSession(response.body<PersistedSession>())
        } else {
            throw Exception("refresh token failed")
        }
    }

    override fun getOauthData(): OAuthData {
        TODO("Not yet implemented")
    }



    private val _currentUser = MutableStateFlow<UserProfile?>(null)

    override suspend fun setCurrentUser(userProfile: UserProfile) {
        loggi("setUser: $userProfile")
        userProfileStore.setUserProfile(userProfile)
        _currentUser.value = userProfile
    }

    override suspend fun signOut() = flow {
        _currentUser.value = null
        userProfileStore.setUserProfile(null)
        authTokenStore.clear()

        emit(Result.success(Unit))
    }

    override fun currentUser(): Flow<UserProfile?> {
        return _currentUser.asStateFlow()
    }
}
