package com.otg.bingo.repository

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class AuthRepositoryImpl(authTokenStore: AuthTokenStore= AuthTokenStoreImpl()
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

        return response.body()
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

@Serializable
data class SupabaseSession(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("refresh_token") val refreshToken: String? = null
)
