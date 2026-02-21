package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import com.otg.bingo.repository.internal.AuthTokenStore
import com.otg.bingo.repository.internal.SUPABASE_HOST
import com.otg.bingo.util.loggi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlin.random.Random


class BingoRepositoryImpl(val httpClient: HttpClient, val authTokenStore: AuthTokenStore) : BingoRepository {

    override fun getGameThemes(): Flow<Result<List<GameTheme>>> = flow {
        val themes = httpClient.get("${SUPABASE_HOST}/rest/v1/GameTheme").body<List<GameTheme>>()

        emit(Result.success(themes))
    }.catch { e ->
        loggi("BingoRepository  Error fetching themes $e")
        emit(Result.failure(e)) // Emit empty list on error instead of crashing
    }

    override fun getCardTiles(gameThemeId: Int): Flow<Result<List<CardTile>>> = flow {
        val cardTiles =
            httpClient.get("${SUPABASE_HOST}/rest/v1/CardTiles?game_theme_id=eq.$gameThemeId")
                .body<List<CardTile>>()

        emit(Result.success(cardTiles))
    }

    override fun playCard(gameThemeId: Int): Flow<Result<Unit>> = flow {
        val savedGameResult = httpClient.post(urlString = "${SUPABASE_HOST}/rest/v1/SavedGames")
        {
            // PostgREST requires JSON for inserts
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(mapOf("game_theme_id" to gameThemeId))
        }

        loggi("result = $savedGameResult")
        emit(Random.nextBoolean().let { if (it) Result.success(Unit) else Result.failure(Exception("Supabase exception")) })
    }
}