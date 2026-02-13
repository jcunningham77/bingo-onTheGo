package com.otg.bingo.createcard.gamethemes

import androidx.compose.runtime.Composable
import com.otg.bingo.di.LocalAppComponent

@Composable
fun GameThemeScreen(
    viewModel: GameThemesViewModel = LocalAppComponent.current.gameThemesViewModel,
    onGameThemeSelected: (Int) -> Unit
) {
    GameThemesPager(
        themesFlowResult = viewModel.gameThemes,
        onGameThemeSelected = onGameThemeSelected
    )
}
