package com.otg.bingo.leaderboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.otg.bingo.di.LocalAppComponent
import com.otg.bingo.util.loggi
import com.otg.bingo.views.ThemedText
import com.otg.bingo.views.toUIState

@Composable
fun LeaderboardScreen(leaderboardViewModel: LeaderboardViewModel = LocalAppComponent.current.leaderboardViewModel) {

    val leaderBoardCardsResult by leaderboardViewModel.leaderBoardCards().collectAsState(
        initial = Result.success(emptyList()),
    )

    val uiState = leaderBoardCardsResult.toUIState()

    loggi("MyCardsScreen: myCardsResult = $leaderBoardCardsResult")


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ThemedText(
            "Leaderboards screen...",
        )
    }
}
