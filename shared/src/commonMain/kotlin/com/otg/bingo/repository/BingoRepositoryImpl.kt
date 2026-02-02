package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class BingoRepositoryImpl : BingoRepository {
    private val client = HttpClientFactory.client

    override fun getGameThemes(): Flow<Result<List<GameTheme>>> = flow {

        val themes = client.get("https://qwldabjzqgwyxihictth.supabase.co/rest/v1/GameTheme") {
            header("Authorization", "Bearer sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU")
            header("apikey", "sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU")
        }.body<List<GameTheme>>()

        emit(Result.success(themes))
    }.catch { e ->
        println("BingoRepository JRC Error fetching themes $e")
        emit(Result.failure(e)) // Emit empty list on error instead of crashing
    }

    override fun getCardTiles(gameThemeId: Int): Flow<Result<List<CardTile>>> = flow {
        val cardTiles =
            client.get("https://qwldabjzqgwyxihictth.supabase.co/rest/v1/CardTiles?game_theme_id=eq.$gameThemeId") {
                header("Authorization", "Bearer sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU")
                header("apikey", "sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU")
            }.body<List<CardTile>>()
        emit(Result.success(cardTiles))

    }
}