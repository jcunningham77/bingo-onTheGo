package createcard.gamethemes

import androidx.compose.runtime.Composable

@Composable
fun GameThemeScreen(
    viewModel: GameThemesViewModel = GameThemesViewModel(),
    onGameThemeSelected: (Int) -> Unit
) {
    GameThemesPager(
        themesFlowResult = viewModel.gameThemes,
        onGameThemeSelected = onGameThemeSelected
    )
}
