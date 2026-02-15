// `composeApp/src/commonMain/kotlin/com/otg/bingo/auth/AuthRepository.kt`
package com.otg.bingo.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class AuthRepository(
    private val http: HttpClient,
    private val supabaseUrl: String,
    private val supabaseAnonKey: String
) {
    /**
     * Exchanges a Google ID token for a Supabase session.
     *
     * Endpoint: POST {supabaseUrl}/auth/v1/token?grant_type=id_token
     * Body: { "provider": "google", "id_token": "<token>" }
     */
    suspend fun signInWithGoogleIdToken(idToken: String): SupabaseSession {
        val url = supabaseUrl.trimEnd('/') + "/auth/v1/token?grant_type=id_token"

        return http.post(url) {
            header("apikey", supabaseAnonKey)
            header("Authorization", "Bearer $supabaseAnonKey")
            contentType(ContentType.Application.Json)
            setBody(
                IdTokenGrantRequest(
                    provider = "google",
                    idToken = idToken
                )
            )
        }.body()
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
