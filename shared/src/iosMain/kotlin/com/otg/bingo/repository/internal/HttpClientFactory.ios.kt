package com.otg.bingo.repository.internal

import io.ktor.http.Url

actual fun charlesUrl(): Url? = null

actual fun isReachable(url: Url, timeoutMs: Int): Boolean = false
