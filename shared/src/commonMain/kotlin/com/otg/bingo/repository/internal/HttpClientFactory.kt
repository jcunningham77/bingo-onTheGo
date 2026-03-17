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
        client =
            HttpClient {
                charlesUrl()?.takeIf { isReachable(it) }
                    ?.let {
                        loggi("charles proxy is reachable, configuring: $it")
                        engine { proxy = ProxyBuilder.http(it) }
                    } ?: loggi("charles proxy not configured or unreachable, skipping")
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                        },
                    )
                }
                install(SupabaseAuthPlugin) {
                    tokenProvider = { tokenStore.getAuthToken() }
                }
            }
        return client
    }


}

expect fun charlesUrl(): Url?
expect fun isReachable(url: Url, timeoutMs: Int = 1000): Boolean
