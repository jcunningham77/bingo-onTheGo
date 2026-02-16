// `composeApp/src/commonMain/kotlin/com/otg/bingo/auth/AuthRepository.kt`
package com.otg.bingo.auth

import com.otg.bingo.repository.HttpClientFactory
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class AuthRepository(
    private val supabaseUrl: String="",
    private val supabaseAnonKey: String=""
) {
    private val client = HttpClientFactory.client
    /**
     * Exchanges a Google ID token for a Supabase session.
     *
     * Endpoint: POST {supabaseUrl}/auth/v1/token?grant_type=id_token
     * Body: { "provider": "google", "id_token": "<token>" }
     */
    suspend fun signInWithGoogleIdToken(idToken: String): SupabaseSession {
        println("signInWGoogleToke = $idToken")
        val url = supabaseUrl.trimEnd('/') + "/auth/v1/token?grant_type=id_token"

        return client.post(url) {
            header(API_KEY_KEY, API_KEY_VALUE)
            header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE)
            contentType(ContentType.Application.Json)
            setBody(
                IdTokenGrantRequest(
                    provider = "google",
                    idToken = idToken
                )
            )
        }.body()
    }

    companion object {
        private const val SUPABASE_HOST = "https://qwldabjzqgwyxihictth.supabase.co"
        private const val API_KEY_KEY = "apiKey"
        private const val API_KEY_VALUE = "sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU"

        private const val AUTHORIZATION_KEY = "Authorization"
        private const val AUTHORIZATION_VALUE = "Bearer $API_KEY_VALUE"
    }
}

@Serializable
private data class IdTokenGrantRequest(
    val provider: String,
    @SerialName("id_token") val idToken: String
)

@Serializable
data class SupabaseSession(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("refresh_token") val refreshToken: String? = null
)
