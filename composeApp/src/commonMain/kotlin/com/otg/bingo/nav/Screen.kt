package com.otg.bingo.nav

sealed class Screen {
    object CreateCard : Screen()
    object ViewCards : Screen()
    object Leaderboard : Screen()
}