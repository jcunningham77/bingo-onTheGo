package com.otg.bingo.repository.internal

import com.otg.bingo.util.loggi
import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    val client: HttpClient by lazy {
        HttpClient {
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
        }
    }
}

expect fun charlesUrl(): Url?
