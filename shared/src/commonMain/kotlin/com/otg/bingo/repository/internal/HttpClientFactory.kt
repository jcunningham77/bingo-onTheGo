package com.otg.bingo.repository.internal

import com.otg.bingo.util.loggi
import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    private lateinit var client: HttpClient

    fun build(tokenStore: AuthTokenStore): HttpClient {
        client = HttpClient {
            charlesUrl()?.let {
                loggi("charles proxy URL is configured: $it")
                engine { proxy = ProxyBuilder.http(it) }
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(SupabaseAuthPlugin) {
                tokenProvider = { tokenStore.getAuthToken() }
            }

        }
        return client
    }
}

expect fun charlesUrl(): Url?
