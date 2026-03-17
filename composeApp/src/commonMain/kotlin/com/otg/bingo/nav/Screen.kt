package com.otg.bingo.nav

sealed class Screen {
    object CreateCard : Screen()

    object MyCards : Screen()

    object Leaderboard : Screen()
}
