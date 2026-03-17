package com.otg.bingo.repository.internal

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.Url

private const val API_KEY_KEY = "apiKey"
private const val API_KEY_VALUE = "sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU"

private const val AUTHORIZATION_KEY = "Authorization"
private const val AUTHORIZATION_VALUE = "Bearer"

private fun supabaseHostOf(url: String): String = Url(url).host

private fun HttpRequestBuilder.isSupabaseRequest(supabaseHost: String): Boolean = url.host == supabaseHost

val SupabaseAuthPlugin =
    createClientPlugin("SupabaseAuthPlugin", ::Config) {
        val supabaseHost = pluginConfig.supabaseHost
        val tokenProvider = pluginConfig.tokenProvider

        onRequest { request, _ ->
            if (!request.isSupabaseRequest(supabaseHost)) return@onRequest

            // Always add API key for Supabase traffic
            request.header(API_KEY_KEY, API_KEY_VALUE)

            // Add Authorization only if available
            val token = tokenProvider()
            if (!token.isNullOrBlank()) {
                request.header(AUTHORIZATION_KEY, "$AUTHORIZATION_VALUE $token")
            }
        }
    }

class Config {
    var supabaseHost: String = supabaseHostOf(SUPABASE_HOST)

    /**
     * Keep it sync; call sites can close over a cached value if needed.
     * If you need suspend, switch to `on(Send)` and run a suspend provider.
     */
    var tokenProvider: () -> String? = { null }
}
