package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class BingoRepositoryImpl : BingoRepository {

    private val client = HttpClientFactory.client

    private val SUPABASE_HOST = "https://qwldabjzqgwyxihictth.supabase.co"
    private val SUPABASE_KEY = "sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU"

    private fun HttpRequestBuilder.addSupabaseHeaders() {
        header("Authorization", "Bearer $SUPABASE_KEY")
        header("apikey", SUPABASE_KEY)
    }

    override fun getGameThemes(): Flow<Result<List<GameTheme>>> = flow {
        val themes =
            client.get("$SUPABASE_HOST/rest/v1/GameTheme") { addSupabaseHeaders() }
                .body<List<GameTheme>>()

        emit(Result.success(themes))
    }.catch { e ->
        println("BingoRepository JRC Error fetching themes $e")
        emit(Result.failure(e)) // Emit empty list on error instead of crashing
    }

    override fun getCardTiles(gameThemeId: Int): Flow<Result<List<CardTile>>> = flow {
        val cardTiles =
            client.get("$SUPABASE_HOST/rest/v1/CardTiles?game_theme_id=eq.$gameThemeId") { addSupabaseHeaders() }
                .body<List<CardTile>>()

        emit(Result.success(cardTiles))
    }
}