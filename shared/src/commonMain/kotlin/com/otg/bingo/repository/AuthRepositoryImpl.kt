package com.otg.bingo.repository

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AuthRepositoryImpl(
    val authTokenStore: AuthTokenStore = AuthTokenStoreImpl()
) : AuthRepository {


    // FIXME inject client
    private val client = HttpClientFactory.client

    /**
     * Exchanges a Google ID token for a Supabase session.
     *
     * Endpoint: POST {supabaseUrl}/auth/v1/token?grant_type=id_token
     * Body: { "provider": "google", "id_token": "<token>" }
     */
    // TODO this is `suspend`, should it be?
    override suspend fun signInWithOauthToken(oAuthData: OAuthData): SupabaseSession {
        println("JRC signInWithOauthToken = ${oAuthData.token}")
        val url = "$SUPABASE_HOST/auth/v1/token?grant_type=id_token"

        val response = client.post(url) {
            header(API_KEY_KEY, API_KEY_VALUE)
            header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE)
            contentType(ContentType.Application.Json)
            setBody(
                IdTokenGrantRequest(
                    provider = oAuthData.provider.apiValue,
                    idToken = oAuthData.token
                )
            )
        }
        println("JRC signInWithOauthToken, response status = ${response.status}")
        println("signInWithOauthToken, response body = ${response.body<SupabaseSession>()}")
        if (response.status == HttpStatusCode.OK) {

            authTokenStore.saveSession(response.body<SupabaseSession>().toPersistedSession())
        } else {
            println("JRC error signing into Supabase ${response.status}")
        }
        // TODO implement error handling
        return response.body()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun tryRestoreSession(
    ): Boolean {

        println("JRC tryRestoreSession 1")
        val session = authTokenStore.loadSession() ?: return false
        println("JRC tryRestoreSession 2")
        val now = Clock.System.now().epochSeconds
        val isExpired = now >= (session.obtainedAtEpochSeconds + session.expiresInSeconds)
        println("JRC tryRestoreSession 3")
        if (!isExpired) return true
        println("JRC tryRestoreSession 4")
        val refresh = session.refreshToken ?: return false
        return runCatching { signInWithRefreshToken(refresh) }.isSuccess
    }

    private suspend fun signInWithRefreshToken(refreshToken: String) {
        println("JRC signInWithRefreshToken 1")
        val url = "$SUPABASE_HOST/auth/v1/token?grant_type=id_token"
        println("JRC signInWithRefreshToken 2")
        val response = client.post(url) {
            header(API_KEY_KEY, API_KEY_VALUE)
            header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE)
            contentType(ContentType.Application.Json)
            setBody(
                RefreshTokenRequest(refreshToken = refreshToken)
            )
        }
        println("JRC signInWithRefreshToken 3")
        if (response.status == HttpStatusCode.OK) {
            println("JRC signInWithRefreshToken 4")
            authTokenStore.saveSession(response.body<PersistedSession>())
        }else {
            throw Exception("refresh token failed")
        }
    }

    override fun getOauthData(): OAuthData {
        TODO("Not yet implemented")
    }

    companion object {
        private const val SUPABASE_HOST = "https://qwldabjzqgwyxihictth.supabase.co"
        private const val API_KEY_KEY = "apiKey"
        private const val API_KEY_VALUE = "sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU"

        private const val AUTHORIZATION_KEY = "Authorization"
        private const val AUTHORIZATION_VALUE = "Bearer $API_KEY_VALUE"
    }
}

data class OAuthData(val token:String,val provider:OauthProvider)

enum class OauthProvider(val apiValue:String){
    GOOGLE("google")
}

@Serializable
data class IdTokenGrantRequest(
    val provider: String,
    @SerialName("id_token") val idToken: String
)
data class RefreshTokenRequest(
    @SerialName("refresh_token") val refreshToken: String
)


