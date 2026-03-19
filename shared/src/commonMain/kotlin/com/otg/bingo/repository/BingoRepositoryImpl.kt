package com.otg.bingo.repository

import com.otg.bingo.model.CardTile
import com.otg.bingo.model.GameTheme
import com.otg.bingo.model.LeaderboardCard
import com.otg.bingo.model.MyCard
import com.otg.bingo.repository.internal.SUPABASE_HOST
import com.otg.bingo.repository.internal.isSuccess
import com.otg.bingo.util.loggi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class BingoRepositoryImpl(
    val httpClient: HttpClient,
    val authRepository: AuthRepository,
) : BingoRepository {
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

    override suspend fun playCard(gameThemeId: Int): Result<Unit> {
        val savedGameResponse =
            httpClient.post(urlString = "${SUPABASE_HOST}/rest/v1/SavedGames") {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun myCards(): Flow<Result<List<MyCard>>> {
        // FIXME we get infinite retry on 400
        return authRepository
            .currentUser()
            .flatMapLatest { userProfile ->
                if (userProfile == null) {
                    loggi("user profile is null")
                    flowOf(Result.failure(Exception("No user logged in")))
                } else {
                    flow {
                        val response =
                            httpClient.get(
                                urlString = "$SUPABASE_HOST/rest/v1/SavedGames?select=id,created_at,GameTheme(*)&user_id=eq.${userProfile.userId}&order=created_at.desc",
                            )
                        emit(
                            if (response.isSuccess()) {
                                val body = response.body<List<MyCard>>()
                                Result.success(body)
                            } else {
                                loggi("get my cards failed with $response")
                                Result.failure(Exception("GET my cards failed"))
                            },
                        )
                    }
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun leaderboard(): Flow<Result<List<LeaderboardCard>>> {
        // FIXME we get infinite retry on 400
        return authRepository
            .currentUser()
            .flatMapLatest { userProfile ->
                if (userProfile == null) {
                    loggi("user profile is null")
                    flowOf(Result.failure(Exception("No user logged in")))
                } else {
                    flow {
                        val response =
                            httpClient.get(
                                urlString = "$SUPABASE_HOST/rest/v1/saved_games_with_profiles?select=id,created_at,username,avatar_url,GameTheme(*)&order=created_at.desc",
                            )
                        emit(
                            if (response.isSuccess()) {
                                val body = response.body<List<LeaderboardCard>>()
                                Result.success(body)
                            } else {
                                loggi("get my cards failed with $response")
                                Result.failure(Exception("GET my cards failed"))
                            },
                        )
                    }
                }
            }
    }
}
