package com.otg.bingo.repository

import com.otg.bingo.model.GameTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

class BingoRepositoryImpl : BingoRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    override fun getGameThemes(): Flow<List<GameTheme>> = flow {

        val themes = client.get("https://qwldabjzqgwyxihictth.supabase.co/rest/v1/GameTheme") {
            header("Authorization", "Bearer sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU")
            header("apikey", "sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU")
        }.body<List<GameTheme>>()

        emit(themes)
    }.flowOn(Dispatchers.Default)
}