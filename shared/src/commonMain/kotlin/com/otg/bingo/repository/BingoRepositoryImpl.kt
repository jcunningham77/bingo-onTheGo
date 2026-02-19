package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import com.otg.bingo.repository.internal.HttpClientFactory
import com.otg.bingo.repository.internal.SUPABASE_HOST
import com.otg.bingo.repository.internal.addSupabaseHeaders
import com.otg.bingo.util.loggi
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class BingoRepositoryImpl : BingoRepository {

    // FIXME inject client
    private val client = HttpClientFactory.client


    override fun getGameThemes(): Flow<Result<List<GameTheme>>> = flow {
        val themes = client.get("${SUPABASE_HOST}/rest/v1/GameTheme") { addSupabaseHeaders() }
                .body<List<GameTheme>>()

        emit(Result.success(themes))
    }.catch { e ->
        loggi("BingoRepository  Error fetching themes $e")
        emit(Result.failure(e)) // Emit empty list on error instead of crashing
    }

    override fun getCardTiles(gameThemeId: Int): Flow<Result<List<CardTile>>> = flow {
        val cardTiles =
            client.get("${SUPABASE_HOST}/rest/v1/CardTiles?game_theme_id=eq.$gameThemeId") { addSupabaseHeaders() }
                .body<List<CardTile>>()

        emit(Result.success(cardTiles))
    }
}