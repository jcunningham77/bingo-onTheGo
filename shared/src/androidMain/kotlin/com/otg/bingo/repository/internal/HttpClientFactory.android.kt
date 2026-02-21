package com.otg.bingo.repository.internal

import com.otg.bingo.shared.BuildConfig
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url

actual fun charlesUrl(): Url? {

    val configHost = BuildConfig.CHARLES_HOST.takeIf { it.isNotBlank() }
    val configPort = BuildConfig.CHARLES_PORT.takeIf { it > 0 }

    return safeLet(configHost, configPort) { h, p ->
        URLBuilder().apply {
            protocol = URLProtocol.HTTP
            host = h
            port = p
        }.build()
    }
}

private inline fun <A, B, R> safeLet(a: A?, b: B?, block: (A, B) -> R): R? =
    if (a != null && b != null) block(a, b) else null
