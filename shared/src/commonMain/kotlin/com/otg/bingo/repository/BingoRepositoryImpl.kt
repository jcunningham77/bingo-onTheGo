package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import com.otg.bingo.model.SavedCard
import com.otg.bingo.repository.internal.SUPABASE_HOST
import com.otg.bingo.util.loggi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow

class BingoRepositoryImpl(val httpClient: HttpClient) : BingoRepository {

    override suspend fun getGameThemes(): Result<List<GameTheme>> {
        val themesHttpResponse = httpClient.get("${SUPABASE_HOST}/rest/v1/GameTheme")

        return if (themesHttpResponse.isSuccess()) {
            Result.success(themesHttpResponse.body<List<GameTheme>>())
        } else {
            Result.failure(Exception("GET game themes failed"))
        }
    }

    override suspend fun getCardTiles(gameThemeId: Int): Result<List<CardTile>> {
        val cardTilesResponse =
            httpClient.get("${SUPABASE_HOST}/rest/v1/CardTiles?game_theme_id=eq.$gameThemeId")

        return if (cardTilesResponse.isSuccess()) {
            Result.success(cardTilesResponse.body<List<CardTile>>())
        } else {
            Result.failure(Exception("GET game themes failed"))
        }
    }

    override suspend fun playCard(gameThemeId: Int): Result<Unit>  {
        val savedGameResponse = httpClient.post(urlString = "${SUPABASE_HOST}/rest/v1/SavedGames")
        {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(mapOf("game_theme_id" to gameThemeId))
        }

        loggi("result = $savedGameResponse")
        return if (savedGameResponse.isSuccess()) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Supabase exception"))
        }
    }

//    override fun myCards(): Flow<Result<List<SavedCard>>> {
//
////        val savedGamesResponse = httpClient.get(urlString = "$SUPABASE_HOST/SavedGames?user_id=eq.<myUserId>")
//        return fl
//    }

    private fun HttpResponse.isSuccess(): Boolean {
        return this.status.value in 200..299
    }
}