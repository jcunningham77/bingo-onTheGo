package com.otg.bingo.repository.internal

import com.otg.bingo.shared.BuildConfig
import com.otg.bingo.util.loggi
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import java.net.InetSocketAddress
import java.net.Socket

actual fun charlesUrl(): Url? {
    val configHost = BuildConfig.CHARLES_HOST.takeIf { it.isNotBlank() }
    val configPort = BuildConfig.CHARLES_PORT.takeIf { it > 0 }

    return safeLet(configHost, configPort) { h, p ->
        URLBuilder()
            .apply {
                protocol = URLProtocol.HTTP
                host = h
                port = p
            }.build()
    }
}

actual fun isReachable(url: Url, timeoutMs: Int): Boolean {
    return runCatching {
        val executor = java.util.concurrent.Executors.newSingleThreadExecutor()
        val future = executor.submit<Boolean> {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(url.host, url.port), timeoutMs)
                true
            }
        }
        future.get(timeoutMs.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS)
            .also { executor.shutdown() }
        }.getOrElse {
        loggi("charles proxy unreachable: $it")
            false
        }
}

private inline fun <A, B, R> safeLet(
    a: A?,
    b: B?,
    block: (A, B) -> R,
): R? = if (a != null && b != null) block(a, b) else null
