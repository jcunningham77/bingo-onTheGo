package com.otg.bingo.leaderboard

import com.otg.bingo.model.LeaderboardCard
import com.otg.bingo.repository.BingoRepository
import kotlinx.coroutines.flow.Flow

class LeaderboardViewModel(val repository: BingoRepository) {

    fun leaderBoardCards(): Flow<Result<List<LeaderboardCard>>> = repository.leaderboard()
}