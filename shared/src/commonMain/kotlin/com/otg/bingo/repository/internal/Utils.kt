package com.otg.bingo.repository.internal

import io.ktor.client.statement.HttpResponse

internal fun HttpResponse.isSuccess(): Boolean = this.status.value in 200..299
